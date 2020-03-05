package com.github.mrazjava.booklink.actuator;

import com.github.mrazjava.booklink.config.AllowedCorsEntries;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.BuildInfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 0.1.0
 */
@Component
public class CustomBuildInfoContributor extends BuildInfoContributor {

    private SimpleDateFormat dateFormat;

    private static final String KEY_ARTIFACT = "artifact";
    private static final String KEY_GROUP = "group";
    private static final String KEY_VERSION = "version";

    @Value("${spring.profiles.active}")
    private String runningEnvironment;

    @Inject
    private AllowedCorsEntries corsConfig;

    public CustomBuildInfoContributor(BuildProperties buildProperties) {
        super(buildProperties);
    }

    @PostConstruct
    void initialize() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    }

    @Override
    public void contribute(Info.Builder builder) {

        Map<String, Object> content = generateContent();
        BuildProperties buildProps = getProperties();
        builder.withDetail("build", content);

        Map<String, Object> maven = new HashMap<>();
        maven.put(KEY_ARTIFACT, content.get(KEY_ARTIFACT));
        maven.put(KEY_GROUP, content.get(KEY_GROUP));
        maven.put(KEY_VERSION, content.get(KEY_VERSION));

        replaceValue(content, "time", dateFormat.format(Date.from(buildProps.getTime())));
        content.put("maven", maven);
        content.put("environment", runningEnvironment);
        content.put("cors-allow-origins", corsConfig.getAllowedEntries());
        content.remove("name");
        content.remove(KEY_ARTIFACT);
        content.remove(KEY_GROUP);
        content.remove(KEY_VERSION);
    }
}
