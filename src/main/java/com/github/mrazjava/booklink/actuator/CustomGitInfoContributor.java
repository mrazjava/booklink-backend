package com.github.mrazjava.booklink.actuator;

import org.slf4j.Logger;
import org.springframework.boot.actuate.info.GitInfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @since 0.1.0
 */
@Component
public class CustomGitInfoContributor extends GitInfoContributor {

    @Inject
    private Logger log;

    private SimpleDateFormat dateFormat;

    public CustomGitInfoContributor(GitProperties properties) {
        super(properties);
    }

    @PostConstruct
    void initialize() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    }

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> map = generateContent();
        Map<String, Object> commitMap = getNestedMap(map, "commit");
        GitProperties props = getProperties();
        props.forEach(e -> log.debug("**** GIT [{}]=[{}]", e.getKey(), e.getValue()));
        commitMap.put("message", props.get("commit.message.short"));
        commitMap.put("id", props.get("commit.id"));
        commitMap.put("author", props.get("commit.user.name"));
        commitMap.put("host", props.get("build.host"));
        builder.withDetail("git", map);

        GitProperties gitProps = getProperties();

        String commitTime = dateFormat.format(Date.from(gitProps.getCommitTime()));
        replaceValue(getNestedMap(map, "commit"), "time", commitTime);
    }
}
