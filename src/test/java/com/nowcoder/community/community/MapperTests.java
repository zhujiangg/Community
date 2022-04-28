package com.nowcoder.community.community;

import com.nowcoder.community.community.dao.DiscussPostMapper;
import com.nowcoder.community.community.dao.LoginTicketMapper;
import com.nowcoder.community.community.dao.MessageMapper;
import com.nowcoder.community.community.dao.UserMapper;
import com.nowcoder.community.community.entity.DiscussPost;
import com.nowcoder.community.community.entity.LoginTicket;
import com.nowcoder.community.community.entity.Message;
import com.nowcoder.community.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void selectUser() {
        User user = userMapper.selectByName("朱江");
        System.out.println(user);
    }

    @Test
    public void insertUser() {
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
    public void updateUser() {
        int flag = userMapper.updateStatus(1, 0);
        System.out.println(flag);
    }

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post);
        }
        System.out.println("==========================================");
        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testLoginTicket() {
//        LoginTicket loginTicket = new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setTicket("abc");
//        loginTicket.setStatus(0);
//        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
//        loginTicketMapper.insertLoginTicket(loginTicket);

//        LoginTicket loginTicket1 = loginTicketMapper.selectByTicket("abc");
//        System.out.println(loginTicket1);
        System.out.println(new Date(System.currentTimeMillis() + 100 * 1000));

//        loginTicketMapper.updateStatus("abc", 1);
    }

    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }
        System.out.println("============================================");

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
        System.out.println("============================================");

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }
        System.out.println("============================================");

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);
        System.out.println("============================================");

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }
}
