

package io.seg.kofo.ethwo.biz.service.impl;

import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import io.seg.framework.dao.BaseDao;
import io.seg.framework.dao.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Slf4j
@Service
public class BlockHeightServiceImpl implements BlockHeightService {
      @Autowired
      private BaseDao baseDao;

      @Override
      public BlockHeightPo selectOne(BlockHeightPo btcBlockHeight) {
       return baseDao.selectOne(btcBlockHeight);
      }

      @Override
      public QueryResult<BlockHeightPo> selectLimit(BlockHeightPo btcBlockHeight) {
       return baseDao.selectQueryResult(btcBlockHeight, btcBlockHeight.getPageIndex(), btcBlockHeight.getPageSize());
      }

      @Override
      public List<BlockHeightPo> selectList(BlockHeightPo btcBlockHeight) {
       return baseDao.selectList(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(BlockHeightPo btcBlockHeight) {
       return baseDao.insert(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BlockHeightPo btcBlockHeight) {
       return baseDao.update(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BlockHeightPo set, BlockHeightPo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(BlockHeightPo btcBlockHeight) {
       return baseDao.delete(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<BlockHeightPo> list) {
       return baseDao.batchInsert(BlockHeightPo.class, list);
      }

}
