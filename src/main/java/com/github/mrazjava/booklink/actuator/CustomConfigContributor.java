package com.github.mrazjava.booklink.actuator;

import com.github.mrazjava.booklink.config.AllowedCorsEntries;
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
public class CustomConfigContributor implements InfoContributor {

    @Inject
    private AllowedCorsEntries corsConfig;


    @Override
    public void contribute(Info.Builder builder) {

        Map<String, Object> config = new HashMap<>();
        builder.withDetail("configuration", config);

        config.put("cors-allow-origins", corsConfig.getAllowedEntries());
    }
}
