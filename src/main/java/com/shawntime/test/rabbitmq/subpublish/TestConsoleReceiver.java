package com.shawntime.test.rabbitmq.subpublish;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by shma on 2017/5/6.
 */
public class TestConsoleReceiver extends TestLogReceiver {

    @Override
    public void parseLog(String log) {
        System.out.println(log);
    }

    public static void main(String[] args) {
        TestLogReceiver testLogReceiver = new TestConsoleReceiver();
        try {
            testLogReceiver.receiver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
