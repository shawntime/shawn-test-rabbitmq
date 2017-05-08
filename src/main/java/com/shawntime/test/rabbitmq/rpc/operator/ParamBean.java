package com.shawntime.test.rabbitmq.rpc.operator;

/**
 * Created by shma on 2017/5/8.
 */
public class ParamBean {

    private String methodName;

    private String className;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
