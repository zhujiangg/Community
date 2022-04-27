package com.nowcoder.community.community.dao;

import com.nowcoder.community.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: ZhuJiang
 * @Date: 2022/3/18 10:14
 * @Version: 1.0
 * @Description: 用户操作：
 *                  1、通过 id查询用户
 *                  2、通过 name查询用户
 *                  3、通过 email查询用户
 *                  4、插入用户
 *                  5、更新用户状态
 *                  6、更新用户头像
 *                  7、更新用户密码
 */
@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String name);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
