package com.senjie.sellergoods.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.qos.command.impl.Ls;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.senjie.entity.PageResult;
import com.senjie.mapper.TbSpecificationOptionMapper;
import com.senjie.pojo.*;
import com.senjie.pojo.TbTypeTemplateExample.Criteria;
import com.senjie.mapper.TbTypeTemplateMapper;
import com.senjie.sellergoods.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * InnoDB free: 5120 kB服务实现层
 *
 * @author Administrator
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbTypeTemplate> findAll () {
        return typeTemplateMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage (int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add (TbTypeTemplate typeTemplate) {
        typeTemplateMapper.insert(typeTemplate);
    }


    /**
     * 修改
     */
    @Override
    public void update (TbTypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKey(typeTemplate);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate findOne (Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete (Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage (TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbTypeTemplateExample example = new TbTypeTemplateExample();
        Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
                criteria.andNameLike("%" + typeTemplate.getName() + "%");
            }
            if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
                criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
            }
            if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
                criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
            }
            if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
                criteria.andCustomAttributeItemsLike("%" + typeTemplate.getCustomAttributeItems() + "%");
            }
        }

        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(example);
        this.saveRedis();
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Map> selectOptionList () {
        return typeTemplateMapper.selectOptionList();
    }

    @Override
    public List<Map> findSpecList (Long id) {
        //Query the template object based on the template ID
        TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        //从模板id取出规格
        List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
        if (list != null) {
            for (Map map : list) {
                Long specId = new Long((Integer) map.get("id"));
                TbSpecificationOptionExample example = new TbSpecificationOptionExample();
                TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                List<TbSpecificationOption> optionList = specificationOptionMapper.selectByExample(example);
                map.put("option", optionList);
            }

        }
        return list;
    }

    private void saveRedis () {
        List<TbTypeTemplate> all = this.findAll();
        for (TbTypeTemplate typeTemplate : all) {
            List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), brandList);
            List<Map> specList = this.findSpecList(typeTemplate.getId());
            redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), specList);
        }

    }
}
