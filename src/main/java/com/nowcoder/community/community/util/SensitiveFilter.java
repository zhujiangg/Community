package com.nowcoder.community.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/25 12:22
 * @Version: 1.0
 * @Description: 敏感词过滤
 *          1、TreeNode：创建前缀树类
 *          2、init()：加载敏感词文件数据
 *          3、addKeyword(String keyword)：构造前缀树
 *          4、filter(String text)：过滤敏感词
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private TreeNode root = new TreeNode();
    // 加载敏感词文件数据
    @PostConstruct
    public void init(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.text");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String keyword;
        try {
            while((keyword = reader.readLine()) != null){
                addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    // 若 subNode为null，则创建，否则说明重复
    public void addKeyword(String keyword){
        TreeNode temp = root;
        for (int i = 0; i < keyword.length(); i++) {
            TreeNode subNode = temp.getSubNode(keyword.charAt(i));
            if(subNode == null){
                subNode = new TreeNode();
                temp.addSubNode(keyword.charAt(i), subNode);
            }
            temp = subNode;
            if(i == keyword.length()-1){
                temp.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *  @param text 待过滤的文本
     *  @return 过滤后的文本
     */
    public String filter(String text) {

        if (StringUtils.isBlank(text)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        // 3个指针
        TreeNode tempNode = root;
        int begin = 0, position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);

            // 敏感词外部（tempNode 指针 指向根节点）的符号跳过，内部的转换：#赌#博# -> #***#
            if (isSymbol(c)) {
                if (tempNode == root) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                /***
                 * 注意这里三行：若 abd 为文本，abc 为敏感词
                 * position 到 c 位置发现不对，此时 begin 在 a 位置
                 * 要将 begin 添加，并将 position 回退
                 */
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = root;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = root;
            } else {
                position++;
            }
        }

        // position 到最后了，不需要回退至 ++begin 位置继续开始，直接将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断是否为符号（不是东亚文字和Ascii码）
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TreeNode{
        // 关键词结尾标识（即敏感词）
        private boolean isKeywordEnd = false;

        // 子节点集合(key是下级字符, value是下级节点)
        private Map<Character, TreeNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c, TreeNode node){
            subNodes.put(c, node);
        }
        public TreeNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
