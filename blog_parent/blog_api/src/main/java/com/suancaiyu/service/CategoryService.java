package com.suancaiyu.service;

import com.suancaiyu.resultcommon.Result;

public interface CategoryService {

   public Result getCategoryVoList();

   public Result getCategoryList();

   public Result categoriesDetailById(Long id);
}
