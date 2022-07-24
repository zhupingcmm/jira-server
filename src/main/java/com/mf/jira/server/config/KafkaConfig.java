package com.mf.jira.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConfig {

    private final ConsumerFactory consumerFactory;
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);
        factory.setBatchListener(true);

        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (consumerRecord.offset() % 2 == 0) {
                return false;
            }
              return true;
        });
        return factory;
    }
}
