package jit.wxs.disruptor.demo.multi;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author jitwxs
 * @date 2020年03月15日 16:41
 */
public class CarEventTranslator {
    private RingBuffer<CarEvent> ringBuffer;

    private String producerName;

    public CarEventTranslator(String producerName, RingBuffer<CarEvent> ringBuffer) {
        this.producerName = producerName;
        this.ringBuffer = ringBuffer;
    }

    private EventTranslatorOneArg<CarEvent, Long> translator = new EventTranslatorOneArg<CarEvent, Long>() {
        @Override
        public void translateTo(CarEvent event, long sequence, Long carId) {
            event.setId(carId);
            System.out.println(String.format("%s Producer seq: %d, carId: %d", producerName, sequence, carId));
        }
    };

    public void publish(Long carId) {
        this.ringBuffer.publishEvent(translator, carId);
    }
}
