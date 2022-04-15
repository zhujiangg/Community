package com.nowcoder.community.community.controller;

import com.nowcoder.community.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    /******************************************  spring  **************************************************
     *  一、在 test 中利用 applicationContext 获取 bean
     *  二、利用 @Autowired 自动注入bean，controller -> service -> dao
     * */
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello Spring Boot";
    }

    @Autowired
    AlphaService alphaService;

    @ResponseBody
    @RequestMapping("/data")
    public String getData(){
        return alphaService.find();
    }

    /****************************************  springmvc  **********************************************
     *  一、传统通过 HttpServletRequest、HttpServletResponse 方式来请求数据（get、post…）、响应数据
     *  二、get请求参数的两种方式（url 或 ？&），post提交参数的方式
     *  三、响应数据的两种方式：HTML（返回 ModelAndView 或 String）和 JSON（异步请求）
     * **/

    /**  1、传统方式  */

    @RequestMapping("/http")
    public void http(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        // 获取请求数据
        System.out.println(servletRequest.getMethod());
        System.out.println(servletRequest.getServletPath());
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = servletRequest.getHeader(name);
            System.out.println(name + ": " + value);
        }
        // 返回相应数据
        servletResponse.setContentType("text/html; charset=utf-8");
        PrintWriter writer = servletResponse.getWriter();
        writer.write("<h1>智网院<h1>");
    }

    /**  2、get请求两种传参方式，post提交方式  */

    /*
        2.1 /student?id=111&name=aaa
        @RequestParam 设置是否能为空，设置参数默认值
    */

    @RequestMapping(value = "/getstudent", method = RequestMethod.GET)
    @ResponseBody
    public void getStudent1(
            @RequestParam(name="id", required = false, defaultValue = "111") int id,
            @RequestParam(name="name", required = false, defaultValue = "aaa") String name){
        System.out.println(id);
        System.out.println(name);
    }

    /*
        2.2 /student/111
        {id} 大括号表示变量
        @PathVariable 设置路径变量
    */

    @RequestMapping(value = "/getstudent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void getStudent2(@PathVariable(name = "id") int id){
        System.out.println(id);
    }

    /*
        2.3 访问static下的静态资源：inis/html/student.html
        提交表单 <form> 后跳转回当前路径
    */

    @RequestMapping(path = "/poststudent", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(int id, String name) {
        System.out.println(id);
        System.out.println(name);
        return "success";
    }

    /**  3、响应数据的两种方式：HTML 和 JSON */

    /*
        3.1 HTML：返回 ModelAndView 或 String（优先这种）两种方式
    */

    @RequestMapping("teacher1")
    public ModelAndView respTeacher1(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id", 222);
        modelAndView.addObject("name", "李四");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    @RequestMapping("teacher2")
    public String respTeacher2(Model model){
        model.addAttribute("id", 111);
        model.addAttribute("name", "张三");
        return "/demo/view";
    }

    /*
        3.2 JSON(异步请求)：Java对象 -> JSON字符串 -> JS对象
        比如检验昵称是否被占用，此时未提交页面但已经访问服务器
    */

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", 24);
        emp.put("salary", 9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", 25);
        emp.put("salary", 10000.00);
        list.add(emp);

        return list;
    }
}
