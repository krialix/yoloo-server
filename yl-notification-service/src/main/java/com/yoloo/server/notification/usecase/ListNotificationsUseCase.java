package com.yoloo.server.notification.usecase;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;
import com.yoloo.server.notification.entity.Notification;
import com.yoloo.server.notification.mapper.NotificationResponseMapper;
import com.yoloo.server.notification.vo.NotificationCollectionResponse;

import static com.yoloo.server.objectify.ObjectifyProxy.ofy;

public class ListNotificationsUseCase {

  private final NotificationResponseMapper mapper;

  public ListNotificationsUseCase(NotificationResponseMapper mapper) {
    this.mapper = mapper;
  }

  public NotificationCollectionResponse execute(long userId, String cursor) {
    return NotificationCollectionResponse.builder().build();
  }

  private QueryResultIterator<Notification> getNotificationQueryResultIterator(long userId, String cursor) {
    Query<Notification> query = ofy()
        .load()
        .type(Notification.class)
        .filter(Notification.INDEX_RECEIVER_ID, userId)
        .orderKey(true);

    if (cursor != null) {
      query = query.startAt(Cursor.fromWebSafeString(cursor));
    }

    query = query.limit(50);

    return query.iterator();
  }
}
