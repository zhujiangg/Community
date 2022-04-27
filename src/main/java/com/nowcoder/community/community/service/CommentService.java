package com.nowcoder.community.community.service;

import com.nowcoder.community.community.dao.CommentMapper;
import com.nowcoder.community.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/27 15:09
 * @Version: 1.0
 * @Description: 评论操作：
 *                  1、查询每页的评论
 *                  2、查询回复的数量（评论的数量在 DiscussPost 中能取到）
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit){
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType, entityId);
    }
}
