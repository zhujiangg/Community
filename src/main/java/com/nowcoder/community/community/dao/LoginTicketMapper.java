package com.nowcoder.community.community.dao;

import com.nowcoder.community.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/21 15:52
 * @Version: 1.0
 * @Description: 登录凭证操作：
 *                  1、增加凭证
 *                  2、根据 ticket查询凭证（来自 cookie）
 *                  3、修改凭证状态
 */

@Mapper
@Deprecated // 已弃用
public interface LoginTicketMapper {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectByTicket(String ticket);
    int updateStatus(String ticket, int status);
}
