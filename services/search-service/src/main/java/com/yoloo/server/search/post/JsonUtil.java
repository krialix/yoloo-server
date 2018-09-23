package com.yoloo.server.search.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtil {

  public static <T> List<T> toList(ObjectMapper mapper, File json) throws IOException {
    return mapper.readValue(json, new TypeReference<List<T>>() {});
  }
}