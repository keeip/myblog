package com.suancaiyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suancaiyu.pojo.Archives;
import com.suancaiyu.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 获取文章归档列表
     * @return
     */
   public List<Archives> getListArchives();
   public IPage<Article> getListArticle(Page page,Long categoryId,Long TagId,String year,String month);
}
