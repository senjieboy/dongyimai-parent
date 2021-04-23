package com.senjie.sellergoods.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.senjie.entity.Goods;
import com.senjie.entity.PageResult;
import com.senjie.mapper.*;
import com.senjie.pojo.*;
import com.senjie.pojo.TbGoodsExample.Criteria;
import com.senjie.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * InnoDB free: 5120 kB服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbSellerMapper sellerMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<TbGoods> findAll () {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage (int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add (Goods goods) {
        goods.getGoods().setAuditStatus("0");
        //插入商品表
        goodsMapper.insert(goods.getGoods());
        //增加2
        TbGoodsDesc goodsDesc = goods.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getGoods().getId());
        //插入商品扩展表
        goodsDescMapper.insert(goods.getGoodsDesc());
        this.saveItemList(goods);

    }

    /**
     * 修改
     */
    @Override
    public void update (Goods goods) {
        //设置商品未审核状态
        goods.getGoods().setAuditStatus("0");
        goodsMapper.updateByPrimaryKey(goods.getGoods());
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
        //删除原来的sku列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);
        //添加新的sku列表
        this.saveItemList(goods);

    }

    private void saveItemList (Goods goods) {
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            //sku information
            for (TbItem item : goods.getItemList()) {
                // get title
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> map = JSON.parseObject(item.getSpec(), Map.class);
                for (String key : map.keySet()) {
                    title += "" + map.get(key);
                }
                item.setTitle(title);

                this.setItemValue(goods, item);
            }
        } else {
            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());
            item.setPrice(goods.getGoods().getPrice());
            item.setStatus("1");
            item.setIsDefault("1");
            item.setNum(9999);
            item.setSpec("{}");
            this.setItemValue(goods, item);
            itemMapper.insert(item);


        }
    }

    private void setItemValue (Goods goods, TbItem item) {
        //商品的spuid
        item.setGoodsId(goods.getGoods().getId());
        //商家编号
        item.setSeller(goods.getGoods().getSellerId());
        //分类
        item.setCategoryid(goods.getGoods().getCategory3Id());
        //create time
        item.setCreateTime(new Date());
        //update time
        item.setUpdateTime(new Date());
        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());


        //分类名称
        TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //商家名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(seller.getName());
        //商品图片
        List<Map> list = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (list.size() > 0) {
            item.setImage((String) list.get(0).get("url"));
        }
        itemMapper.insert(item);

    }


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne (Long id) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);

        //查询sku列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> items = itemMapper.selectByExample(example);
        goods.setItemList(items);
        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete (Long[] ids) {
        for (Long id : ids) {
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(goods);
        }
        //修改删除状态
        List<TbItem> items = this.findItems(ids, "1");
        for (TbItem item : items) {
            item.setStatus("0");
            itemMapper.updateByPrimaryKey(item);
        }
    }


    @Override
    public PageResult findPage (TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();
        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus (Long[] ids, String status) {
        for (Long id : ids) {
            //goods查询
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(goods);
            //修改状态
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(id);
            List<TbItem> items = itemMapper.selectByExample(example);
            for (TbItem item : items) {
                item.setStatus(status);
                itemMapper.updateByPrimaryKey(item);
            }
        }

    }

    @Override
    public List<TbItem> findItems (Long[] goodsId, String status) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdIn(Arrays.asList(goodsId));
        criteria.andStatusEqualTo(status);
        return itemMapper.selectByExample(example);
    }
}
