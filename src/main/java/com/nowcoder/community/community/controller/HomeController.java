package com.nowcoder.community.community.controller;

import com.nowcoder.community.community.dao.DiscussPostMapper;
import com.nowcoder.community.community.dao.UserMapper;
import com.nowcoder.community.community.entity.DiscussPost;
import com.nowcoder.community.community.entity.Page;
import com.nowcoder.community.community.entity.User;
import com.nowcoder.community.community.service.DiscussPostService;
import com.nowcoder.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/18 10:38
 * @Version: 1.0
 * @Description: 网站首页
 */

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello Spring Boot";
    }

    @RequestMapping(path="/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){

        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        // 将 list 中的 DiscussPost 对象 dp 和 User 对象 user 联合起来放入 map 中，再把所有的map放入 discussPosts 中
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for (DiscussPost post: list) {
                User user = userService.findUserById(post.getUserId());
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        /**
         * 方法调用前,SpringMVC会自动实例化 Model和 Page,并将 Page注入 Model
         * 所以,在 thymeleaf中可以直接访问 Page对象中的数据，这步可省略
         * model.addAttribute("page", page);
         * */
        return "index";
    }

    /**
     *  错误页面
     * */
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
