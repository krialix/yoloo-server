package com.yoloo.server.common.appengine.service;

import com.google.firebase.messaging.Message;

public interface NotificationService {

  void send(Message message);

  void sendAsync(Message message);
}
