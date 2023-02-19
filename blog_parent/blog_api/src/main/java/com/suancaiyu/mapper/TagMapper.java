package com.suancaiyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suancaiyu.pojo.Tag;
import com.suancaiyu.vo.TagVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 查询最热标签id列表
     * @param limt
     * @return
     */
    public List<Long> FindHotTagId(int limt);

    /**
     * 根据文章id查询标签列表
     * @param id
     * @return
     */
    public List<Tag> FindByArticleId(Long id);

    List<Tag> FindById(  List<Long> tagIdList);
}
