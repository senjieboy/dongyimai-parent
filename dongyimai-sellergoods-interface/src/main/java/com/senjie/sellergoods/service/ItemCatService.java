package com.senjie.sellergoods.service;

import com.senjie.entity.PageResult;
import com.senjie.pojo.TbItemCat;

import java.util.List;

/**
 * 商品类目服务层接口
 *
 * @author Administrator
 */
public interface ItemCatService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbItemCat> findAll ();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage (int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add (TbItemCat item_cat);


    /**
     * 修改
     */
    public void update (TbItemCat item_cat);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbItemCat findOne (Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete (Long[] ids);



    /**
     * 根据父ID查询
     * @return
     */
    public PageResult findByParentId (Long parentId,Integer pageNum, Integer pageSize);
    /**
     * 根据父ID查询
     * @return
     */
    public List<TbItemCat> findByParentId (Long parentId);

}
