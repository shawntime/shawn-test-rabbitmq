package com.shawntime.test.rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.shawntime.test.rabbitmq.rpc.rabbit.ConnectModel;
import com.shawntime.test.rabbitmq.rpc.rabbit.Service;

/**
 * Created by shma on 2017/5/8.
 */
public class TestServerMain {

    public static void main(String[] args) throws IOException, TimeoutException {
        String queueName = "rpc_queue_name";
        ConnectModel model = new ConnectModel();
        model.setHost("127.0.0.1");
        model.setPassword("shawntime");
        model.setUserName("shawntime");
        model.setVirtualHost("Test");
        model.setPort(5672);

        Service service = new Service(model, queueName);
    }
}
