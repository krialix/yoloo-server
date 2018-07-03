package com.yoloo.server.search.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PostUtil {

  public static List<Post> extractPosts(ObjectMapper mapper, File json) throws IOException {
    return mapper.readValue(json, new TypeReference<List<Post>>() {});
  }
}