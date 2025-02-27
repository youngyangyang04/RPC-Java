package common.serializer.mycoder;


import common.message.MessageType;
import common.message.RpcRequest;
import common.message.RpcResponse;
import common.serializer.myserializer.Serializer;
import common.trace.TraceContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @ClassName MyEncoder
 * @Description 编码器
 * @Author Tong
 * @LastChangeDate 2024-11-29 10:32
 * @Version v5.0
 */
@Slf4j
@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        log.debug("Encoding message of type: {}", msg.getClass());
        //1.写入trace消息头
        String traceMsg= TraceContext.getTraceId() +";"+TraceContext.getSpanId();
        byte[] traceBytes=traceMsg.getBytes();
        //  1.1写入traceMsg长度
        out.writeInt(traceBytes.length);
        //  1.2写入traceBytes
        out.writeBytes(traceBytes);

        //2.写入消息类型
        if (msg instanceof RpcRequest) {
            out.writeShort(MessageType.REQUEST.getCode());
        } else if (msg instanceof RpcResponse) {
            out.writeShort(MessageType.RESPONSE.getCode());
        } else {
            log.error("Unknown message type: {}", msg.getClass());
            throw new IllegalArgumentException("Unknown message type: " + msg.getClass());
        }
        //3.写入序列化方式
        out.writeShort(serializer.getType());
        //得到序列化数组
        byte[] serializeBytes = serializer.serialize(msg);
        if (serializeBytes == null || serializeBytes.length == 0) {
            throw new IllegalArgumentException("Serialized message is empty");
        }
        //4.写入长度
        out.writeInt(serializeBytes.length);
        //5.写入序列化数组
        out.writeBytes(serializeBytes);
    }
}
