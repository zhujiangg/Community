package com.nowcoder.community.community.entity;

import java.util.Date;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/17 16:54
 * @Version: 1.0
 * @Description: 用户类
 * +-----------------+--------------+------+-----+---------+----------------+
 * | Field           | Type         | Null | Key | Default | Extra          |
 * +-----------------+--------------+------+-----+---------+----------------+
 * | id              | int(11)      | NO   | PRI | NULL    | auto_increment |
 * | username        | varchar(50)  | YES  | MUL | NULL    |                |
 * | password        | varchar(50)  | YES  |     | NULL    |                |
 * | salt            | varchar(50)  | YES  |     | NULL    |                |
 * | email           | varchar(100) | YES  | MUL | NULL    |                |
 * | type            | int(11)      | YES  |     | NULL    |                |   '0-普通用户; 1-超级管理员; 2-版主;'
 * | status          | int(11)      | YES  |     | NULL    |                |   '0-未激活; 1-已激活;'
 * | activation_code | varchar(100) | YES  |     | NULL    |                |
 * | header_url      | varchar(200) | YES  |     | NULL    |                |
 * | create_time     | timestamp    | YES  |     | NULL    |                |
 * +-----------------+--------------+------+-----+---------+----------------+
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password + '\'' + ", salt='" + salt + '\'' + ", email='" + email + '\'' + ", type=" + type + ", status=" + status + ", activationCode='" + activationCode + '\'' + ", headerUrl='" + headerUrl + '\'' + ", createTime=" + createTime + '}';
    }

}
