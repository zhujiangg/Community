package com.nowcoder.community.community;

import com.nowcoder.community.community.dao.UserMapper;
import com.nowcoder.community.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void selectUser() {
        User user = userMapper.selectById(0);
        System.out.println(user);
    }

    @Test
    public void insertUser(){
        User user = new User();
        user.setUsername("test3");
        user.setPassword("1234563");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        int flag = userMapper.insertUser(user);
        System.out.println(flag);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int flag = userMapper.updateStatus(1, 0);
        System.out.println(flag);
    }
}
