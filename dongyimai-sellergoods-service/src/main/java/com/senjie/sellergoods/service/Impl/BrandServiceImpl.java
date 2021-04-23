package com.senjie.sellergoods.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.senjie.entity.PageResult;
import com.senjie.mapper.TbBrandMapper;
import com.senjie.pojo.TbBrand;
import com.senjie.pojo.TbBrandExample;
import com.senjie.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/3/30 16:08
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;


    @Override
    public List<TbBrand> findAllBrands () {
        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage (int pagNum, int pagSize) {

        PageHelper.startPage(pagNum, pagSize);

        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * The brand add service
     *
     * @param tbBrand
     */
    @Override
    public void add (TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    @Override
    public TbBrand findOne (Long id) {

        return tbBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update (TbBrand brand) {
        tbBrandMapper.updateByPrimaryKey(brand);

    }

    @Override
    public void delete (Long[] ids) {
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }

    }

    @Override
    public PageResult findPage (TbBrand brand, int pageNum, int pagSize) {
        PageHelper.startPage(pageNum, pagSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (brand != null) {
            if (brand.getName() != null && brand.getName().length() > 0) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }

            if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }

        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Map> findBrandList () {
        return tbBrandMapper.findBrandList();
    }
}


