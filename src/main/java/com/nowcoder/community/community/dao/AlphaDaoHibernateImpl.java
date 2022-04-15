package com.nowcoder.community.community.dao;

import org.springframework.stereotype.Repository;

//默认使用首字母小写类名装配 alphaDaoHibernateImpl, 也可重新取个简易名字
@Repository("alphaHi")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
