package jit.wxs.disruptor.demo.oom;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import jit.wxs.disruptor.demo.oom.bean.Entity;
import jit.wxs.disruptor.demo.oom.bean.EntityData;

import java.util.List;

class EntityEventTranslator {
    private final RingBuffer<Entity> ringBuffer;

    public EntityEventTranslator(RingBuffer<Entity> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorTwoArg<Entity, Long, List<EntityData>> TRANSLATOR = (event, sequence, id, dataList) -> {
        event.setId(id);
        event.setDataList(dataList);
    };

    /**
     * 将event放入ringBuffer
     */
    public void publish(Long id, List<EntityData> dataList) {
        ringBuffer.publishEvent(TRANSLATOR, id, dataList);
    }
}