package com.suancaiyu.service;

import com.suancaiyu.resultcommon.Result;
import com.suancaiyu.vo.TagVo;
import org.springframework.stereotype.Service;

import java.util.List;
public interface TagService {
    public List<TagVo> FindByArticleId(Long id);

    /**
     * 获取最热标签
     * @param limt
     * @return
     */
    public Result getHotTag(int limt);

    public Result getTagVoList();

   public Result getTagList();

   public Result findDetailById(Long id);
}
