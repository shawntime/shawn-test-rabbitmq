package com.shawntime.test.rabbitmq.subpublish;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

/**
 * Created by shma on 2017/5/6.
 */
public class TestSaveReceiver extends TestLogReceiver {

    @Override
    public void parseLog(String log) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("F:/rabbitmq/data.log", true);
            fw.write(log);
            fw.write("\r\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TestLogReceiver testLogReceiver = new TestSaveReceiver();
        try {
            testLogReceiver.receiver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
