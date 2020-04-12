package jit.wxs.disruptor.netty.server.handler;

import com.lmax.disruptor.EventHandler;
import jit.wxs.disruptor.common.netty.Entry;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2020年04月12日 14:55
 */
public class DisruptorEntryHandler implements EventHandler<Entry> {
    private static Logger logger = Logger.getLogger(DisruptorEntryHandler.class.getName());

    /**
     * 当前handle所属modulo
     */
    private int modulo;

    public DisruptorEntryHandler(int modulo) {
        this.modulo = modulo;
    }

    @Override
    public void onEvent(Entry event, long sequence, boolean endOfBatch) throws Exception {
        logger.info(String.format("[modulo-%d]: Start process data, userId: %s", this.modulo, event.getUserId()));

        try {
            // 模拟业务耗时操作
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
