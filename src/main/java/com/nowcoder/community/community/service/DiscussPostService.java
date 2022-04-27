package com.nowcoder.community.community.service;

import com.nowcoder.community.community.dao.DiscussPostMapper;
import com.nowcoder.community.community.entity.DiscussPost;
import com.nowcoder.community.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/18 10:14
 * @Version: 1.0
 * @Description: 帖子操作：
 *                  1、查询每页的帖子
 *                  2、查询帖子数量
 *                  3、增加帖子
 *                  4、根据 id查询帖子
 */

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost discussPost){
        if(discussPost == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        // 转义html
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
}
