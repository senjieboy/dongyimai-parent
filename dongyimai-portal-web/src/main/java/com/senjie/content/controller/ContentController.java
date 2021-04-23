package com.senjie.content.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.senjie.content.service.ContentService;

import com.senjie.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;

	@RequestMapping("findContentById")
	public List<TbContent> findContentById(Long categoryId){
		return  contentService.findContentById(categoryId);
	}
}
