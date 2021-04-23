package com.senjie.sellergoods.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.senjie.entity.Goods;
import com.senjie.entity.PageResult;
import com.senjie.entity.Result;
import com.senjie.pojo.TbGoods;
import com.senjie.pojo.TbItem;

import com.senjie.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

import java.util.List;

/**
 * InnoDB free: 5120
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 3000)
    private GoodsService goodsService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queueSolrDestination;
    @Autowired
    private Destination queueDeleteSolrDestination;
    @Autowired
    private Destination queuePageDestination;
    @Autowired
    private Destination queueDeletePageDestination;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll () {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage (int page, int rows) {
        return goodsService.findPage(page, rows);
    }


    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update (@RequestBody Goods goods) {
        //当前操作是否为当前商家
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Goods one = goodsService.findOne(goods.getGoods().getId());
        if (!one.getGoods().getSellerId().equals(sellerId) || !goods.getGoods().getSellerId().equals(sellerId)) {
            return new Result(true, "不是当前商家");
        }

        try {

            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne (Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete (Long[] ids) {
        try {
            goodsService.delete(ids);
            jmsTemplate.send(queueDeleteSolrDestination, session -> session.createObjectMessage(ids));
            //Delete static pages
            jmsTemplate.send(queueDeletePageDestination, session -> session.createObjectMessage(ids));
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search (@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus (Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            if ("1".equals(status)) {
                List<TbItem> items = goodsService.findItems(ids, status);
                   //import data solr
                    jmsTemplate.send(queueSolrDestination, session -> session.createTextMessage(JSON.toJSONString(items)));
                System.out.println("import data into solr");
                //导入静态页面生成服务
                for (Long id : ids) {
                    jmsTemplate.send(queuePageDestination, session -> session.createTextMessage(id + ""));
                }
            } else {
                System.out.println("NO data to import");
            }
            return new Result(true, "审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "审核失败");
        }
    }

}
