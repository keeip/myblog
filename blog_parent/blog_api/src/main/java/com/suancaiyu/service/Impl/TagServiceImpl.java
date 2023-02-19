package com.suancaiyu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suancaiyu.mapper.TagMapper;
import com.suancaiyu.pojo.Tag;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.TagService;
import com.suancaiyu.vo.TagVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;
    @Override
    public List<TagVo> FindByArticleId(Long id) {
        List<Tag> tagList= tagMapper.FindByArticleId(id);
        return copylist(tagList);
    }

    @Override
    public Result getHotTag(int limt) {

       List<Long> tagIdList= tagMapper.FindHotTagId(limt);//获取最热标签id
        if (CollectionUtils.isEmpty(tagIdList)){
            return Result.success(Collections.emptyList());
        }
       List<Tag> hottagList=tagMapper.FindById(tagIdList);
        return Result.success(hottagList);

    }

    @Override
    public Result getTagVoList() {
        List<Tag> tagList=tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copylist(tagList));
    }

    @Override
    public Result getTagList() {
        return Result.success(tagMapper.selectList(new LambdaQueryWrapper<>()));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(tag);
    }

    private List<TagVo> copylist(List<Tag> tagList) {
        List<TagVo> tagVoList =new ArrayList<>();
        for (Tag tag :tagList){
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
    private TagVo copy(Tag tag){
        TagVo tagVo=new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
