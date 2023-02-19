package com.suancaiyu.controller;

import com.suancaiyu.cache.Cache;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.ArticleService;
import com.suancaiyu.service.Impl.ArticleServiceImpl;
import com.suancaiyu.utils.UserThreadLocal;
import com.suancaiyu.vo.ArticleParam;
import com.suancaiyu.vo.ArticleVo;
import com.suancaiyu.vo.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleControl {
    /**
     * 文章列表控制器
     * @param pageParams
     * @return
     */
    @Autowired
    private ArticleService articleService;
    @PostMapping

    public Result articleList(@RequestBody PageParams pageParams){
       return articleService.articleList(pageParams);
    }

    /**
     * 获取最热文章
     * @return
     */
    @PostMapping("/hot")

    public Result hotArticleList(){
        int limit=5;
        return Result.success(articleService.getHotArticleList(limit));

    }

    /**
     * 获取最新文章
     * @return
     */
    @PostMapping("/new")

    public Result newArticleList(){
        int limit=5;
        return Result.success(articleService.getNewArticleList(limit));
    }

    /**
     * 获取文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives(){
        return Result.success(articleService.getListArchives());

    }

    /**
     * 查看文章详情页面
     * @param id
     * @return
     */
    @PostMapping("/view/{id}")
    public Result ArticleDetail(@PathVariable("id") Long id){
       ArticleVo articleVo= articleService.FindById(id);
       return Result.success(articleVo);
    }
    @PostMapping("/publish")
    public  Result PublishArticle(@RequestBody ArticleParam articleParam){
        return articleService.save(articleParam);
    }
}
