package jit.wxs.disruptor.demo.oom;

import com.google.common.collect.Lists;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import jit.wxs.disruptor.demo.oom.bean.Entity;
import jit.wxs.disruptor.demo.oom.bean.EntityData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测试 RingBuffer Size 设置过大时，内存溢出问题
 * -Xms256m -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\disruptor_oom.hprof
 * @author jitwxs
 * @date 2020年04月18日 18:35
 */
public class OOMTest {
    private static int BUFFER_SIZE = 65536;
//    private static int BUFFER_SIZE = 128;

    public static void main(String[] args) throws InterruptedException {
        Disruptor<Entity> disruptor = new Disruptor<>(Entity::new, BUFFER_SIZE, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());

        // 2. 添加消费者
        disruptor.handleEventsWith(new EntityEventHandler());

        // 3. 启动Disruptor
        RingBuffer<Entity> ringBuffer = disruptor.start();

        // 4. 创建生产者
        EntityEventTranslator producer = new EntityEventTranslator(ringBuffer);

        // 5. 死循环发送事件
        while (true) {
            long id = RandomUtils.nextLong(1, 100000);
            List<EntityData> dataList = mockData(RandomUtils.nextInt(10, 1000));

            producer.publish(id, dataList);

            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

    private static List<EntityData> mockData(int size) {
        List<EntityData> result = Lists.newArrayListWithCapacity(size);
        for(int i = 0; i < size; i++) {
            result.add(new EntityData(RandomUtils.nextLong(100000, 1000000), RandomStringUtils.randomAlphabetic(1, 100)));
        }
        return result;
    }
}
