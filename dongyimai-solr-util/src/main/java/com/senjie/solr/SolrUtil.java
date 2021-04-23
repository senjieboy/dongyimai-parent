package com.senjie.solr;

import com.alibaba.fastjson.JSON;

import com.github.promeg.pinyinhelper.Pinyin;
import com.senjie.mapper.TbItemMapper;
import com.senjie.pojo.TbItem;
import com.senjie.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/4/14 14:36
 */
@Component
public class SolrUtil {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    public void importData () {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);
        System.out.println("---------开始---------------");
        for (TbItem item : itemList) {
            System.out.println(item.getTitle());
            Map<String, String> pinYin = new HashMap<String, String>();

            Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
            for (String key : map.keySet()) {
                pinYin.put(Pinyin.toPinyin(key, "").toLowerCase(), map.get(key));

            }
            item.setSpecMap(pinYin);

        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("---------结束---------------");
    }

    public void dele () {
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();

    }

    public static void main (String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");


        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
       solrUtil.importData();
        //solrUtil.dele();

    }
}
