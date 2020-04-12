package jit.wxs.disruptor.netty.server.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import jit.wxs.disruptor.netty.server.ringbuffer.RingBufferService;

/**
 * @author jitwxs
 * @date 2020年03月22日 23:00
 */
public class NettyServer {
    private int port;

    private RingBufferService ringBufferService;

    private ChannelFuture channelFuture;

    private EventLoopGroup bossGroup, workGroup;

    public NettyServer(int port, RingBufferService ringBufferService) {
        this.port = port;
        this.ringBufferService = ringBufferService;
    }

    public void start() {
        this.bossGroup = new NioEventLoopGroup();
        this.workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap
                    .group(this.bossGroup, this.workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(this.port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                    .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                    .addLast(new NettyServerHandler(ringBufferService));
                        }
                    });

            // 绑定端口，同步等待成功
            this.channelFuture = bootstrap.bind().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void syncClose() {
        try {
            // 关闭监听端口，同步等待
            this.channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程资源
            this.bossGroup.shutdownGracefully();
            this.workGroup.shutdownGracefully();
        }
    }
}
