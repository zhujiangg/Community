package com.nowcoder.community.community.service;

import com.nowcoder.community.community.dao.LoginTicketMapper;
import com.nowcoder.community.community.dao.UserMapper;
import com.nowcoder.community.community.entity.LoginTicket;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.CommunityUtil;
import com.nowcoder.community.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/18 10:18
 * @Version: 1.0
 * @Description: 用户相关服务
 *  1、通过 id查找用户、通过 name查找用户
 *  2、用户注册（提交账号、密码、邮箱），返回一个 Map<String, Object>集合（放入提示信息，若为 null说明成功注册）
 *      2.1、判断为空（user, username, password, email）
 *      2.2、验证账号、邮箱
 *      2.3、生成用户（加密密码，把用户各信息注入 user对象）
 *      2.4、发送激活邮件
 *  3、用户激活
 *  4、用户登录（提交账号、密码、过期时间），返回一个 Map<String, Object>集合（放入提示信息，若为 null说明成功登录）
 *      4.1、判断为空（username, password）
 *      4.2、验证账号、状态、密码
 *      4.3、生成登录凭证
 *  5、用户退出（更改状态）
 */

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    // 拼接 url: http://localhost:8080/inis
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 判断为空，user为空抛出异常，其他为空不是逻辑异常，是service异常，返回map
        if (user == null) {
            throw new IllegalArgumentException("user参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在！");
            return map;
        }

        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }

        /**
         *  注册用户(password、salt、type、status、activationCode、headerUrl、createTime)id主键自增不需生成
         *  username、email 通过表单 post 方式提交进 user（在 @{/register} 的 user 中）；password 不通过post提交，需要加密等处理
         * */
        // salt 由 UUID随机生成，不需太长
        // password = password + salt
        // headerUrl 提供默认头像地址（0~1000）："http://images.nowcoder.com/head/1t.png"
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        /**
         *  发送激活邮件
         *  不使用 Controller 在 test 中调用 /mail/demo.html
         *      TemplateEngine templateEngine 扫描装配
         *      Context context 设置属性参数
         * */
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/inis/activation/id/activationCode
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    // 激活邮件
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        //验证账号、状态、密码
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    // 退出登录
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);
    }
    // 查询 登录凭证
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
    // 更新 用户头像
    public int updateHeader(int userId, String haaderUrl){
        return userMapper.updateHeader(userId, haaderUrl);
    }

    /**
     * 修改密码
     *  1、原密码正确，则将密码修改为新密码，并重定向到退出功能
     *  2、若错误则返回到账号设置页面，给与相应提示
     * */
    public Map<String, Object> updatePassword(int userId, String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(oldPassword)){
            map.put("oldPasswordMsg", "原密码不能为空!");
            return map;
        }
        if(StringUtils.isBlank(newPassword)){
            map.put("newPasswordMsg", "新密码不能为空!");
            return map;
        }

        // 验证原始密码
        User user = userMapper.selectById(userId);
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(oldPassword)) {
            map.put("oldPasswordMsg", "原密码错误！");
        }

        // 更新密码

        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(userId, newPassword);

        return map;
    }
}
