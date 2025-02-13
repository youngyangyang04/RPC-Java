package com.kama.client.rpcclient.impl;

import com.kama.client.rpcclient.RpcClient;
import common.message.RpcRequest;
import common.message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @ClassName SimpleSocketRpcClient
 * @Description 实现简单客户都
 * @Author Tong
 * @LastChangeDate 2024-12-02 10:12
 * @Version v5.0
 */
public class SimpleSocketRpcClient implements RpcClient {
    private String host;
    private int port;

    public SimpleSocketRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        // 定义响应对象
        RpcResponse response = null;

        // 创建 Socket 和流对象
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            // 发送请求对象
            oos.writeObject(request);
            oos.flush();

            // 接收响应对象
            response = (RpcResponse) ois.readObject();

        } catch (UnknownHostException e) {
            System.err.println("未知的主机: " + host);
        } catch (IOException e) {
            System.err.println("I/O 错误: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("无法识别的类: " + e.getMessage());
        }

        return response;
    }

    @Override
    public void close() {

    }
}
