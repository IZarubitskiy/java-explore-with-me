package ru.practicum.ewm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.client.StatisticClient;

@Configuration
public class AppConfig {
    @Bean
    public StatisticClient getStatisticClient(@Value("${stats-server.url:http://localhost:9090}") String address) {
        return new StatisticClient(address);
    }
}