# eth watch-only 

## 主要功能
- 提供交易查询和交易广播接口
- 提供区块相关信息查询接口
- 同步区块,生成区块信息，生成分叉消息
- 可配置连接多个自建eth全节点，自动选取可用节点

## 外部依赖
- mysql v5.7
- apollo v2.0
- zookeeper
- eureka 
- sequence 全局序列号产生服务
- seg-job
- geth

## 接口介绍
- /queryAddressNonce 获取地址nonce值
- /allowance 获取erc20代币授权
- /balance 查询地址余额
- /gasPrice 查询gasPrice
- /height 查询当前最新高度
- /token 根据合约地址查询合约相关信息
- /queryTransactionDetails 根据交易hash查询交易详情
- /queryTransactionReceipts 根据交易hash查询交易receipt
- /queryTransaction 根据交易hash查询序列化后的交易
- /sendRawTransaction 发送序列化后的交易
- /sendTransaction 发送结构化的交易
- 以上是通用接口 其余接口为执行特定合约方法

## 定时任务介绍
- BlockHeightUpdateJob 检查更新全节点高度，记录外部api提供的全网高度。
- MsgCallBackDataFlowJob 将区块信息发送给业务方。
- MsgCleanSimpleJob 清理发送成功的区块信息。
- MsgGeneratorDataFlowJob 区块信息同步任务，保持一直在同步最新区块。
