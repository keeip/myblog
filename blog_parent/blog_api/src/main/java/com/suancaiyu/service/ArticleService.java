package com.suancaiyu.service;

import com.suancaiyu.pojo.Archives;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.vo.ArticleParam;
import com.suancaiyu.vo.ArticleVo;
import com.suancaiyu.vo.PageParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    public Result articleList(PageParams pageParams);

    /**
     * 获取最热文章列表
     * @param limit
     * @return
     */
    public List<ArticleVo> getHotArticleList(int limit);

    /**
     * 获取最新文章列表
     * @param limit
     * @return
     */
    public List<ArticleVo> getNewArticleList(int limit);

    /**
     * 获取文章归档list列表
     * @return
     */
     public List<Archives> getListArchives();

    /**
     * 根据文章id获取详情
     * @param id
     * @return
     */
    public ArticleVo FindById(Long id);

    public Result save(ArticleParam articleParam);
}
