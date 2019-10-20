package com.example.demo.exception;

import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchExceptionHandler implements ExceptionHandler {

  @Override
  public void handleException(RepeatContext context, Throwable throwable) throws Throwable {
    log.error("Error occured during step 1 : {}",throwable);

  }

}
