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

    private String exchangeType;

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

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", exchangeType='").append(exchangeType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
