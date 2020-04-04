package com.github.mrazjava.booklink.actuator;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@Component
public class AwsInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {

        Map<String, Object> aws = new LinkedHashMap<>();
        builder.withDetail("AWS", aws);

        try {
            AWSCredentials credentials = new DefaultAWSCredentialsProviderChain().getCredentials();

            String region = StringUtils.firstNonEmpty(
                    System.getenv("AWS_REGION"), System.getProperty("aws.region"));

            aws.put("region", region);
            aws.put("key", maskValue(credentials.getAWSAccessKeyId(), 6, '*'));
            aws.put("secret", maskValue(credentials.getAWSSecretKey(), 4, '*'));
        }
        catch(SdkClientException e) {
            aws.put("error", e.getMessage());
        }
    }

    private String maskValue(String value, int unmaskedAtEnd, char mask) {

        String result = null;

        if(StringUtils.isNotEmpty(value)) {
            int accessKeyOverlay = value.length()-unmaskedAtEnd;
            result = StringUtils.right(StringUtils.overlay(value, StringUtils.repeat(mask, accessKeyOverlay),0, accessKeyOverlay), 10);
        }

        return result;
    }
}
