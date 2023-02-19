package com.suancaiyu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suancaiyu.mapper.ArticleAndTagMapper;
import com.suancaiyu.mapper.ArticleBodyMapper;
import com.suancaiyu.mapper.ArticleMapper;
import com.suancaiyu.mapper.CategoryMapper;
import com.suancaiyu.pojo.*;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.ArticleService;
import com.suancaiyu.service.SysUserService;
import com.suancaiyu.service.TagService;
import com.suancaiyu.service.ThreadService;
import com.suancaiyu.utils.UserThreadLocal;
import com.suancaiyu.vo.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private ArticleAndTagMapper articleAndTagMapper;
    @Resource
    private ArticleBodyMapper articleBodyMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagService tagService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ThreadService threadService;
    @Override
    public Result articleList(PageParams pageParams) {
        //分页查询
        Page<Article> page=new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> ipage =  articleMapper.getListArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return Result.success(copylist(ipage.getRecords(),true,true,false,true));
    }

    @Override
    public List<ArticleVo> getHotArticleList(int limit) {
        /**
         * 根据浏览量查询id和title
         */
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
        lambdaQueryWrapper.select(Article::getId,Article::getTitle);
        lambdaQueryWrapper.last("limit "+limit);//sql语句结尾处添加 limit 5 限制五条
        List<Article> articleList=articleMapper.selectList(lambdaQueryWrapper);
        return copylist(articleList,false,false);
    }

    @Override
    public List<ArticleVo> getNewArticleList(int limit) {
       LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
       lambdaQueryWrapper.orderByDesc(Article::getCreateDate);
       lambdaQueryWrapper.select(Article::getId,Article::getTitle);
       lambdaQueryWrapper.last("limit "+limit);
       return copylist(articleMapper.selectList(lambdaQueryWrapper),false,false);
    }

    @Override
    public List<Archives> getListArchives() {
        return articleMapper.getListArchives();
    }

    @Override
    public ArticleVo FindById(Long id) {
        /**
         * 查询用户并且封装vo对象返回
         * 查看完文章需要更新阅读数量，更新阅读数量是对数据库一个更新操作，更新操作加锁的会阻塞其他读操作，本该直接返回数据，但因为更新操作不得不等待他完成在返回
         * 更新增加这次接口的耗时，并且更新操作如果发生异常，会阻塞我们读操作，我们可以使用线程池来并发同时实现更新和读操作
         * 把更新操作丢入线程池中，让线程池去操作
         */

       Article article= articleMapper.selectById(id);
       //把更新操作交给线程去做
        threadService.updateViewCount(article);
       return copy(article,true,true,true,true);

    }

    @Override
    /**
     * 对三张表进行插入操作
     * 分别是article，articlebody，articletag
     * article和articlebody表插入要同步进行
     *
     */
    @Transactional
    public Result save(ArticleParam articleParam) {
        //完善article实体类
        SysUser sysUser= UserThreadLocal.get();
        Article article=new Article();
        article.setId(articleParam.getId());
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCommentCounts(0);
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setAuthorId(sysUser.getId());
        article.setBodyId(-1L);
        articleMapper.insert(article);
        //插入完成后设置id字段，不如后面tag插入无法完成
        articleParam.setId(article.getId());
        //插入body表并且返回bodyid
        ArticleBody articleBody=new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        Long bodyId=articleBody.getId();
        article.setBodyId(bodyId);
        articleMapper.updateById(article);
        //利用线程去进行article_tag表进行插入
        threadService.insertarticle_tag(articleParam);
        //返回articlevo对象
        ArticleVo articleVo=new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }
    /**
     * 转化article集合为articlevo集合
     * @param articleList
     * @return
     */
    private List<ArticleVo> copylist(List<Article> articleList,boolean isAuthor,boolean isTag) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for (Article article : articleList){
            articleVoList.add(copy(article,isAuthor,isTag));
        }
        return articleVoList;
    }
    private List<ArticleVo> copylist(List<Article> articleList,boolean isAuthor,boolean isTag,boolean isbody,boolean isCategory) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for (Article article : articleList){
            articleVoList.add(copy(article,isAuthor,isTag,isbody,isCategory));
        }
        return articleVoList;
    }

    /**
     * 转化article对象为articlevo对象
     * @param article
     * @return
     */
    private ArticleVo copy(Article article,boolean isAuthor,boolean isTag){
        ArticleVo articleVo=new ArticleVo();
        if (isAuthor){
            Long authorId=article.getAuthorId();
            SysUser sysUser= sysUserService.FindByAuthorId(authorId);
            if (sysUser==null){
                sysUser=new SysUser();
                sysUser.setNickname("未知作者");
            }
            articleVo.setAuthor(sysUser.getNickname());
        }
        if (isTag){
            Long article_id=article.getId();
            List<TagVo> tagVoList= tagService.FindByArticleId(article_id);
            articleVo.setTags(tagVoList);
        }
        BeanUtils.copyProperties(article,articleVo);
        //设置vo对象时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }
    private ArticleVo copy(Article article,boolean isAuthor,boolean isTag,boolean isBody,boolean isCategory){
        ArticleVo articleVo=new ArticleVo();
        if (isAuthor){
            Long authorId=article.getAuthorId();
            SysUser sysUser= sysUserService.FindByAuthorId(authorId);
            if (sysUser==null){
                sysUser=new SysUser();
                sysUser.setNickname("未知作者");
            }
            articleVo.setAuthor(sysUser.getNickname());
        }
        if (isTag){
            Long article_id=article.getId();
            List<TagVo> tagVoList= tagService.FindByArticleId(article_id);
            articleVo.setTags(tagVoList);
        }
        if (isBody){
            ArticleBodyVo articleBodyVo=new ArticleBodyVo();
            LambdaQueryWrapper<ArticleBody> lambdaQueryWrapper =new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(ArticleBody::getContent);
            lambdaQueryWrapper.eq(ArticleBody::getId,article.getBodyId());
            ArticleBody articleBody= articleBodyMapper.selectOne(lambdaQueryWrapper);
            BeanUtils.copyProperties(articleBody,articleBodyVo);
            articleVo.setBody(articleBodyVo);
        }
        if (isCategory){
            CategoryVo categoryVo=new CategoryVo();
            Long categoryId=article.getCategoryId();
            Category category=categoryMapper.selectById(categoryId);
            BeanUtils.copyProperties(category,categoryVo);
            articleVo.setCategory(categoryVo);
        }
        BeanUtils.copyProperties(article,articleVo);
        //设置vo对象时间
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }
}
