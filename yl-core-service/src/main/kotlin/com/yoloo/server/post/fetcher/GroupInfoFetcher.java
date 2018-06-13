package com.yoloo.server.post.fetcher;

import com.yoloo.server.post.vo.GroupInfoResponse;
import org.springframework.web.client.RestTemplate;

public interface GroupInfoFetcher {

  GroupInfoResponse fetch(Long groupId);

  class DefaultFetcher implements GroupInfoFetcher {

    private final RestTemplate template;

    public DefaultFetcher(RestTemplate template) {
      this.template = template;
    }

    @Override
    public GroupInfoResponse fetch(Long groupId) {
      return template.getForObject("", GroupInfoResponse.class, groupId);
    }
  }
}
