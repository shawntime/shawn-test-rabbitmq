package com.shawntime.test.rabbitmq.helloworld;

import java.io.Serializable;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-22 14:59
 * Desc:
 */
public class User implements Serializable {

    private int id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
