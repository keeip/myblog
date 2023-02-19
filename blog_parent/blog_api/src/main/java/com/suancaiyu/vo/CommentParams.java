package com.suancaiyu.vo;

import lombok.Data;

@Data
/**
 * 评论参数封装
 */
public class CommentParams {
    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
