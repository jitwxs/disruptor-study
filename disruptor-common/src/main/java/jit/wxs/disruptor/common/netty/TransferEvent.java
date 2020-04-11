package jit.wxs.disruptor.common.netty;

import lombok.*;

import java.io.Serializable;

/**
 * Netty 传输实体
 * @author jitwxs
 * @date 2020年03月22日 22:58
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferEvent implements Serializable {
    private static final long serialVersionUID = 19722066008272961L;

    private long id;

    private String data;
}
