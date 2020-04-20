package jit.wxs.disruptor.netty.client;

import jit.wxs.disruptor.common.netty.Constant;
import jit.wxs.disruptor.common.netty.Entry;
import jit.wxs.disruptor.common.util.ThreadPoolUtils;
import jit.wxs.disruptor.netty.client.handler.NettyClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**
 * 客户端主方法
 * @author jitwxs
 * @date 2020年03月23日 1:25
 */
public class NettyClientMain {
    public static void main(String[] args) {
        // 1. 启动 Netty 客户端
        NettyClient nettyClient = new NettyClient(Constant.NETTY_SERVER_IP, Constant.NETTY_SERVER_PORT);
        nettyClient.start();

        // 2. 向服务端发送消息
        ThreadPoolExecutor executor = ThreadPoolUtils.defaultPoolExecutor("netty-client");
        IntStream.range(1, 5).forEach(index -> executor.execute(() -> {
            IntStream.range(1, 100).forEach(subIndex -> {
                Entry entry = new Entry();
                entry.setUserId(RandomUtils.nextInt(1, 100000));
                entry.setMessage(RandomStringUtils.randomAlphabetic(15));

                nettyClient.sendServer(entry);
            });
        }));

        // 3. 同步等待关闭客户端
        nettyClient.syncClose();
        ThreadPoolUtils.shutdown(executor);
    }
}
