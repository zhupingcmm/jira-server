package com.mf.jira.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    private Long id;
    private Long orderNo;
    private Integer amount;
    private Boolean status;
    private Long userId;
    private Long couponRecordId;
    private Date createTime;
    private Date updateTime;
}