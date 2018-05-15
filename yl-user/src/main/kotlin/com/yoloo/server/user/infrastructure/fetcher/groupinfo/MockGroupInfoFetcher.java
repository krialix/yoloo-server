package com.yoloo.server.user.infrastructure.fetcher.groupinfo;

import com.yoloo.server.user.domain.vo.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Profile("dev")
@Component
public class MockGroupInfoFetcher implements GroupInfoFetcher {

  @NotNull
  @Override
  public List<UserGroup> fetch(@NotNull Collection<Long> ids) {
    return ids.stream().map(id -> new UserGroup(id, "image", "name")).collect(Collectors.toList());
  }
}
