package com.nowcoder.community.community.util;

import com.nowcoder.community.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/22 11:12
 * @Version: 1.0
 * @Description: 储存 user 的容器 users
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
