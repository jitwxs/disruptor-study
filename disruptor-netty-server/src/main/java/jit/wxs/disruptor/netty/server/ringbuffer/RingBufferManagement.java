package jit.wxs.disruptor.netty.server.ringbuffer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import jit.wxs.disruptor.common.netty.Constant;
import jit.wxs.disruptor.common.netty.Entry;
import jit.wxs.disruptor.netty.server.handler.DisruptorEntryHandler;

import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

/**
 * Disruptor消费模型：
 * - 单生产者: 在 {@link jit.wxs.disruptor.netty.server.handler.NettyServerHandler} 中添加进 RingBuffer
 * - 单消费者: 由 {@link Constant#RING_BUFFER_SIZE} 组成的 RingBuffer 分片组
 * @author jitwxs
 * @date 2020年04月12日 14:38
 */
public class RingBufferManagement {
    /**
     * k: modulo; v: ringBuffer
     */
    private RingBuffer<Entry>[] ringBuffers;

    private static final String THREAD_NAME="disruptor-";

    public void start() {
        this.ringBuffers = new RingBuffer[Constant.RING_BUFFER_SIZE];
        IntStream.range(0, Constant.RING_BUFFER_SIZE).forEach(modulo -> {
            ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
            ThreadFactory threadFactory = threadFactoryBuilder.setNameFormat(THREAD_NAME + modulo).build();

            Disruptor<Entry> disruptor = new Disruptor<>(Entry::new, Constant.RING_BUFFER_SIZE, threadFactory,
                    ProducerType.SINGLE, new BlockingWaitStrategy());
            disruptor.handleEventsWith(new DisruptorEntryHandler(modulo));

            this.ringBuffers[modulo] = disruptor.getRingBuffer();

            disruptor.start();
        });
    }

    public RingBuffer<Entry> getByModulo(int modulo) {
        return this.ringBuffers[modulo];
    }

    public RingBuffer<Entry> getByUserId(int userId) {
        return getByModulo(getModulo(userId));
    }

    private int getModulo(final int userId) {
        return userId & (Constant.RING_BUFFER_SIZE - 1);
    }
}
