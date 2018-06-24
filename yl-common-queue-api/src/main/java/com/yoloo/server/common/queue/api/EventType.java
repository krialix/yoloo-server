package com.yoloo.server.common.queue.api;

public enum EventType {
  NEW_POST,
  NEW_COMMENT,
  NEW_USER,
  APPROVE_COMMENT,
  FOLLOW_USER,
  UPDATE_USER,
  UPDATE_POST,
  DELETE_USER,
  DELETE_POST
}
