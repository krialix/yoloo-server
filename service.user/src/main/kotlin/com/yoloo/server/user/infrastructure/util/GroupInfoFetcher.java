package com.yoloo.server.user.infrastructure.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.yoloo.server.user.domain.vo.UserGroup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupInfoFetcher {

  private final URLFetchService urlFetchService;

  public GroupInfoFetcher(URLFetchService urlFetchService) {
    this.urlFetchService = urlFetchService;
  }

  public List<UserGroup> fetch(Collection<String> ids) throws IOException {
    String idsString = ids.stream().collect(Collectors.joining(","));
    ObjectMapper mapper = new ObjectMapper();

    HTTPResponse response = urlFetchService.fetch(new URL(""));

    return mapper.readValue(response.getContent(), new TypeReference<List<UserGroup>>() {});
  }
}
