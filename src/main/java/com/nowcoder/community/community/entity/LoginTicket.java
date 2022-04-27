package com.nowcoder.community.community.entity;

import java.util.Date;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/21 15:29
 * @Version: 1.0
 * @Description: 登录凭证类
 * +---------+-------------+------+-----+---------+----------------+
 * | Field   | Type        | Null | Key | Default | Extra          |
 * +---------+-------------+------+-----+---------+----------------+
 * | id      | int(11)     | NO   | PRI | NULL    | auto_increment |
 * | user_id | int(11)     | NO   |     | NULL    |                |
 * | ticket  | varchar(45) | NO   | MUL | NULL    |                |
 * | status  | int(11)     | YES  |     | 0       |                |    '0-有效; 1-无效;'
 * | expired | timestamp   | NO   |     | NULL    |                |
 * +---------+-------------+------+-----+---------+----------------+
 */
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "LoginTicket{" + "id=" + id + ", userId=" + userId + ", ticket='" + ticket + '\'' + ", status=" + status + ", expired=" + expired + '}';
    }
}
