package com.github.mrazjava.booklink.actuator;

import com.github.mrazjava.booklink.rest.model.DbInfoResponse;
import com.github.mrazjava.booklink.service.DbMetaInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@Component
public class AwsInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {

        Map<String, Object> aws = new HashMap<>();
        builder.withDetail("AWS", aws);

        String region = StringUtils.firstNonEmpty(
                System.getenv("AWS_REGION"),System.getProperty("aws.region"));
        String accessKeyId = StringUtils.firstNonEmpty(
                System.getenv("AWS_ACCESS_KEY_ID"), System.getProperty("aws.accessKeyId"));
        String secret = StringUtils.firstNonEmpty(
                System.getenv("AWS_SECRET_ACCESS_KEY"), System.getProperty("aws.secretKey"));

        aws.put("region", region);
        aws.put("access key", maskValue(accessKeyId, 6, '*'));
        aws.put("secret", maskValue(secret, 4, '*'));
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
