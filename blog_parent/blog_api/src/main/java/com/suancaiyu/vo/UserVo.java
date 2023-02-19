package com.suancaiyu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserVo {
    private String nickname;


    private String avatar;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}
