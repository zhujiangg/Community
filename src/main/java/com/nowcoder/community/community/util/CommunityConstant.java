package com.nowcoder.community.community.util;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/20 15:47
 * @Version: 1.0
 * @Description: 激活状态：成功、重复激活、失败
 *               cookie_MaxAge：默认 12h、记住我 100d
 *               实体类型: 帖子、评论、用户
 */
public interface CommunityConstant {
    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPEAT = 1;
    int ACTIVATION_FAILURE = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 30;

    int ENTITY_TYPE_POST = 1;
    int ENTITY_TYPE_COMMENT = 2;
    int ENTITY_TYPE_USER = 3;

}
