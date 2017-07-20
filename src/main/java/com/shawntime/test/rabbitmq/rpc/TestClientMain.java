package com.shawntime.test.rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.shawntime.test.rabbitmq.rpc.operator.IBaseClientService;
import com.shawntime.test.rabbitmq.rpc.operator.bean.User;
import com.shawntime.test.rabbitmq.rpc.operator.client.BaseClientService;
import com.shawntime.test.rabbitmq.rpc.rabbit.Client;
import com.shawntime.test.rabbitmq.rpc.rabbit.ConnectModel;

/**
 * Created by shma on 2017/5/8.
 */
public class TestClientMain {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectModel model = new ConnectModel();
        model.setHost("127.0.0.1");
        model.setPassword("shawntime");
        model.setUserName("shawntime");
        model.setVirtualHost("Test");
        model.setPort(5672);

        Client client = new Client(model);
        IBaseClientService baseClientService = new BaseClientService(client);
        for(int i=0; i<1; ++i) {
            User userInfo = baseClientService.getUserInfo(1);
            System.out.println(userInfo.getUserId());
            System.out.println(userInfo.getUserName());
        }
        User user = new User();
        user.setUserName("AAA");
        user.setUserId(222);
        System.out.println(baseClientService.save(user));
    }
}
