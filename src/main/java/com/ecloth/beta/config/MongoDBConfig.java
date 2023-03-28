package com.ecloth.beta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.ecloth.beta.chat.repository"})
public class MongoDBConfig {
}
