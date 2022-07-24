package com.mf.jira.server.kafka;

import com.mf.jira.server.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = {"order.usd2cny"}, groupId = "zp", containerFactory = "filterFactory")
    public void onBatchMessage(List<ConsumerRecord<String, String>> records){
        records.forEach(record -> {
            log.info("batch consumer === topic: {} partition: {}  offset: {}  value：{} ", record.topic() , record.partition(), record.offset() , record.value());
        });
    }

    @KafkaListener(topics = {"order.usd2cny"}, groupId = "zp", containerFactory = "filterFactory")
    public void onSingleMessage(ConsumerRecord<String, Order> record){
        log.info("single consumer === topic: {} partition: {}  offset: {}  value：{} ", record.topic() , record.partition(), record.offset() , record.value());
    }
}
