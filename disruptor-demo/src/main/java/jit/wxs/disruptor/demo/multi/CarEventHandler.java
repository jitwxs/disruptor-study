package jit.wxs.disruptor.demo.multi;

import com.lmax.disruptor.WorkHandler;

/**
 * 多消费者，只能实现 WorkHandler 接口
 * @author jitwxs
 * @date 2020年03月15日 16:39
 */
public class CarEventHandler implements WorkHandler<CarEvent> {
    private String consumerName;

    public CarEventHandler(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void onEvent(CarEvent event) throws Exception {
        System.out.println(String.format("%s Consumer event: %s", this.consumerName, event.toString()));
    }
}
