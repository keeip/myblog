package com.suancaiyu.vo;

import lombok.Data;

@Data
/**
 * 用于接收前端文章信息内容
 */
public class ArticleBodyParam {
    private String content;

    private String contentHtml;

}
