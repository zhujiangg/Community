package com.nowcoder.community.community.dao;

import com.nowcoder.community.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
 *                  5、修改帖子评论数量
 */
@Mapper
public interface DiscussPostMapper {

    //可通过 userId 查询，为后续开发个人主页（某个人分布的帖子）准备
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    // 增加帖子
    int insertDiscussPost(DiscussPost discussPost);

    // 查询帖子
    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
