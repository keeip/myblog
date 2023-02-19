package com.suancaiyu.controller;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tags")
public class TagControl {
    @Resource
    private TagService tagService;

    /**
     * 获取最热标签
     * @return
     */
    @GetMapping("/hot")
    public Result hotTagList(){
        int limt=6;
       return tagService.getHotTag(limt);
    }
    @GetMapping
    public Result getTagsListInView(){
        return tagService.getTagVoList();
    }
    @GetMapping("/detail")
    public Result getTagList(){
        return tagService.getTagList();
    }
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }
}
