package com.kama.server.server.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kama.server.provider.ServiceProvider;
import com.kama.server.server.RpcServer;
import com.kama.server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName SimpleRpcServer
 * @Description 简单服务端
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:23
 * @Version v5.0
 */
@AllArgsConstructor
@Slf4j
public class SimpleRpcServer implements RpcServer {
    private ServiceProvider serviceProvider;
    // 控制服务器运行状态
    private AtomicBoolean running = new AtomicBoolean(true);
    private ServerSocket serverSocket;

    @Override
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            log.info("服务器启动了，监听端口：{}", port);
            while (running.get()) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(new WorkThread(socket, serviceProvider)).start();
                } catch (IOException e) {
                    if (running.get()) { // 如果不是因为服务器被停止导致的异常
                        log.error("接受连接时发生异常：{}", e.getMessage(), e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("服务器启动失败：{}", e.getMessage(), e);
        } finally {
            stop();
        }
    }

    @Override
    public void stop() {
        if (!running.get()) return; // 防止重复停止

        running.set(false);
        log.info("服务器正在关闭...");

        // 关闭 ServerSocket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                log.info("服务器已关闭");
            } catch (IOException e) {
                log.error("关闭服务器时发生异常：{}", e.getMessage(), e);
            }
        }
    }
}
