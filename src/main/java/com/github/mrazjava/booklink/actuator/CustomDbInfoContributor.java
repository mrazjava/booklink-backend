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

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long hikariConnectionTimeout;

    @Value("${spring.datasource.validation-query}")
    private String validationQuery;

    @Value("${spring.datasource.max-active}")
    private Integer maxActiveConnections;

    @Value("${spring.datasource.initial-size}")
    private Integer initialConnectionPoolSize;

    @Value("${spring.datasource.max-idle}")
    private Integer maxIdleConnections;

    @Override
    public void contribute(Info.Builder builder) {

        Map<String, Object> database = new HashMap<>();
        builder.withDetail("database", database);
        Map<String, String> dbDriver = new HashMap<>();
        Map<String, Object> dbConnectionInfo = new HashMap<>();

        DbInfoResponse dbInfo = dbMetaInfoService.dbInfo();

        database.put("validation-query", validationQuery);
        database.put("connection", dbConnectionInfo);

        dbConnectionInfo.put("timeout", hikariConnectionTimeout);
        dbConnectionInfo.put("max-active", maxActiveConnections);
        dbConnectionInfo.put("initial-pool-size", initialConnectionPoolSize);
        dbConnectionInfo.put("max-idle", maxIdleConnections);

        if (StringUtils.isBlank(dbInfo.getInitError())) {
            dbDriver.put("name", dbInfo.getDriverName());
            dbDriver.put("version", dbInfo.getDriverVersion());
            dbDriver.put("class-name", driverClass);

            database.put("product", dbInfo.getDbName());
            database.put("version", dbInfo.getDbVersion());
            database.put("driver", dbDriver);
            database.put("url", dbInfo.getConnectedUrl());
        } else {
            database.put("driver-class-name", driverClass);
            database.put("error", dbInfo.getInitError());
        }
    }
}
