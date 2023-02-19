package com.suancaiyu.service;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.vo.CommentParams;

public interface CommentsService {
    /**
     * 通过文章id获取评论
     * @param id
     * @return
     */
    Result getCommentsByArticleId(Long id);

    /**
     * 保存评论
     * @param commentParams
     * @return
     */
    Result save(CommentParams commentParams);
}
