package jit.wxs.disruptor.netty.server;

import jit.wxs.disruptor.common.netty.Constant;
import jit.wxs.disruptor.netty.server.handler.NettyServer;
import jit.wxs.disruptor.netty.server.ringbuffer.RingBufferManagement;
import jit.wxs.disruptor.netty.server.ringbuffer.RingBufferService;
import jit.wxs.disruptor.netty.server.ringbuffer.RingBufferServiceImpl;

/**
 * @author jitwxs
 * @date 2020年04月12日 16:57
 */
public class NettyServerMain {
    public static void main(String[] args) {
        // 1. 启动 Disruptor 集群
        RingBufferManagement management = new RingBufferManagement();
        management.start();

        // 2. 由于不依赖于注入框架，因此要手动构造出 RingBufferService
        RingBufferService ringBufferService = new RingBufferServiceImpl(management);

        // 3. 启动 Netty 服务端
        NettyServer nettyServer = new NettyServer(Constant.NETTY_SERVER_PORT, ringBufferService);
        nettyServer.start();

        // 4. 同步等待关闭服务端
        nettyServer.syncClose();
    }
}
