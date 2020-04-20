package jit.wxs.disruptor.demo.oom.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jitwxs
 * @date 2020年04月18日 18:37
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EntityData {
    private long id;

    private String message;
}
