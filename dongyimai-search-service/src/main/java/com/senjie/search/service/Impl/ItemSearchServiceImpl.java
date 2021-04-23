package com.senjie.search.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.promeg.pinyinhelper.Pinyin;
import com.senjie.pojo.TbItem;
import com.senjie.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author SenJie
 * @Data 2021/4/14 19:10
 */
@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, Object> search (Map searchMap) {
        Map<String, Object> map = new HashMap<>();
        //1.高亮显示
        map.putAll(this.searchList(searchMap));
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);

        //差选品牌和规格数据
        String categoryName = (String) (String) searchMap.get("category");
        if (!"".equals(categoryName)) {
            map.putAll(this.searchBrandAndSpec(categoryName));
        } else {
            if (categoryList.size() > 0) {
                Map brandAndSpec = this.searchBrandAndSpec(categoryList.get(0));
                map.putAll(brandAndSpec);
            }
        }
        return map;

    }

    @Override
    public void importData (List<TbItem> list) {
        for (TbItem item : list) {
            Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
            Map pinYin = new HashMap();
            for (String key : map.keySet()) {
                pinYin.put("item_spec_" + Pinyin.toPinyin(key, "").toLowerCase(), map.get(key));
            }
            item.setSpecMap(pinYin);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();

    }

    @Override
    public void deleteByGoodsId (List goodsId) {
        Query query = new SimpleQuery();
        Criteria goodsid = new Criteria("item_goodsid").in(goodsId);
        query.addCriteria(goodsid);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


    /**
     * 根据关键字查询商品分类
     *
     * @param searchMap
     * @return
     */


    private List<String> searchCategoryList (Map searchMap) {
        List<String> list = new ArrayList<>();
        //查询所有结果集
        SimpleQuery query = new SimpleQuery();
        //查询字段
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        //要分组的字段
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        query.addCriteria(criteria);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> itemCategory = page.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = itemCategory.getGroupEntries();
        List<GroupEntry<TbItem>> groupEntryList = groupEntries.getContent();
        for (GroupEntry<TbItem> itemGroupEntry : groupEntryList) {

            list.add(itemGroupEntry.getGroupValue());
        }

        return list;

    }

    private Map searchList (Map searchMap) {

        Map<String, Object> map = new HashMap<>();
        //1、创建一个支持高亮查询器对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        //2、设定需要高亮处理字段
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        //3、设置高亮前缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //4、设置高亮后缀
        highlightOptions.setSimplePostfix("</em>");
        //5、关联高亮选项到高亮查询器对象
        query.setHighlightOptions(highlightOptions);
        //6、设定查询条件 根据关键字查询
        //创建查询条件对象
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        //关联查询条件到查询器对象
        query.addCriteria(criteria);
        //按照分类进行过滤筛选
        if (!"".equals(searchMap.get("category"))) {
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //按照品牌过滤筛选
        if (!"".equals(searchMap.get("brand"))) {
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //按照价格过滤筛选
        if (!"".equals(searchMap.get("price"))) {
            String[] prices = ((String) searchMap.get("price")).split("-");

            if (!prices[0].equals("0")) {
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

            if (!prices[1].equals("*")) {
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //按照品牌过滤筛选
        if (searchMap.get("spec") != null) {
            Map<String, String> specMap = (Map) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_" + Pinyin.toPinyin(key, "").toLowerCase()).is(specMap.get(key));
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo == null) {
            pageNo = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }
        //当前第几页
        query.setOffset((pageNo - 1) * pageSize);
        //每页多少条
        query.setRows(pageSize);

        //排序状态ASC DESC
        String sortValue = (String) searchMap.get("sort");
        //排序的字段
        String sortField = (String) searchMap.get("sortField");
        if (null != sortValue && !"".equals(sortValue)) {
            if ("ASC".equals(sortValue)) {
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);

            }
            if ("DESC".equals(sortValue)) {
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
            }
        }

        //7、发出带高亮数据查询请求
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //8、获取高亮集合入口
        List<HighlightEntry<TbItem>> highlightEntryList = page.getHighlighted();
        //9、遍历高亮集合
        for (HighlightEntry<TbItem> highlightEntry : highlightEntryList) {
            //获取基本数据对象
            TbItem tbItem = highlightEntry.getEntity();
            if (highlightEntry.getHighlights().size() > 0 && highlightEntry.getHighlights().get(0).getSnipplets().size() > 0) {
                List<HighlightEntry.Highlight> highlightList = highlightEntry.getHighlights();
                //高亮结果集合
                List<String> snippets = highlightList.get(0).getSnipplets();
                //获取第一个高亮字段对应的高亮结果，设置到商品标题
                tbItem.setTitle(snippets.get(0));
            }

        }

        //把带高亮数据集合存放map
        map.put("rows", page.getContent());
        //总页数
        map.put("totalPages", page.getTotalPages());
        //总记录数
        map.put("totals", page.getTotalElements());
        return map;
    }

    public Map searchBrandAndSpec (String category) {
        Map map = new HashMap();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (typeId != null) {
            //根据模板id查询品牌数据
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList", brandList);
            //根据模板id查询规格
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }
        return map;
    }
}
