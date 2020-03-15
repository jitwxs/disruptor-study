package jit.wxs.disruptor.demo.multi;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 多生产者、多消费者
 * @author jitwxs
 * @date 2020年03月15日 16:48
 */
public class MultiMain {
    private static int BUFFER_SIZE = 1024, CONSUMER_COUNT = 10, PRODUCER_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {
        CarEventFactory factory = new CarEventFactory();

        Disruptor<CarEvent> disruptor = new Disruptor<>(factory, BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new YieldingWaitStrategy());
        // 设置消费者
        disruptor.handleEventsWithWorkerPool(buildConsumers());
        // 启动 Disruptor
        RingBuffer<CarEvent> ringBuffer = disruptor.start();

        CountDownLatch mainLatch = new CountDownLatch(1);
        // 创建生产者列表，生产数据
        for(int i = 0; i < PRODUCER_SIZE; i++) {
            CarEventTranslator translator = new CarEventTranslator(String.format("Producer-%d", i + 1), ringBuffer);
            new Thread(() -> {
                try {
                    mainLatch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 每个生产者生产10条数据
                for(int j = 0; j < 10; j++) {
                    translator.publish(RandomUtils.nextLong(1, 1000));
                }
            }).start();
        }

        // 等待生产者的线程都创建完毕
        TimeUnit.SECONDS.sleep(1);
        mainLatch.countDown();

        // 预留时间让消费者消费
        TimeUnit.SECONDS.sleep(2);

        // 停止Disruptor
        disruptor.shutdown();
    }

    private static CarEventHandler[] buildConsumers() {
        CarEventHandler[] consumers = new CarEventHandler[CONSUMER_COUNT];
        for(int i = 0; i  < consumers.length; i++) {
            consumers[i] = new CarEventHandler(String.format("Handler-%d", i+1));
        }
        return consumers;
    }
}
