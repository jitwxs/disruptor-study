package jit.wxs.disruptor.netty.client.handler;

import com.github.jitwxs.commons.core.json.JacksonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import jit.wxs.disruptor.common.netty.Entry;

/**
 * @author jitwxs
 * @date 2020年04月12日 14:35
 */
public class NettyClient {
    private String ip;

    private int port;

    private ChannelFuture channelFuture;

    private EventLoopGroup workGroup;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        this.workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                .addLast(new LengthFieldPrepender(4))
                                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                .addLast(new NettyClientHandler());
                    }
                });

        try {
            this.channelFuture = bootstrap.connect(this.ip, this.port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务端发送消息
     */
    public void sendServer(Entry entry) {
        this.channelFuture.channel().writeAndFlush(JacksonUtils.objectToJson(entry));
    }

    public void syncClose() {
        try {
            // 等待关闭连接channel
            this.channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程资源
            this.workGroup.shutdownGracefully();
        }
    }
}
