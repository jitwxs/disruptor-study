package jit.wxs.disruptor.demo.multi;

import lombok.Data;

/**
 * @author jitwxs
 * @date 2020年03月15日 16:38
 */
@Data
public class CarEvent {
    private long id;

    @Override
    public String toString() {
        return "Car id: " + id;
    }
}
