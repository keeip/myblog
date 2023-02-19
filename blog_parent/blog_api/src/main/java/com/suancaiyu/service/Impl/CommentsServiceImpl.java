package com.suancaiyu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suancaiyu.mapper.CommentMapper;
import com.suancaiyu.pojo.Comment;
import com.suancaiyu.pojo.SysUser;
import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.service.CommentsService;
import com.suancaiyu.service.SysUserService;
import com.suancaiyu.utils.UserThreadLocal;
import com.suancaiyu.vo.CommentParams;
import com.suancaiyu.vo.CommentVo;
import com.suancaiyu.vo.ErrorCode;
import com.suancaiyu.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private SysUserService sysUserService;
    @Override
    public Result getCommentsByArticleId(Long id) {
        /**
         * 1 根据id查找文章评论列表
         * 2 先查找level为1的评论
         * 3 使用copylist函数转化为List<Commentvo>对象
         * 4 copylist内部使用copy把单个comment转化为commentvo
         * 5 查出level为1的子评论
         * 6 继续利用copylist函数转化子评论
         * 7 算法本质利用了递归，不断往下查找
         */
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId,id);
        lambdaQueryWrapper.eq(Comment::getLevel,1);
        List<Comment> commentList=commentMapper.selectList(lambdaQueryWrapper);
        return Result.success(copylist(commentList));
    }

    @Override
    public Result save(CommentParams commentParams) {
        /**
         * 1 本地线程内获取用户信息
         * 2 new comment对象，完善然后插入表中
         */
        log.info("commentParams:{}",commentParams);
        SysUser sysUser= UserThreadLocal.get();
        Comment comment=new Comment();
        Long parent= commentParams.getParent();
        Long toUserId = commentParams.getToUserId();
        comment.setContent(commentParams.getContent());
        comment.setToUid(toUserId == null ? 0 : toUserId);
        comment.setArticleId(commentParams.getArticleId());
        comment.setCreateDate(System.currentTimeMillis());
        if (commentParams.getParent()==null||commentParams.getParent()==0){
            comment.setLevel(1);
        }
        else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long userId=sysUser.getId();
        comment.setAuthorId(userId);
        int isComment= commentMapper.insert(comment);
        if (isComment<1){
            return Result.fail(ErrorCode.COMMENT_FILED.getCode(),ErrorCode.COMMENT_FILED.getMsg());
        }
        return Result.success(null);
    }

    private List<CommentVo> copylist(List<Comment> commentList) {
        List<CommentVo> commentVoList=new ArrayList<>();
        for (Comment comment:commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo=new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //把时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //设置author属性
        Long authorId=comment.getAuthorId();
        UserVo userVo= sysUserService.FindById(authorId);
        commentVo.setAuthor(userVo);
        //设置子评论
        List<CommentVo> childrensCommentVoList=findCommentsByParentId(comment.getId());
        commentVo.setChildrens(childrensCommentVoList);
        //设置toUser属性---对谁回复的
        if (comment.getLevel()>1){
            /**
             * 评论等级大于1才有父评论
             */
            Long touserId=comment.getToUid();
            UserVo toUserVo=sysUserService.FindById(touserId);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        /**
         * 寻找子评论并且调用copylist函数转化为list<commentvo>
         */
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getParentId,id);
        lambdaQueryWrapper.eq(Comment::getLevel,2);
        List<Comment> commentList=commentMapper.selectList(lambdaQueryWrapper);
        return copylist(commentList);
    }
}
