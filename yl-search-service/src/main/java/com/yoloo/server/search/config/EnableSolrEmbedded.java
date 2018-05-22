package com.yoloo.server.search.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
@Import(SolrEmbeddedConfiguration.class)
public @interface EnableSolrEmbedded {}
