# eth watch-only 

## 主要功能
- 提供交易查询和交易广播接口
- 提供区块相关信息查询接口
- 同步区块,生成区块信息，生成分叉消息
- 可配置连接多个自建eth全节点，自动选取可用节点

## 外部依赖
- mysql v5.7
- apollo v2.0
- eureka 
- geth

## kofo内部依赖
- maven本地安装依赖包：
   
    - `mvn install:install-file -DgroupId=io.seg.framework -DartifactId=framework-dao-mybatis -Dversion=1.0.1-SNAPSHOT -Dfile=lib/framework-dao-mybatis-1.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true`
    - `mvn install:install-file -Dgrou1pId=io.seg.framework -DartifactId=framework-dao-api -Dversion=1.0.1-SNAPSHOT -Dfile=lib/framework-dao-api-1.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true`
    - `mvn install:install-file -DgroupId=io.seg.kofo -DartifactId=kofo-common -Dversion=1.0-SNAPSHOT -Dfile=lib/kofo-common-1.0-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true`
     
    

## 表结构
- 见 `ethwo/src/main/resources/scripts/kofo-eth-wo.sql`
- 其中sync_height表保存当前同步高度及区块hash（单条记录），同步区块定时任务会更新该条记录。
- block_height表记录当前节点高度状况、外部节点高度、当前业务回调高度等。只有一条记录。
- block_cache表记录同步到的高度和hash，分叉逻辑依赖该表。
- msg_queue表模拟回调业务方的消息队列，串行回调成功后有定时任务清理该表。

## 启动步骤
1. 搭建并使用apollo服务，eureka服务，eth全节点,mysql
    - apollo配置项参考`ethwo/src/main/resources/application.properties`中注释部分，在apollo中建立对应的应用和集群，将配置复制过去。
    - 建表语句见`ethwo/src/main/resources/scripts/kofo-eth-wo.sql`
2. 编译并打包eth-wo模块，得到kofo-ethwatchonly.jar
    - kofo内部依赖需要安装到本地maven库
3. `java -jar kofo-ethwatchonly.jar` 即可启动服务
 
  

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
