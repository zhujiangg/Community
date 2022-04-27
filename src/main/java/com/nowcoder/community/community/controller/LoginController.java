package com.nowcoder.community.community.controller;

import com.google.code.kaptcha.Producer;
import com.mysql.cj.Session;
import com.nowcoder.community.community.config.KaptchaConfig;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.service.UserService;
import com.nowcoder.community.community.util.CommunityConstant;
import com.nowcoder.community.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/19 16:24
 * @Version: 1.0
 * @Description: 注册页面
 */
@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    /**
     *  注册成功就跳转激活页面 "/site/operate-result"
     *  失败就跳转原页面并提示错误信息，同时保留原注册信息（在表单中设置默认值为上一次使用的值）
     * */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map==null || map.isEmpty()){
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }


    /**
     * 根据service层的状态来跳转（激活成功跳转登录页面、其余跳转主页）
     * http://localhost:8080/inis/activation/id/activationCode
     * */
    @RequestMapping(value = "/activation/{id}/{activationCode}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("id") int id, @PathVariable("activationCode") String activationCode){
        int result = userService.activation(id, activationCode);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    // 生成验证码及图片并存入 session
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        // 生成验证码及图片
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    /**
     *  登录：
     *      remeber 记住我，expiredSeconds变长
     *      session 验证码在 session中
     *      cookie 里面存的是 LoginTicket
     * */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String KaptCode, boolean rememberme,
                        Model model, HttpSession session, HttpServletResponse response) {
        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(KaptCode) || !kaptcha.equalsIgnoreCase(KaptCode)) {
            model.addAttribute("codeMsg", "验证码不正确!");
            return "/site/login";
        }

        // 检查账号,密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    /**
     *  退出登录：
     *      从 Cookie 获取 ticket
     * */
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
