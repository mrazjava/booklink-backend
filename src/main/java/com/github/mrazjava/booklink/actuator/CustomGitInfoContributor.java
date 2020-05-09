package com.github.mrazjava.booklink.actuator;

import org.apache.commons.lang3.StringUtils;
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
 * @author AZ
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

        Map<String, Object> gitMap = generateContent();
        Map<String, Object> commitMap = getNestedMap(gitMap, "commit");
        GitProperties props = getProperties();
        props.forEach(e -> log.debug("**** GIT [{}]=[{}]", e.getKey(), e.getValue()));

        commitMap.put("message", props.get("commit.message.short"));
        commitMap.put("id", props.get("commit.id.abbrev"));
        commitMap.put("author", props.get("commit.user.name"));
        commitMap.put("host", props.get("build.host"));
        builder.withDetail("git", gitMap);

        String commitTime = dateFormat.format(Date.from(props.getCommitTime())); // reformat commit time
        replaceValue(getNestedMap(gitMap, "commit"), "time", commitTime);

        if(StringUtils.startsWith(props.getBranch(), props.getShortCommitId())) {
            // branch name will reflect commit id (due to detached state) if we're tagged, so show it accordingly
            replaceValue(gitMap, "branch", String.format("%s (tag)", props.get("tags")));
        }
        gitMap.put("origin", props.get("remote.origin.url"));
    }
}
