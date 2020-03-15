package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.model.DbInfoResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Random;

/**
 * @since 0.1.0
 */
@Component
public class ProofOfConceptService {

    @Inject
    private Logger log;

    @Inject
    private DataSource dataSource;

    public int randomCount() {
        int count = new Random().nextInt();
        log.debug("computed count: {}", count);
        return count;
    }

    public DbInfoResponse dbInfo() throws SQLException {

        DbInfoResponse response = new DbInfoResponse();
        DatabaseMetaData dbMetadata = dataSource.getConnection().getMetaData();

        response.setDbName(dbMetadata.getDatabaseProductName());
        response.setDbVersion(dbMetadata.getDatabaseProductVersion());
        response.setDriverVersion(dbMetadata.getDriverVersion());
        response.setConnectedUrl(dbMetadata.getURL());
        response.setConnectedUsername(dbMetadata.getUserName());

        return response;
    }
}
