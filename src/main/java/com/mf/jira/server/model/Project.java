package com.mf.jira.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Long id;
    private String name;
    private Boolean pin;
    private Long personId;
    private String organization;
    private Date createTime;
    private Date updateTime;
}
