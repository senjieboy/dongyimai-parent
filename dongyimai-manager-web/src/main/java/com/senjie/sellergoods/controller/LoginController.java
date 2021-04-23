package com.senjie.sellergoods.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/4/3 21:13
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/showName")
    public Map showName () {
        Map map = new HashMap<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("loginName", name);
        return map;
    }
}
