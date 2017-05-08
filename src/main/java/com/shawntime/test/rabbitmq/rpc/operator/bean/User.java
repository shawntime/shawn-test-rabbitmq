package com.shawntime.test.rabbitmq.rpc.operator.bean;

import java.io.Serializable;

/**
 * Created by shma on 2017/5/8.
 */
public class User implements Serializable {

    private int userId;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
