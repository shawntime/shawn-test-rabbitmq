package com.shawntime.test.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by shma on 2017/5/8.
 */
public class UserReceiverLogsTopic extends ReceiveLogsTopic {
    @Override
    public String getRoutKey() {
        return "*.user";
    }

    public static void main(String[] args) {
        UserReceiverLogsTopic receiver = new UserReceiverLogsTopic();
        try {
            receiver.receiver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
