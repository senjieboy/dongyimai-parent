package com.senjie.sellergoods.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.senjie.entity.PageResult;
import com.senjie.entity.Specification;
import com.senjie.mapper.TbSpecificationOptionMapper;
import com.senjie.pojo.TbSpecificationExample;
import com.senjie.pojo.TbSpecificationExample.Criteria;
import com.senjie.mapper.TbSpecificationMapper;
import com.senjie.pojo.TbSpecification;
import com.senjie.pojo.TbSpecificationOption;
import com.senjie.pojo.TbSpecificationOptionExample;
import com.senjie.sellergoods.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * InnoDB free: 5120 kB服务实现层
 *
 * @author Administrator
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll () {
        return specificationMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage (int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add (Specification specification) {
        specificationMapper.insert(specification.getSpecification());
        //循环插入规格选项
        for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()) {
            specificationOption.setSpecId(specification.getSpecification().getId());
            specificationOptionMapper.insert(specificationOption);
        }
    }


    /**
     * 修改
     */
    @Override
    public void update (Specification specification) {
        specificationMapper.updateByPrimaryKey(specification.getSpecification());

        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(specification.getSpecification().getId());
        //先删除在修改
        specificationOptionMapper.deleteByExample(example);
        //循环插入到规格选项
        for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()) {
            specificationOption.setSpecId(specification.getSpecification().getId());
            specificationOptionMapper.insert(specificationOption);
        }

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Specification findOne (Long id) {
        TbSpecification specification = specificationMapper.selectByPrimaryKey(id);
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();

        criteria.andSpecIdEqualTo(specification.getId());
        List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);
        Specification specification1 = new Specification();
        specification1.setSpecification(specification);
        specification1.setSpecificationOptionList(specificationOptionList);
        return specification1;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete (Long[] ids) {
        for (Long id : ids) {
            specificationMapper.deleteByPrimaryKey(id);
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);

        }
    }


    @Override
    public PageResult findPage (TbSpecification specification, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSpecificationExample example = new TbSpecificationExample();
        Criteria criteria = example.createCriteria();

        if (specification != null) {
            if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
                criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
            }
        }

        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Map> findSpecList () {
        return specificationMapper.findSpecList();
    }
}
