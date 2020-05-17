package jit.wxs.disruptor.demo.flashsharing;

import com.github.jitwxs.commons.core.date.TimeUtils;
import jit.wxs.disruptor.demo.flashsharing.bean.ContendedLong;
import jit.wxs.disruptor.demo.flashsharing.bean.FlashSharingConstant;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用@Contended解决伪共享
 * 需开启 JVM 参数 -XX:-RestrictContended
 * @author jitwxs
 * @date 2020年03月15日 23:10
 */
public class ContendedTest {
    private static ContendedLong[] longs;

    public static void main(final String[] args) throws Exception {
        System.out.println("Starting ContendedTest....");
        longs = new ContendedLong[FlashSharingConstant.NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new ContendedLong();
        }
        final long start = TimeUtils.nowMs();
        runTest();
        System.out.println("Duration = " + TimeUtils.diffMs(start) + "ms");
    }

    private static void runTest() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(FlashSharingConstant.NUM_THREADS);
        for(int i = 0; i < FlashSharingConstant.NUM_THREADS; i++) {
            new Thread(new TestContendedThread(startLatch, endLatch, i, FlashSharingConstant.ITERATIONS, longs)).start();
        }

        TimeUnit.SECONDS.sleep(1);
        startLatch.countDown();

        endLatch.await();
    }

    private static class TestContendedThread implements Runnable {
        private CountDownLatch startLatch;
        private CountDownLatch endLatch;
        private Integer arrayIndex;
        private Long runningCount;
        private ContendedLong[] longs;

        public TestContendedThread(CountDownLatch startLatch, CountDownLatch endLatch, Integer arrayIndex, Long runningCount, ContendedLong[] longs) {
            this.startLatch = startLatch;
            this.endLatch = endLatch;
            this.arrayIndex = arrayIndex;
            this.runningCount = runningCount;
            this.longs = longs;
        }

        @Override
        public void run() {
            try {
                startLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (0 != --runningCount) {
                longs[arrayIndex].value = runningCount;
            }

            endLatch.countDown();
        }
    }
}