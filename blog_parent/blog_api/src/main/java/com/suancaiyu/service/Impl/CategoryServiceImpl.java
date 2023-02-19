package com.suancaiyu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suancaiyu.mapper.CategoryMapper;
import com.suancaiyu.pojo.Category;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.CategoryService;
import com.suancaiyu.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public Result getCategoryVoList() {
        List<Category> categoryList=categoryMapper.selectList(new LambdaQueryWrapper<>());

         return Result.success(copyList(categoryList));
    }

    @Override
    public Result getCategoryList() {
        return Result.success(categoryMapper.selectList(new LambdaQueryWrapper<>())) ;
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(category);
    }

    private List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVoList=new ArrayList<>();
        for (Category category: categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo=new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
