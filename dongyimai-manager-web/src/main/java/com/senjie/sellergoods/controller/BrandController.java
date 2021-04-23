package com.senjie.sellergoods.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.senjie.entity.PageResult;
import com.senjie.entity.Result;
import com.senjie.pojo.TbBrand;
import com.senjie.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/3/30 16:13
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference(timeout = 30000)
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAllBrands () {
        return brandService.findAllBrands();
    }

    @RequestMapping("/findPage")
    public PageResult findPage (int page, int rows) {
        return brandService.findPage(page, rows);
    }

    @RequestMapping("/add")
    public Result add (@RequestBody TbBrand brand) {
        brandService.add(brand);

        try {
            return new Result(true, "  successfully added");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "   failure added");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne (Long id) {
        return brandService.findOne(id);
    }


    @RequestMapping("/update")
    public Result update (@RequestBody TbBrand brand) {
        brandService.update(brand);
        try {
            return new Result(true, "  successfully update");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "   failure update");
        }

    }

    @RequestMapping("/delete")
    public Result delete (Long[] id) {
        brandService.delete(id);
        try {
            return new Result(true, "  successfully delete");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "   failure delete");
        }
    }

    @RequestMapping("/search")
    public PageResult search (@RequestBody TbBrand brand, int page, int rows) {
        return brandService.findPage(brand, page, rows);

    }

    @RequestMapping("/findBrandList")
    public List<Map> findBrandList () {

        return brandService.findBrandList();
    }
}
