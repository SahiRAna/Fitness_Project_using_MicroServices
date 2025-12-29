package com.fitness.AiService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Configuration class for MongoDB.
 * This class enables MongoDB auditing, which automatically populates fields like @CreatedDate and @LastModifiedDate.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
