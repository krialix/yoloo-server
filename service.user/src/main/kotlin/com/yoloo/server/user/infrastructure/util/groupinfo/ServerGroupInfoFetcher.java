package com.yoloo.server.user.infrastructure.util.groupinfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.yoloo.server.user.domain.vo.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Profile("!dev")
@Component
public class ServerGroupInfoFetcher implements GroupInfoFetcher {

  private final URLFetchService urlFetchService;

  public ServerGroupInfoFetcher(URLFetchService urlFetchService) {
    this.urlFetchService = urlFetchService;
  }

  @NotNull
  @Override
  public List<UserGroup> fetch(@NotNull Collection<String> ids) throws IOException {
    String idsString = ids.stream().collect(Collectors.joining(","));
    ObjectMapper mapper = new ObjectMapper();

    HTTPResponse response = urlFetchService.fetch(new URL(""));

    return mapper.readValue(response.getContent(), new TypeReference<List<UserGroup>>() {});
  }
}
