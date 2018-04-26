package com.appdirect.dbAccessPocComponent;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Test;

import io.elastic.api.InvalidCredentialsException;

public class DbCredentialsVerifierTest {

  private DbCredentialsVerifier credentialsVerifier = new DbCredentialsVerifier();

  private static final String HOST = "testdbinstance.ct6iaulvwr4s.eu-west-1.rds.amazonaws.com";
  private static final String PORT = "3306";
  private static final String DB_NAME = "testDbInstance";
  private static final String USERNAME = "fedeSassone";
  private static final String PASSWORD= "FedePass1234";

  @Test(expected = InvalidCredentialsException.class)
  public void testCredentials_Fails() throws Exception {
    JsonObject configuration = Json.createObjectBuilder()
      .add("host", "")
      .add("port", "")
      .add("dbName", "")
      .add("user", "")
      .add("password", "")
      .build();

    credentialsVerifier.verify(configuration);
  }

  @Test
  public void testCredentials_Success() throws Exception {
    JsonObject configuration = Json.createObjectBuilder()
      .add("host", HOST)
      .add("port", PORT)
      .add("dbName", DB_NAME)
      .add("user", USERNAME)
      .add("password", PASSWORD)
      .build();

    credentialsVerifier.verify(configuration);
  }
}
