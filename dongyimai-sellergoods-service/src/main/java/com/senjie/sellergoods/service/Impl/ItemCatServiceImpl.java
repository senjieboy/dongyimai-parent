package com.senjie.sellergoods.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.senjie.entity.PageResult;
import com.senjie.pojo.TbItemCatExample;
import com.senjie.pojo.TbItemCatExample.Criteria;
import com.senjie.mapper.TbItemCatMapper;
import com.senjie.pojo.TbItemCat;
import com.senjie.sellergoods.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 商品类目服务实现层
 *
 * @author Administrator
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll () {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage (int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add (TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update (TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne (Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete (Long[] ids) {
        for (Long id : ids) {
            itemCatMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public PageResult findByParentId (Long parentId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        List<TbItemCat> catList = this.findAll();
        for (TbItemCat itemCat : catList) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
        }

        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public List<TbItemCat> findByParentId (Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        return itemCatMapper.selectByExample(example);
    }
}
