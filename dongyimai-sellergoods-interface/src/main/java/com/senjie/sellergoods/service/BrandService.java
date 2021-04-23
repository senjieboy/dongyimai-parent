package com.senjie.sellergoods.service;

import com.senjie.entity.PageResult;
import com.senjie.pojo.TbBrand;

import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/3/30 16:04
 */
public interface BrandService {

    /**
     * View all brands
     **/
    public List<TbBrand> findAllBrands();

    /**
     * @param pagNum  current page
     * @param pagSize page size
     * @return
     */
    public PageResult findPage(int pagNum, int pagSize);

    /**
     * The brand add
     *
     * @param tbBrand
     */

    public void add(TbBrand tbBrand);

    /**
     *   Query by id
     * @param id
     * @return
     */


    public TbBrand findOne(Long id);

    /**
     * Update
     * @param brand
     */


    public  void  update(TbBrand brand);

    /**
     * Delete by ids
     * @param ids
     */

    public  void delete(Long [] ids);

    /**
     *  fuzzy Query
     * @param brand
     * @param pageNum
     * @param pagSize
     * @return
     */

    public  PageResult  findPage(TbBrand brand,int pageNum, int pagSize);


    public List<Map>  findBrandList();

}
