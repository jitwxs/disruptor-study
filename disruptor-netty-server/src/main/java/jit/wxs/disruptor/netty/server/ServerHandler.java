package jit.wxs.disruptor.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年03月22日 23:00
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    Logger logger = Logger.getLogger(ServerHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        logger.info("ServerHandler Rec: " + s);

        ctx.writeAndFlush("resp_" + s);
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
