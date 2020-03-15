package jit.wxs.disruptor.demo.multi;

import com.lmax.disruptor.EventFactory;

/**
 * @author jitwxs
 * @date 2020年03月15日 16:39
 */
public class CarEventFactory implements EventFactory<CarEvent> {
    @Override
    public CarEvent newInstance() {
        return new CarEvent();
    }
}
