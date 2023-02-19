package com.suancaiyu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章归档实体类,基于文章查询出来的
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archives {
    private Integer year;

    private Integer month;

    private Integer count;
}
