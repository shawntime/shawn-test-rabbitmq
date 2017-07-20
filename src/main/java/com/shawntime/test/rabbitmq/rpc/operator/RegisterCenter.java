package com.shawntime.test.rabbitmq.rpc.operator;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Created by shma on 2017/5/8.
 */
public class RegisterCenter {

    private static final Map<Integer, ParamBean> dataMap = Maps.newHashMap();

    static {
        ParamBean paramBean = new ParamBean();
        paramBean.setMethodName("getUserInfo");
        paramBean.setClassName("com.shawntime.test.rabbitmq.rpc.operator.service.BaseClientService");
        dataMap.put(1, paramBean);
        ParamBean paramBean2 = new ParamBean();
        paramBean2.setMethodName("save");
        paramBean2.setClassName("com.shawntime.test.rabbitmq.rpc.operator.service.BaseClientService");
        dataMap.put(2, paramBean2);
    }

    public static ParamBean getMethodBean(int id) {
        return dataMap.get(id);
    }
}
