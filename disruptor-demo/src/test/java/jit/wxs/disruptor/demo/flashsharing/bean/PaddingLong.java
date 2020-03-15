package jit.wxs.disruptor.demo.flashsharing.bean;

/**
 * 使用 padding 填充解决伪共享
 * @author jitwxs
 * @date 2020年03月15日 23:15
 */
public class PaddingLong {
    protected long p1, p2, p3, p4, p5, p6, p7;
    public volatile long value = 0L;
    protected long p9, p10, p11, p12, p13, p14, p15;
}
