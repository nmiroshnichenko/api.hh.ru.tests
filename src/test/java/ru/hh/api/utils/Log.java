package ru.hh.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log methods container
 */
public class Log {
  private final Logger logger;

  public Log() {
    logger = LoggerFactory.getLogger(this.getClass());
  }

  public Log(Class cls) {
    logger = LoggerFactory.getLogger(cls);
  }

  public void trace(String msg) {
    logger.trace(msg);
  }

  public void debug(String msg) {
    logger.debug(msg);
  }

  public void info(String msg) {
    logger.info(msg);
  }

  public void warn(String msg) {
    logger.warn(msg);
  }

  public void error(String msg) {
    logger.error(msg);
  }
}
