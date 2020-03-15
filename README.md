## Origin Version

基于 [Disruptor](https://github.com/LMAX-Exchange/disruptor) 3.4.2 版本

## Tree Construction

```
disruptor-study
  - disruptor-common: 基础工具包
  - disruptor-source: 官方源码包
  - disruptor-demo：新增 Case
```

## New Case

以完成 Case 包括：

- [x] 单生产者单消费者：`jit.wxs.disruptor.demo.quickstart`
- [x] 单生产者单消费者，消费者多边形消费：`jit.wxs.disruptor.demo.quickstart2`
- [x] 多生产者多消费者：`jit.wxs.disruptor.demo.multi`
- [x] 纯 RingBuffer，单生产者单消费者：`jit.wxs.disruptor.demo.ringbuffer`
- [x] 纯 RingBuffer，多生产者多消费者：`jit.wxs.disruptor.demo.ringbuffer2`

## Complete Annotation

## Change Logs

### 1.0.0

- 创建项目，源码基于 Disruptor 3.4.2
- 添加已完成 Case

