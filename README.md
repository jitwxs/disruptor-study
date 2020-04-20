## Origin Version

基于 [Disruptor](https://github.com/LMAX-Exchange/disruptor) 3.4.2 版本

## Tree Construction

```
disruptor-study
  - disruptor-common: 基础工具包
  - disruptor-source: 官方源码包
  - disruptor-demo：新增 Case
  - disruptor-netty-server: Disruptor 整合 Netty 服务端
  - disruptor-netty-client: Disruptor 整合 Netty 客户端
```

## New Case

以完成 Disruptor Case 包括：

- [x] 单生产者单消费者：`jit.wxs.disruptor.demo.quickstart`
- [x] 单生产者单消费者，消费者多边形消费：`jit.wxs.disruptor.demo.quickstart2`
- [x] 多生产者多消费者：`jit.wxs.disruptor.demo.multi`
- [x] 纯 RingBuffer，单生产者单消费者：`jit.wxs.disruptor.demo.ringbuffer`
- [x] 纯 RingBuffer，多生产者多消费者：`jit.wxs.disruptor.demo.ringbuffer2`

以完成 Test Case 包括：

- [x] 缓存行测试：`jit.wxs.disruptor.demo.flashsharing`
- [x] RingBuffer Size 过大导致 OOM：`jit.wxs.disruptor.demo.oom`

## Netty Case

该 Case 的重点并不是 Netty，因此关于 Netty 部分使用了最简单的启动代码。

实现的效果是客户端向服务端通过 Netty 长连接的方式发送数据，Netty 端接收到数据后，根据用户所属的分片，分发到对应的 RingBuffer 中进行消费。

- Start Server: `jit.wxs.disruptor.netty.server.NettyServerMain`
- Start Client:  `jit.wxs.disruptor.netty.client.NettyClientMain`

