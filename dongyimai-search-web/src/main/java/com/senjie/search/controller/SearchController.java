package com.senjie.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.senjie.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
/**
 * @Author SenJie
 * @Data 2021/4/14 19:42
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Reference(timeout = 3000)
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map searchMap){

        return  itemSearchService.search(searchMap);
    }

}
