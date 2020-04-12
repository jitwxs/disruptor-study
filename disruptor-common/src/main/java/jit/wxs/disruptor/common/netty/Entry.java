package jit.wxs.disruptor.common.netty;


import lombok.Data;

/**
 * Disruptor 与 Netty 整合项目，传递对象
 * @author jitwxs
 * @date 2020年04月12日 14:50
 */
@Data
public class Entry {
    private int userId;

    private String message;

    @Override
    public String toString() {
        return userId + ": " + message;
    }
}
