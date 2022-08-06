package com.mf.jira.server.config;

import com.mf.jira.server.base.IDTypeEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class IDGenerator {
    private final RedisTemplate<String, Object> restTemplate;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    public Long getId (IDTypeEnum idTypeEnum) {
        LocalDateTime now = LocalDateTime.now();
        String dataTime = dateTimeFormatter.format(now);
        long value = restTemplate.opsForValue().increment(idTypeEnum.getRedisCounter(), 1);

        if (value >=1000) {
            value = value % 1000;
        }
        String seq = StringUtils.leftPad(Long.toString(value), 3, "0");
        String result = idTypeEnum.getCode() + dataTime + seq;
        return Long.parseLong(result);
    }
}
