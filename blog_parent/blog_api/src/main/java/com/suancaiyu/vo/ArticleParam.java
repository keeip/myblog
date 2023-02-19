package com.suancaiyu.vo;

import lombok.Data;

import java.util.List;

@Data
/**
 * 用于接收前端传输的文章信息
 */
public class ArticleParam {
    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
