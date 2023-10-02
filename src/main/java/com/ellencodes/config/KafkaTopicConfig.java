package com.ellencodes.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ellencodesJsonTopic() {
        return TopicBuilder.name("ellencodesJson")
                .build();
    }
}
