package jit.wxs.disruptor.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import jit.wxs.disruptor.common.netty.TransferEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年03月23日 1:26
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i < 10; i++) {
            TransferEvent event = new TransferEvent();
            event.setId(RandomUtils.nextLong());
            event.setData(RandomStringUtils.randomAlphabetic(15));

            logger.info("Client Send Msg: " + event.toString());

            ctx.writeAndFlush(event.toString());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        try {
            logger.info("ClientHandler Rec: " + s);
        } finally {
            // 客户端使用完毕，要进行释放
            ReferenceCountUtil.release(s);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client channelInactive: " + ctx.channel().localAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(Level.SEVERE, "Client channelInactive " + ctx.channel().localAddress() + " error: ", cause);
    }
}