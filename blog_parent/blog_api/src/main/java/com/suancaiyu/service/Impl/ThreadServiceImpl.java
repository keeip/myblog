package com.suancaiyu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suancaiyu.mapper.ArticleAndTagMapper;
import com.suancaiyu.mapper.ArticleMapper;
import com.suancaiyu.pojo.Article;
import com.suancaiyu.pojo.ArticleTag;
import com.suancaiyu.service.ThreadService;
import com.suancaiyu.vo.ArticleParam;
import com.suancaiyu.vo.TagVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ThreadServiceImpl implements ThreadService {
    @Resource
    private ArticleAndTagMapper articleAndTagMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Override
    @Async("taskExecutor")
    public void updateViewCount(Article article) {
        Article updateArticle=new Article();
        //更新操作时，对象属性值为null是不进行更新，所以属性一般都用包装类
        updateArticle.setViewCounts(article.getViewCounts()+1);
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getId,article.getId());
        lambdaQueryWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(updateArticle,lambdaQueryWrapper);
        /**添加睡眠时间，证明不会影响主线程
         * try{
         *             Thread.sleep(5000);
         *         } catch (InterruptedException e) {
         *             throw new RuntimeException(e);
         *         }
         */

    }

    @Override
    @Async("taskExecutor")
    public void insertarticle_tag(ArticleParam articleParam) {
        List<TagVo> tagVoList=articleParam.getTags();
        if (tagVoList!=null)
        {
            for (TagVo tagVo:tagVoList){
                ArticleTag articleTag =new ArticleTag();
                articleTag.setTagId(tagVo.getId());
                articleTag.setArticleId(articleParam.getId());
                articleAndTagMapper.insert(articleTag);
            }
        }
    }
}
