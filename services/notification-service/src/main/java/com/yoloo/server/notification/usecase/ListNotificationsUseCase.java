package com.yoloo.server.notification.usecase;

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.QueryResults;
import com.googlecode.objectify.cmd.Query;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.mapper.NotificationResponseMapper;
import com.yoloo.server.notification.vo.NotificationCollectionResponse;
import com.yoloo.server.notification.vo.NotificationResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class ListNotificationsUseCase {

  private final NotificationResponseMapper mapper;

  public ListNotificationsUseCase(NotificationResponseMapper mapper) {
    this.mapper = mapper;
  }

  public NotificationCollectionResponse execute(long userId, String cursor) {
    QueryResults<Notification> queryResults = getQueryResultIterator(userId, cursor);

    if (!queryResults.hasNext()) {
      return NotificationCollectionResponse.builder().data(Collections.emptyList()).build();
    }

    Iterable<Notification> iterable = () -> queryResults;

    List<NotificationResponse> responses =
        StreamSupport.stream(iterable.spliterator(), false)
            .map(mapper::apply)
            .collect(Collectors.toList());

    return NotificationCollectionResponse.builder()
        .data(responses)
        .prevPageToken(cursor)
        .nextPageToken(queryResults.getCursorAfter().toUrlSafe())
        .build();
  }

  private QueryResults<Notification> getQueryResultIterator(long userId, String cursor) {
    Query<Notification> query =
        ofy()
            .load()
            .type(Notification.class)
            .filter(Notification.INDEX_RECEIVER_ID, userId)
            .orderKey(true);

    if (cursor != null) {
      query = query.startAt(Cursor.fromUrlSafe(cursor));
    }

    query = query.limit(50);

    return query.iterator();
  }
}
