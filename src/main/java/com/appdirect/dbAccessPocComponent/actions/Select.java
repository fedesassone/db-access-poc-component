package com.appdirect.dbAccessPocComponent.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Module;

public class Select implements Module {
  private static final Logger logger = LoggerFactory.getLogger(Select.class);

  /**
   * Executes the actions's logic by sending a request to the DB and emitting response to the platform.
   *
   * @param parameters execution parameters
   */
  @Override
  public void execute(final ExecutionParameters parameters) {


  }


}
