package com.suancaiyu.pojo;

import lombok.Data;

@Data
/**
 * 文章与标签id联系实体对象
 */
public class ArticleTag {
    private Long id;

    private Long articleId;

    private Long tagId;
}
