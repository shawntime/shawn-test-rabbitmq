package com.shawntime.test.rabbitmq.rpc.operator;

import com.shawntime.test.rabbitmq.rpc.operator.bean.User;

/**
 * Created by shma on 2017/5/8.
 */
public interface IBaseClientService {

    User getUserInfo(Integer userId);
}
