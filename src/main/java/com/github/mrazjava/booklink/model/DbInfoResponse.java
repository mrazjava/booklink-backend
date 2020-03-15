package com.github.mrazjava.booklink.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author mrazjava
 * @since 0.2.0
 */
public class DbInfoResponse {

    private String dbName;

    private String dbVersion;

    private String driverVersion;

    private String connectedUrl;

    private String connectedUsername;

    private String initError;

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getConnectedUrl() {
        return connectedUrl;
    }

    public void setConnectedUrl(String connectedUrl) {
        this.connectedUrl = connectedUrl;
    }

    public String getConnectedUsername() {
        return connectedUsername;
    }

    public void setConnectedUsername(String connectedUsername) {
        this.connectedUsername = connectedUsername;
    }

    public String getInitError() {
        return initError;
    }

    public void setInitError(String initError) {
        this.initError = initError;
    }
}
