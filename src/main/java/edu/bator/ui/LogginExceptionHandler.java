package edu.bator.ui;

import org.apache.log4j.Logger;

public class LogginExceptionHandler implements Thread.UncaughtExceptionHandler {

  private static final Logger log = Logger.getLogger(LogginExceptionHandler.class);

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    log.error(t.getName() + " error:", e);
  }
}
