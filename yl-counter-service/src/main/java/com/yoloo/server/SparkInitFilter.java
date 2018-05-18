package com.yoloo.server;

import spark.servlet.SparkFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(
    filterName = "SparkInitFilter",
    urlPatterns = {"/*"},
    initParams = {@WebInitParam(name = "applicationClass", value = "com.yoloo.server.CounterApplication")})
public class SparkInitFilter extends SparkFilter {
}
