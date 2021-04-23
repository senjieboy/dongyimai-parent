package com.senjie.search.service;

import com.senjie.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/4/14 19:10
 */
public interface ItemSearchService {

    /**
     * 搜索方法
     *
     * @param searchMap
     * @return
     */
    public Map<String, Object> search (Map searchMap);

    /**
     * 导入数据
     *
     * @param list
     */

    public void importData (List<TbItem> list);

    /**
     *  删除索引库
     * @param goodsId
     */
    public void deleteByGoodsId (List goodsId);
}
