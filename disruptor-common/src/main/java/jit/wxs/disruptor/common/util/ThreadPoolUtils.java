package jit.wxs.disruptor.common.util;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年02月16日 18:14
 */
public class ThreadPoolUtils {
    private static final Logger logger = Logger.getLogger(ThreadPoolUtils.class.getName());

    private static final long TIMEOUT = 100L, KEEP_ALIVE_TIME = 60L;

    private static final int MAX_QUEUE_SIZE = 100_0000;

    public static ThreadPoolExecutor poolExecutor(int core, int max, Object... name) {
        ThreadFactory factory = Objects.nonNull(name) ?
                new ThreadFactoryBuilder().setNameFormat(Joiner.on(" ").join(name)).build() :
                new ThreadFactoryBuilder().build();

        return new ThreadPoolExecutor(core, max, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(MAX_QUEUE_SIZE), factory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ScheduledThreadPoolExecutor scheduledExecutor(int core, Object... name) {
        ThreadFactory factory = Objects.nonNull(name) ?
                new ThreadFactoryBuilder().setNameFormat(Joiner.on(" ").join(name)).setDaemon(true).build() :
                new ThreadFactoryBuilder().setDaemon(true).build();

        return new ScheduledThreadPoolExecutor(core, factory);
    }

    public static void shutdown(ExecutorService executor) {
        if(executor != null) {
            executor.shutdown();

            try {
                while (!executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS)) {
                    logger.info("ThreadPoolUtils#shutdown is not shutdown yet");
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (InterruptedException e) {
                logger.warning( "ThreadPoolUtils#shutdown error" + e);
            }
            logger.info("ThreadPoolUtils#shutdown success");
        }
    }

    public static void sleep(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            logger.info( "ThreadPoolUtils#sleep error, timeout: {}" + timeout + e);
        }
    }
}
