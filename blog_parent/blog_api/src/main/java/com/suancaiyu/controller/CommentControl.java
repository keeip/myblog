package com.suancaiyu.controller;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.CommentsService;
import com.suancaiyu.vo.CommentParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comments")
public class CommentControl {
    @Resource
    private CommentsService commentsService;
    @GetMapping("/article/{id}")
    public Result getComments(@PathVariable("id") Long id){
        return commentsService.getCommentsByArticleId(id);
    }
    @PostMapping("create/change")
    public Result createComment(@RequestBody CommentParams commentParams){
       return commentsService.save(commentParams);
    }
}
