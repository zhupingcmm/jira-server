package com.mf.jira.server.config;

import com.mf.jira.server.base.IDTypeEnum;
import com.mf.jira.server.base.ZkSerializer;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ZkIdGenerator {
    private ZkClient zkClient;
    private static final String ID_PATH = "/id";

    @PostConstruct
    private void init () {
        zkClient = new ZkClient("localhost:2181");
        zkClient.setZkSerializer(new ZkSerializer());
        if (!zkClient.exists(ID_PATH)) {
            zkClient.createPersistent(ID_PATH);
        }

    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");

    public Long getId(IDTypeEnum idTypeEnum) {
        LocalDateTime now = LocalDateTime.now();

        String dataTime = dateTimeFormatter.format(now);

        String value = zkClient.createPersistentSequential(ID_PATH + "/", Thread.currentThread().getName());
        long id = Long.parseLong(value.substring(ID_PATH.length() + 1));
        if (id >= 1000) {
            id = id % 1000;
        }

        String seq = StringUtils.leftPad(Long.toString(id), 3, "0");

        return Long.parseLong(idTypeEnum.getCode() + dataTime + seq);
    }

}
