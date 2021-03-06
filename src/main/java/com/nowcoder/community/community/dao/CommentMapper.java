package com.nowcoder.community.community.dao;

import com.nowcoder.community.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/27 14:55
 * @Version: 1.0
 * @Description: 评论操作：
 *                  1、查询每页的评论
 *                  2、查询回复的数量（评论的数量在 DiscussPost 中能取到）
 *                  3、添加评论
 *                  4、根据 id查评论
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentsById(int id);
}
