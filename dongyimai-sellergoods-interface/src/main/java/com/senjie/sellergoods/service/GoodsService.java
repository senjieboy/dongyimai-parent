package com.senjie.sellergoods.service;

import com.senjie.entity.Goods;
import com.senjie.entity.PageResult;
import com.senjie.pojo.TbGoods;
import com.senjie.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * InnoDB free: 5120 kB服务层接口
 *
 * @author Administrator
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll ();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage (int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add (Goods goods);


    /**
     * 修改
     */
    public void update (Goods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    Goods findOne (Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    void delete (Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
   public PageResult findPage (TbGoods goods, int pageNum, int pageSize);

    /**
     * 根据集合修改状态
     * @param ids
     * @param status
     */

   public void  updateStatus(Long[] ids,String status);

    /**
     * 根据商品ID查询商品信息表
     * @param goodsId
     * @param status
     * @return
     */
   public List<TbItem> findItems(Long[] goodsId,String status);


}
