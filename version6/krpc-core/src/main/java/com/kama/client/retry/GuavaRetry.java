package com.kama.client.retry;

import com.github.rholder.retry.*;
import com.kama.client.rpcclient.RpcClient;
import common.message.RpcRequest;
import common.message.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName guavaRetry
 * @Description 重试策略
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:44
 * @Version v5.0
 */
@Slf4j
public class GuavaRetry {

    public RpcResponse sendServiceWithRetry(RpcRequest request, RpcClient rpcClient) {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                //无论出现什么异常，都进行重试
                .retryIfException()
                //返回结果为 error时进行重试
                .retryIfResult(response -> Objects.equals(response.getCode(), 500))
                //重试等待策略：等待 2s 后再进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                //重试停止策略：重试达到 3 次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试第 {} 次", attempt.getAttemptNumber());
                    }
                })
                .build();
        try {
            return retryer.call(() -> rpcClient.sendRequest(request));
        } catch (Exception e) {
            log.error("重试失败: 请求 {} 执行时遇到异常", request.getMethodName(), e);
        }
        return RpcResponse.fail("重试失败，所有重试尝试已结束");
    }
}
