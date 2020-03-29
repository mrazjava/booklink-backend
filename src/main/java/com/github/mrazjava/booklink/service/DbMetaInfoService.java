package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.rest.model.DbInfoResponse;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@Component
public class DbMetaInfoService {

    @Inject
    private DataSource dataSource;

    public DbInfoResponse dbInfo() {

        DbInfoResponse response = new DbInfoResponse();
        try {
            DatabaseMetaData dbMetadata = dataSource.getConnection().getMetaData();

            response.setDbName(dbMetadata.getDatabaseProductName());
            response.setDbVersion(dbMetadata.getDatabaseProductVersion());
            response.setDriverName(dbMetadata.getDriverName());
            response.setDriverVersion(dbMetadata.getDriverVersion());
            response.setConnectedUrl(dbMetadata.getURL());
            response.setConnectedUsername(dbMetadata.getUserName());
        }
        catch(SQLException e) {
            response.setInitError(e.getMessage());
        }

        return response;
    }
}
