

package io.seg.kofo.ethwo.biz.service.impl;

import io.seg.kofo.api.enums.MsgTypeEnum;
import io.seg.kofo.api.response.ResponseEnum;
import io.seg.kofo.ethwo.biz.service.BlockCacheService;
import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.biz.service.MsgQueueService;
import io.seg.kofo.ethwo.biz.service.SyncHeightService;
import io.seg.kofo.ethwo.common.config.FullNodeCache;
import io.seg.kofo.ethwo.common.config.WatchOnlyProperties;
import io.seg.kofo.ethwo.common.exception.CodedBizException;
import io.seg.kofo.ethwo.dao.po.BlockCachePo;
import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import io.seg.kofo.ethwo.dao.po.MsgQueuePo;
import io.seg.kofo.ethwo.dao.po.SyncHeightPo;
import io.seg.kofo.ethwo.model.bo.SegBlock;
import com.alibaba.fastjson.JSON;
import io.seg.elasticjob.common.collect.Lists;
import io.seg.framework.dao.BaseDao;
import io.seg.framework.dao.model.Order;
import io.seg.framework.dao.model.QueryResult;
import io.seg.kofo.ethwo.model.bo.TransactionInfo;
import io.seg.kofo.ethwo.model.bo.TransactionReceipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import java.io.IOException;
import java.util.*;


@Slf4j
@Service
public class MsgQueueServiceImpl implements MsgQueueService {
      @Autowired
      private BaseDao baseDao;
      @Autowired
      private WatchOnlyProperties watchOnlyProperties;
      @Autowired
      private MsgQueueService msgQueueService;
      @Autowired
      private BlockCacheService blockCacheService;
      @Autowired
      private SyncHeightService syncHeightService;
      @Autowired
      private BlockHeightService blockHeightService;
      @Autowired
      FullNodeCache fullNodeCache;

      @Override
      public MsgQueuePo selectOne(MsgQueuePo msgQueue) {
       return baseDao.selectOne(msgQueue);
      }

      @Override
      public QueryResult<MsgQueuePo> selectLimit(MsgQueuePo msgQueue) {
       return baseDao.selectQueryResult(msgQueue, msgQueue.getPageIndex(), msgQueue.getPageSize());
      }

      @Override
      public List<MsgQueuePo> selectList(MsgQueuePo msgQueue) {
       return baseDao.selectList(msgQueue);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(MsgQueuePo msgQueue) {
       return baseDao.insert(msgQueue);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(MsgQueuePo msgQueue) {
       return baseDao.update(msgQueue);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(MsgQueuePo set,MsgQueuePo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(MsgQueuePo msgQueue) {
       return baseDao.delete(msgQueue);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<MsgQueuePo> list) {
       return baseDao.batchInsert(MsgQueuePo.class, list);
      }

      @Override
      public int countMsgQueue(){
            return baseDao.count(MsgQueuePo.builder().build());
      }


      @Override
      public boolean generateBlockMsg(EthBlock.Block syncBlock) {
            Web3j web3j = fullNodeCache.getWeb3j();
            SegBlock.Block segBlock = JSON.parseObject(JSON.toJSONString(syncBlock),SegBlock.Block.class);
            for (TransactionInfo transactionInfo : segBlock.getTransactions()){
                  try {
                        EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transactionInfo.getHash()).send();
                        transactionInfo.setReceipts(JSON.parseObject(
                                JSON.toJSONString(ethGetTransactionReceipt.getTransactionReceipt()),
                                TransactionReceipt.class));
                  } catch (IOException e) {
                        log.info("query eth receipt error:{}",e.getMessage(),e);
                  }
            }
            MsgQueuePo msgQueuePo = MsgQueuePo.builder()
                    //保存json串
                    .msg(JSON.toJSONString(segBlock))
                    .msgType(MsgTypeEnum.BLOCK_MSG.getCode())
                    .isCallback(false)
                    .createTime(new Date())
                    //fixme bigint -> long溢出风险？
                    .height(syncBlock.getNumber().longValueExact())
                    .build();
            BlockCachePo blockCachePo = BlockCachePo.builder()
                    .blockHash(syncBlock.getHash())
                    .blockTime(new Date(syncBlock.getTimestamp().longValueExact()))
                    .height(syncBlock.getNumber().longValueExact())
                    .createTime(new Date())
                    .build();
            return saveBlockInfo(msgQueuePo,blockCachePo);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean saveBlockInfo(MsgQueuePo msgQueuePo,BlockCachePo blockCachePo){
            SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
            syncHeightPo.setSyncHeight(blockCachePo.getHeight());
            syncHeightPo.setBlockHash(blockCachePo.getBlockHash());
            syncHeightPo.setUpdateTime(new Date());
            boolean isUpdateSyncHeight = syncHeightService.update(syncHeightPo) == 1;
            Integer cachedCount = baseDao.count(BlockCachePo.builder()
                    .blockHash(blockCachePo.getBlockHash())
                    .build());
            boolean isInsertBlockCache = true;
            if (cachedCount <= 0){
                  //不存在该hash则插入
                  isInsertBlockCache = blockCacheService.insert(blockCachePo) == 1;
            }
            boolean isInsertMsg = msgQueueService.insert(msgQueuePo) == 1;
            if (isInsertBlockCache && isInsertMsg && isUpdateSyncHeight){
                  return true;
            }else {
                  throw new CodedBizException(ResponseEnum.SYS_ERROR,"saveBlockInfo exception");
            }
      }



      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean generateForkingMsg(EthBlock.Block forkingBlock){

            //默认只有一条记录
            SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
            boolean isUpdateSyncHeight = false;
            boolean isDeleteBlockCache = false;
            boolean isInsertMsg = false;
            if (forkingBlock.getNumber().longValueExact() <= syncHeightPo.getSyncHeight()){
                  log.warn("forking from {} to {}",syncHeightPo.getSyncHeight(),forkingBlock.getNumber());
                  Long preSyncHeight = syncHeightPo.getSyncHeight();
                  //更新高度同步记录
                    //fixme bigint->long 溢出风险
                  syncHeightPo.setSyncHeight(forkingBlock.getNumber().longValueExact() > 1? forkingBlock.getNumber().longValueExact()-1 : 0);
                  syncHeightPo.setBlockHash(forkingBlock.getHash());
                  syncHeightPo.setUpdateTime(new Date());
                  isUpdateSyncHeight = syncHeightService.update(syncHeightPo) == 1;
                  if (!isUpdateSyncHeight){
                        throw new CodedBizException(ResponseEnum.SYS_ERROR," isUpdateSyncHeight generateForkingMsg exception");
                  }
                  //删除blockCache中缓存记录(fork_height之后的)
                  int count = 0;
                  if (forkingBlock.getNumber().longValueExact() > preSyncHeight){
                        log.error("forkingBlock number:{} larger than preSyncHeight:{}!",forkingBlock.getNumber().longValueExact(),preSyncHeight);
                        return false;
                  }
                  for (long i = forkingBlock.getNumber().longValueExact() ; i <= preSyncHeight ; i ++){
                        count += blockCacheService.delete(BlockCachePo.builder()
                        .height(i)
                        .build());
                  }
                  isDeleteBlockCache = count >= 0;
                  if (!isDeleteBlockCache){

                        throw new CodedBizException(ResponseEnum.SYS_ERROR," isDeleteBlockCache generateForkingMsg exception");
                  }
            }
            //生成分叉消息 msg内容为fork_height
            MsgQueuePo msgQueuePo = MsgQueuePo.builder()
                    .isCallback(false)
                    .msgType(MsgTypeEnum.FORKING_MSG.getCode())
                    .msg(JSON.toJSONString(forkingBlock))
                    .createTime(new Date())
                    //分叉高度为公共点之后的高度
//                    .msg(String.valueOf(forkingBlock.getNumber()))
                    .height(forkingBlock.getNumber().longValueExact())
                    .build();
            isInsertMsg = msgQueueService.insert(msgQueuePo) == 1;
            if (!isInsertMsg){
                  throw new CodedBizException(ResponseEnum.SYS_ERROR," isInsertMsg generateForkingMsg exception");

            }
            if (isUpdateSyncHeight && isDeleteBlockCache && isInsertMsg){
               return true;
            }else {
                  throw new CodedBizException(ResponseEnum.SYS_ERROR,"generateForkingMsg exception");
            }
      }

      @Override
      public List<MsgQueuePo> getMsgForCallBack(){
            MsgQueuePo msgQueuePo  = MsgQueuePo.builder()
                    .isCallback(false)
                    .orderBy(Lists.newArrayList(Order.asc("createTime")))
                    .pageSize(1)
                    .pageIndex(1)
                    .build();
            return this.selectLimit(msgQueuePo).getRows();
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean updateCallBackInfo(MsgQueuePo updatedMsg){
            updatedMsg.setIsCallback(true);
            updatedMsg.setUpdateTime(new Date());
            updatedMsg.setLastCallbackTime(new Date());
            boolean isUpdateMsg = msgQueueService.update(updatedMsg) == 1;
            BlockHeightPo blockHeightPo = blockHeightService.selectOne(BlockHeightPo.builder().build());
            blockHeightPo.setLastCallBackHeight(updatedMsg.getHeight());
            blockHeightPo.setLastCallBackTime(new Date());
            boolean isUpdateBlockHeight = blockHeightService.update(blockHeightPo) == 1;
            if(isUpdateBlockHeight && isUpdateMsg){
                  return true;
            }else{
                  throw new CodedBizException(ResponseEnum.SYS_ERROR,"updateCallBackInfo failed");
            }
      }


}
