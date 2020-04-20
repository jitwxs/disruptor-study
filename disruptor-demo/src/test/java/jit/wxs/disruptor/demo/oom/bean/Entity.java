package jit.wxs.disruptor.demo.oom.bean;

import lombok.Data;

import java.util.List;

/**
 * RingBuffer 承载对象
 * @author jitwxs
 * @date 2020年04月18日 18:36
 */
@Data
public class Entity {
    private long id;

    private List<EntityData> dataList;
}


