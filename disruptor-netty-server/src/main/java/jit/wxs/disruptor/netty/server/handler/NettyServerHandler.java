package jit.wxs.disruptor.netty.server.handler;

import com.github.jitwxs.commons.core.util.JacksonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jit.wxs.disruptor.common.netty.Entry;
import jit.wxs.disruptor.netty.server.ringbuffer.RingBufferService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年03月22日 23:00
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    private static Logger logger = Logger.getLogger(NettyServerHandler.class.getName());

    private RingBufferService ringBufferService;

    public NettyServerHandler(RingBufferService ringBufferService) {
        this.ringBufferService = ringBufferService;
    }

    /**
     * 接收到客户端消息时处理
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Entry entry = JacksonUtils.jsonToObject(s, Entry.class);
        logger.info("Server Rec: " + entry);

        // 转发至 Disruptor
        this.ringBufferService.publish(entry.getUserId(), entry.getMessage());

        // 告知客户端，收到请求数据
        ctx.writeAndFlush("Server Rec: " + entry.getUserId());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Server channelInactive: " + ctx.channel().localAddress());
    }

    /**
     * 服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.log(Level.SEVERE, "Server channelInactive " + ctx.channel().localAddress() + " error: ", cause);
    }
}
