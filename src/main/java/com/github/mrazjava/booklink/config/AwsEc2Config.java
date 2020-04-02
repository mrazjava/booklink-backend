package com.github.mrazjava.booklink.config;

import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@EnableContextInstanceData
@Configuration
@Profile("ec2")
public class AwsEc2Config {
}
