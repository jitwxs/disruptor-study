package jit.wxs.disruptor.demo.flashsharing.bean;

import sun.misc.Contended;

/**
 * 使用@Contended解决伪共享
 * 需开启 JVM 参数 -XX:-RestrictContended
 * @author jitwxs
 * @date 2020年03月15日 23:10
 */
@Contended
public class ContendedLong {
    public volatile long value = 0L;
}
