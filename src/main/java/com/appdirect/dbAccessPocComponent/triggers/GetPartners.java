package com.appdirect.dbAccessPocComponent.triggers;

import javax.json.Json;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appdirect.dbAccessPocComponent.HttpClientUtils;
import io.elastic.api.ExecutionParameters;
import io.elastic.api.Message;
import io.elastic.api.Module;

/**
 * Trigger to get partners from db.
 */
public class GetPartners implements Module {
  private static final Logger logger = LoggerFactory.getLogger(GetPartners.class);

  /**
   * Executes the trigger's logic by sending a request to the local connector and emitting a response to the platform.
   * @param parameters execution parameters
   */
  @Override
  public void execute(final ExecutionParameters parameters) {
    final JsonObject configuration = parameters.getConfiguration();

    logger.info("About to retrieve partners");

    final String path = "/api/v1/admin/docusignPartners";

    final JsonObject partners = HttpClientUtils.getSingle(path, configuration);

    logger.info("Got {} partners", partners.size());

    // emitting naked arrays is forbidden by the platform
    final JsonObject body = Json.createObjectBuilder()
      .add("partners", partners)
      .build();

    final Message data
      = new Message.Builder().body(body).build();

    logger.info("Emitting data");

    // emitting the message to the platform
    parameters.getEventEmitter().emitData(data);
  }
}
