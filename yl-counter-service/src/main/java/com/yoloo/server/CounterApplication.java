package com.yoloo.server;

import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import static spark.Spark.get;

public class CounterApplication implements SparkApplication {

  private static final RocksDBFactory.RocksDBSimpleClient simpleClient = RocksDBFactory.rocksDBInstance("/tmp/");

  public static void main(String[] args) {
    new CounterApplication().init();
  }

  @Override
  public void init() {
    get("/increment/:counterName", (request, response) -> {
      final String counterName = request.params(":counterName");
      simpleClient.incrementCounter(counterName.getBytes());
      return "";
    });

    get("/getCounter/:counterName", (request, response) -> {
      final String counterName = request.params(":counterName");
      long value = simpleClient.retrieveCounter(counterName.getBytes());

      return String.format("%d%n", value);
    });

    get("/reset/:counterName", (request, response) -> {
      final String counterName = request.params(":counterName");
      simpleClient.resetCounter(counterName.getBytes());
      return "";
    });
  }

  @WebFilter(
      filterName = "SparkInitFilter",
      urlPatterns = {"/*"},
      initParams = {@WebInitParam(name = "applicationClass", value = "com.yoloo.server.CounterApplication")})
  public static class SparkInitFilter extends SparkFilter {
  }
}
