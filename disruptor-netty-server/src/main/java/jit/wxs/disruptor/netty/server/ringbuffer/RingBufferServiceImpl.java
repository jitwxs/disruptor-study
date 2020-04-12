package jit.wxs.disruptor.netty.server.ringbuffer;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import jit.wxs.disruptor.common.netty.Entry;

/**
 * @author jitwxs
 * @date 2020年04月12日 16:45
 */
public class RingBufferServiceImpl implements RingBufferService {
    private RingBufferManagement management;

    public RingBufferServiceImpl(RingBufferManagement management) {
        this.management = management;
    }

    private static final EventTranslatorTwoArg<Entry, Integer, String> TRANSLATOR  = (event, sequence, userId, message) -> {
        event.setUserId(userId);
        event.setMessage(message);
    };

    @Override
    public void publish(int userId, String message) {
        RingBuffer<Entry> ringBuffer = management.getByUserId(userId);
        if(ringBuffer != null) {
            // 转发至用户所属的具体ringBuffer处理
            ringBuffer.publishEvent(TRANSLATOR, userId, message);
        }
    }
}
