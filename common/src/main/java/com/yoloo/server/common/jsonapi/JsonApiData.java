package com.yoloo.server.common.jsonapi;

public abstract class JsonApiData<T> {

  public static class Builder {
    private String type;
    private String id;
    private JsonApiAttributes attributes;
    private JsonApiRelationships relationships;
  }
}
