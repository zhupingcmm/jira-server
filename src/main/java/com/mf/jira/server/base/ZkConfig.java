package com.mf.jira.server.base;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class ZkConfig {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZkClient zkClient;

    /**
     * 初始化zkClient
     */
    @PostConstruct
    public void init () {
        zkClient = new ZkClient("localhost:2181");
        zkClient.setZkSerializer(new ZkSerializer());
    }

    /**
     * 在zk上更新数据
     * @param path 存储路径
     * @param data 存储数据
     */
    public void setConfigData(String path, String data) {
        if (zkClient.exists(path)) {
            zkClient.writeData(path, data);
            log.info("更新配置内容,{}", data);

        } else {
            zkClient.createPersistent(path, data);
            log.info("创建配置内容,{}", data);
        }
    }

    /**
     * 获取zk数据
     * @param path 存储路径
     * @return
     * @throws InterruptedException
     */
    public String getConfigData (String path) throws InterruptedException {
        String data = zkClient.readData(path);
        log.info("获取到的配置内容: {}", data);
        IZkDataListener dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                log.info("获取到 {} 配置内容更新: {}",dataPath, data);
                countDownLatch.countDown();
            }
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                log.info("获取到 {} 配置内容被删除",dataPath);
                countDownLatch.countDown();

            }
        };
        zkClient.subscribeDataChanges(path, dataListener);
        countDownLatch.await();
        return data;
    }
}
