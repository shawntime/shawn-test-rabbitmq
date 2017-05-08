package com.shawntime.test.rabbitmq.rpc;

import java.io.Serializable;

/**
 * Created by shma on 2017/5/8.
 */
public class RpcInvokeModel implements Serializable {

    //调用序号
    private String did;

    //回调队列名称
    private String callBackQueueName;

    private String className;

    private String methodName;

    private Object[] arguments;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getCallBackQueueName() {
        return callBackQueueName;
    }

    public void setCallBackQueueName(String callBackQueueName) {
        this.callBackQueueName = callBackQueueName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
