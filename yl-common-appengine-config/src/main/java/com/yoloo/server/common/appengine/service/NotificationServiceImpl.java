package com.yoloo.server.common.appengine.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.yoloo.server.common.appengine.util.AppengineEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationServiceImpl implements NotificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

  private final FirebaseMessaging firebaseMessaging;

  public NotificationServiceImpl(FirebaseMessaging firebaseMessaging) {
    this.firebaseMessaging = firebaseMessaging;
  }

  @Override
  public void send(Message message) {
    boolean dryRun = !AppengineEnv.isProd();

    try {
      firebaseMessaging.send(message, dryRun);
    } catch (FirebaseMessagingException e) {
      LOGGER.error("An error occurred while sending message", e);
    }
  }

  @Override
  public void sendAsync(Message message) {
    boolean dryRun = !AppengineEnv.isProd();

    firebaseMessaging.sendAsync(message, dryRun);
  }
}
