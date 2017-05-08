package com.shawntime.test.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by shma on 2017/5/8.
 */
public class HahaReceiveLogsTopic extends ReceiveLogsTopic {
    @Override
    public String getRoutKey() {
        return "#.haha.#";
    }

    public static void main(String[] args) {
        HahaReceiveLogsTopic receiver = new HahaReceiveLogsTopic();
        try {
            receiver.receiver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
