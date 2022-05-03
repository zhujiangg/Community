package com.nowcoder.community.community.service;

import com.nowcoder.community.community.dao.CommentMapper;
import com.nowcoder.community.community.entity.Comment;
import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.HostHolder;
import com.nowcoder.community.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/27 15:09
 * @Version: 1.0
 * @Description: 评论操作：
 *                  1、查询每页的评论
 *                  2、查询回复的数量（评论的数量在 DiscussPost 中能取到）
 *                  3、添加评论
 */
@Service
public class CommentService implements CommunityConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit){
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        System.out.println("===================1"+comment);
        if(comment == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        System.out.println("===================2"+comment);
        // 转义、过滤评论、添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }
}
