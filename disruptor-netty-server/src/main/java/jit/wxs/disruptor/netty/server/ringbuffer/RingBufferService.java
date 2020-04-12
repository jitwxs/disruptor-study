package jit.wxs.disruptor.netty.server.ringbuffer;

/**
 * @author jitwxs
 * @date 2020年04月12日 16:43
 */
public interface RingBufferService {
    void publish(int userId, String message);
}
