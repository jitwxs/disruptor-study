package jit.wxs.disruptor.demo.flashsharing.bean;

/**
 * 存在伪共享问题测试
 * @author jitwxs
 * @date 2020年03月15日 23:15
 */
public class SharingLong {
    public volatile long value = 0L;
}
