package part1.Client.rpcClient.impl;

import part1.Client.rpcClient.RpcClient;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SimpleSocketRpcCilent implements RpcClient {
    private String host;
    private int port;
    public SimpleSocketRpcCilent(String host,int port){
        this.host=host;
        this.port=port;
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            Socket socket=new Socket(host, port);
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response=(RpcResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
