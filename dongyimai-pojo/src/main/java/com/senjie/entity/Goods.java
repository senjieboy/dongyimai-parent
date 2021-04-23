package com.senjie.entity;

import com.senjie.pojo.TbGoods;
import com.senjie.pojo.TbGoodsDesc;
import com.senjie.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Author SenJie
 * @Data 2021/4/6 13:30
 */

/**
 * 商品组合实体类
 * spu
 * sup des
 * sku
 * @author Senjie*/
public class Goods  implements Serializable {
    private TbGoods goods;
    private TbGoodsDesc goodsDesc;
    private List<TbItem> itemList;

    public Goods () {
    }

    public Goods (TbGoods goods, TbGoodsDesc goodsDesc, List<TbItem> itemList) {
        this.goods = goods;
        this.goodsDesc = goodsDesc;
        this.itemList = itemList;
    }

    public TbGoods getGoods () {
        return goods;
    }

    public void setGoods (TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc () {
        return goodsDesc;
    }

    public void setGoodsDesc (TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList () {
        return itemList;
    }

    public void setItemList (List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
