package com.mf.jira.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mf.jira.server.dto.OrderDto;
import com.mf.jira.server.model.Order;
import com.mf.jira.server.service.OrderService;
import com.mf.jira.server.util.ObjectTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void createOrder(OrderDto orderDto) {
       kafkaTemplate.send(new ProducerRecord<String, String>("order.usd2cny", "JIRA", JSON.toJSONString(ObjectTransformer.transform(orderDto, Order.class))))
                .addCallback(success -> {
                    RecordMetadata  recordMetadata = success.getRecordMetadata();
                    ProducerRecord <String, String> producerRecord = success.getProducerRecord();
                    log.info("Producer==== send result: topic: {}, partition {}, offset {}, value {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), producerRecord.value());
                }, failure -> {
                    log.info("Producer failed to send message");
                });
    }
}
