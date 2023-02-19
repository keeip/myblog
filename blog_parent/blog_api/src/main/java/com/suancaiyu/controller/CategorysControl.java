package com.suancaiyu.controller;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/categorys")
public class CategorysControl {
    @Resource
    private CategoryService categoryService;
    @GetMapping
    public Result getCategoryListInView(){
        return categoryService.getCategoryVoList();
    }
    @GetMapping("/detail")
    public Result getCategoryList(){
        return categoryService.getCategoryList();

    }
    @GetMapping("detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id){
        return categoryService.categoriesDetailById(id);
    }
}
