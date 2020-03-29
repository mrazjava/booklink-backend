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
public class CustomDbInfoContributor implements InfoContributor {

    @Inject
    private DbMetaInfoService dbMetaInfoService;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;


    @Override
    public void contribute(Info.Builder builder) {

        Map<String, Object> database = new HashMap<>();
        builder.withDetail("database", database);
        Map<String, String> dbDriver = new HashMap<>();

        DbInfoResponse dbInfo = dbMetaInfoService.dbInfo();

        if (StringUtils.isBlank(dbInfo.getInitError())) {
            dbDriver.put("name", dbInfo.getDriverName());
            dbDriver.put("version", dbInfo.getDriverVersion());
            dbDriver.put("class", driverClass);
            database.put("product", dbInfo.getDbName());
            database.put("version", dbInfo.getDbVersion());
            database.put("driver", dbDriver);
            database.put("url", dbInfo.getConnectedUrl());
        } else {
            database.put("init-error", dbInfo.getInitError());
        }
    }
}
