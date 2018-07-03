package com.yoloo.server.common.util;

public class FcmConstants {

  public static final String FCM_KEY_TYPE = "FCM_KEY_TYPE";

  private FcmConstants() {}

  public enum FcmType {
    FCM_TYPE_NEW_POST,
    FCM_TYPE_NEW_COMMENT,
    FCM_TYPE_MENTION,
    FCM_TYPE_FOLLOW,
    FCM_TYPE_COMMENT_APPROVE
  }
}
