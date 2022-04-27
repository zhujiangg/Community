# SSMStudy
spring
=======
1. 在 test 中利用 applicationContext 获取 bean
2. 利用 @Autowired 自动注入bean，controller -> service -> dao

springmvc
========
1. 传统通过 HttpServletRequest、HttpServletResponse 方式来请求数据（get、post…）、响应数据
2. get请求参数的两种方式（url 或 ？&），post提交参数的方式
3. 响应数据的两种方式：HTML（返回 ModelAndView 或 String）和 JSON（异步请求）

mybatis
=======
1. 设置数据库
2. 导入依赖：mysql、mybatis-spring-boot
3. 配置 mysql 和 mybatis 属性
4. 创建 entity 包中实体类 User
5. 在 dao 下创建对应的 Usermapper(增删改查)
6. resources 下创建 mapper，创建 usermapper.xml
7. 配置 xml 模板，编写 sql
8. test测试
