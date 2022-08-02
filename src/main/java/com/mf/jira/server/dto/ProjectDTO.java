package com.mf.jira.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDTO implements Serializable {
    private Long id;
    private String name;
    private Long personId;
    private String organization;
    private Boolean pin;
}
