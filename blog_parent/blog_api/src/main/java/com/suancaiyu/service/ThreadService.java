package com.suancaiyu.service;

import com.suancaiyu.pojo.Article;
import com.suancaiyu.vo.ArticleParam;
import com.suancaiyu.vo.TagVo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 线程池
 */
public interface ThreadService {


    void updateViewCount(Article article);

    void insertarticle_tag(ArticleParam articleParam);
}
