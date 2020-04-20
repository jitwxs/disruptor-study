package jit.wxs.disruptor.demo.oom;

import com.lmax.disruptor.EventHandler;
import jit.wxs.disruptor.demo.oom.bean.Entity;

class EntityEventHandler implements EventHandler<Entity> {

    @Override
    public void onEvent(Entity event, long sequence, boolean endOfBatch) throws Exception {
        // 从 ringBuffer 中消费数据
        System.out.println("EntityEventHandler Sequence: " + sequence + ", subList size: " + event.getDataList().size());
    }
}