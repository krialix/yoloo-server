package com.yoloo.server.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@Configuration
@ConfigurationProperties(prefix = "yoloo.notification")
public class NotificationProperties {

  @Valid private Scheduler scheduler = new Scheduler();

  public Scheduler getScheduler() {
    return scheduler;
  }

  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public static class Scheduler {
    @Positive private long fixedRate = 5000;
    @Positive private long initialDelay = 5000;

    public long getFixedRate() {
      return fixedRate;
    }

    public void setFixedRate(long fixedRate) {
      this.fixedRate = fixedRate;
    }

    public long getInitialDelay() {
      return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
      this.initialDelay = initialDelay;
    }
  }
}
