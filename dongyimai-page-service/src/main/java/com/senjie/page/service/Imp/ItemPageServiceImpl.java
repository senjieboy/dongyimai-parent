package com.senjie.page.service.Imp;

import com.senjie.mapper.TbGoodsDescMapper;
import com.senjie.mapper.TbGoodsMapper;
import com.senjie.mapper.TbItemCatMapper;
import com.senjie.mapper.TbItemMapper;
import com.senjie.page.service.ItemPageService;
import com.senjie.pojo.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;

import java.io.Writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/4/18 20:35
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pageDir}")
    private String pageDir;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean genItemHtml (Long goodId) {

        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Map map = new HashMap<>();
            //load item list
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodId);
            map.put("goods", goods);
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodId);
            map.put("goodsDesc", goodsDesc);
            //read
            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            map.put("itemCat1", itemCat1);
            map.put("itemCat2", itemCat2);
            map.put("itemCat3", itemCat3);
            //suk data
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");//状态有效
            criteria.andGoodsIdEqualTo(goodId);
            example.setOrderByClause("is_default desc");
            List<TbItem> itemsList = itemMapper.selectByExample(example);
            map.put("itemsList", itemsList);
            Writer writer = new FileWriter(pageDir + goodId + ".html");
            template.process(map, writer);
            writer.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map dataModel = new HashMap<>();

        return false;
    }

    @Override
    public boolean deleteItemHtml (Long[] goodIds) {
        try {
            for (Long goodId : goodIds) {
                new File(pageDir + goodId + ".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
