package com.shawntime.test.rabbitmq.rpc.operator.service;

import com.shawntime.test.rabbitmq.rpc.operator.IBaseClientService;
import com.shawntime.test.rabbitmq.rpc.operator.bean.User;

/**
 * Created by shma on 2017/5/8.
 */
public class BaseClientService implements IBaseClientService {

    public User getUserInfo(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserName("亚洲舞王");
        return user;
    }

    public int save(User user) {
        return 1;
    }
}
