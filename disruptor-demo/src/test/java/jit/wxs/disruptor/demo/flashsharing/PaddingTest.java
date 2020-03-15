package jit.wxs.disruptor.demo.flashsharing;

import jit.wxs.disruptor.common.util.DateUtils;
import jit.wxs.disruptor.demo.flashsharing.bean.FlashSharingConstant;
import jit.wxs.disruptor.demo.flashsharing.bean.PaddingLong;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用 padding 填充解决伪共享
 * @author jitwxs
 * @date 2020年03月15日 23:15
 */
public class PaddingTest {
    private static PaddingLong[] longs;

    public static void main(final String[] args) throws Exception {
        System.out.println("Starting PaddingTest....");
        longs = new PaddingLong[FlashSharingConstant.NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new PaddingLong();
        }
        final long start = DateUtils.nowTime();
        runTest();
        System.out.println("Duration = " + (DateUtils.nowTime() - start) + "ms");
    }

    private static void runTest() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(FlashSharingConstant.NUM_THREADS);
        for(int i = 0; i < FlashSharingConstant.NUM_THREADS; i++) {
            new Thread(new TestPaddingThread(startLatch, endLatch, i, FlashSharingConstant.ITERATIONS, longs)).start();
        }

        TimeUnit.SECONDS.sleep(1);
        startLatch.countDown();

        endLatch.await();
    }

    private static class TestPaddingThread implements Runnable {

        private CountDownLatch startLatch;
        private CountDownLatch endLatch;
        private Integer arrayIndex;
        private Long runningCount;
        private PaddingLong[] longs;

        public TestPaddingThread(CountDownLatch startLatch, CountDownLatch endLatch, Integer arrayIndex, Long runningCount, PaddingLong[] longs) {
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