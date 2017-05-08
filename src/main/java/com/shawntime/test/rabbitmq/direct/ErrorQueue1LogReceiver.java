package com.shawntime.test.rabbitmq.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by shma on 2017/5/8.
 */
public class ErrorQueue1LogReceiver extends AbstractLogReceiver {
    @Override
    public String getQueueName() {
        return "log_direct_queue_1";
    }

    @Override
    public String getRoutKey() {
        return "direct_error_key";
    }

    public static void main(String[] args) {
        ErrorQueue1LogReceiver receiver = new ErrorQueue1LogReceiver();
        try {
            receiver.receiver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
