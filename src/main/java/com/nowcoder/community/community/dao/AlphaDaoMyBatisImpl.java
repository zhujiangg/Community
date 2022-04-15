package com.nowcoder.community.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary //使用 @Autowired 时相较于 AlphaDaoHibernateImpl 优先注入
public class AlphaDaoMyBatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "MyBatis";
    }
}
