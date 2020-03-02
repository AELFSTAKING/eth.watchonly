
package io.seg.kofo.ethwo.biz.service;

import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import io.seg.framework.dao.model.QueryResult;

import java.util.List;

public interface BlockHeightService {

     /**
      * 根据唯一索引查询
      * @param btcBlockHeight
      * @return
      */
     public BlockHeightPo selectOne(BlockHeightPo btcBlockHeight);

     /**
      * 分页查询
      * @param btcBlockHeight
      * @return
      */
     public QueryResult<BlockHeightPo> selectLimit(BlockHeightPo btcBlockHeight);

     /**
      * 列表查询不分页
      * @param btcBlockHeight
      * @return
      */
     public List<BlockHeightPo> selectList(BlockHeightPo btcBlockHeight);

     /**
      * 新增
      * @param btcBlockHeight
      * @return
      */
     public int insert(BlockHeightPo btcBlockHeight);

     /**
      * 更新
      * @param btcBlockHeight
      * @return
      */
     public int update(BlockHeightPo btcBlockHeight);

     /**
      * 更新
      * @param set
      * @param where
      * @return
      */
     public int update(BlockHeightPo set, BlockHeightPo where);

     /**
      * 删除
      * @param btcBlockHeight
      * @return
      */
     public int delete(BlockHeightPo btcBlockHeight);

     /**
      * 批量插入
      * @param list
      * @return
      */
     public boolean batchInsert(List<BlockHeightPo> list);

}
