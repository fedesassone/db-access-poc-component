package com.appdirect.dbAccessPocComponent;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.elastic.api.CredentialsVerifier;
import io.elastic.api.InvalidCredentialsException;

/**
 * Implementation of {@link CredentialsVerifier} used to verify that parameters provided by user
 * are valid. This is accomplished by establishing a connection with the parameters given.
 */
public class DbCredentialsVerifier implements CredentialsVerifier {

    private static final Logger logger = LoggerFactory.getLogger(DbCredentialsVerifier.class);
    private static final String BASE_PATH = "jdbc:mysql://";
    private static final String DB_SEPARATOR = "/";
    private static final String PORT_SEPARATOR = ":";

    @Override
    public void verify(final JsonObject configuration) throws InvalidCredentialsException {
        logger.info("Testing connection to DB");
        try {
          final String host = configuration.getJsonString("host").toString();
          final String port = configuration.getJsonString("port").toString();
          final String dbName = configuration.getJsonString("dbName").toString();
          final String user = configuration.getJsonString("user").toString();
          final String password = configuration.getJsonString("password").toString();

          Connection connection = DriverManager.getConnection(BASE_PATH + host + PORT_SEPARATOR + port + DB_SEPARATOR + dbName, user, password);

          if(Objects.isNull(connection)){
            throw new InvalidCredentialsException("Failed to verify connection");
          }
          logger.info("Connection successfully established");
        } catch (SQLException e) {
          throw new InvalidCredentialsException("Failed to verify connection", e);
        }
    }
}
