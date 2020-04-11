package jit.wxs.disruptor.common.util;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年02月16日 18:14
 */
public class ThreadPoolUtils {
    private static final Logger logger = Logger.getLogger(ThreadPoolUtils.class.getName());

    private static final long TIMEOUT = 100L;

    private static final long KEEP_ALIVE_TIME = 60L;

    private static final int MAX_QUEUE_SIZE = 100_0000;

    private static final String NAME_SEPARATOR = "";

    private static LogUncaughtExceptionHandler EXCEPTION_HANDLER = new LogUncaughtExceptionHandler();

    public static ThreadPoolExecutor poolExecutor(int core, int max, Object... name) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
        if(Objects.nonNull(name)) {
            builder.setNameFormat(Joiner.on(NAME_SEPARATOR).join(name));
        }

        return new ThreadPoolExecutor(core, max, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
                builder.build(), new ThreadPoolExecutor.AbortPolicy());
    }

    public static ScheduledThreadPoolExecutor scheduledExecutor(int core, Object... name) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
        builder.setDaemon(true);
        if(Objects.nonNull(name)) {
            builder.setNameFormat(Joiner.on(NAME_SEPARATOR).join(name));
        }

        return new ScheduledThreadPoolExecutor(core, builder.build());
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
                logger.log(Level.WARNING, "ThreadPoolUtils#shutdown error", e);
            }
            logger.info("ThreadPoolUtils#shutdown success");
        }
    }

    public static void sleep(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "ThreadPoolUtils#sleep error, timeout: " + timeout, e);
        }
    }

    private static class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.log(Level.SEVERE, "Uncaught Exception got, thread: " + t.getName(), e);
        }
    }
}
