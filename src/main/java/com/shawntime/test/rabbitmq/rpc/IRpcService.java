package com.shawntime.test.rabbitmq.rpc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by shma on 2017/5/8.
 */
public interface IRpcService<T> {

    T call(RpcInvokeModel model) throws IOException, InterruptedException, ExecutionException;
}
