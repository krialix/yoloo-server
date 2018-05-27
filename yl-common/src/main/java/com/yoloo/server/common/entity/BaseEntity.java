package com.yoloo.server.common.entity;

import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import com.yoloo.server.common.vo.AuditData;
import com.yoloo.server.common.vo.Keyable;

import java.time.LocalDateTime;

public abstract class BaseEntity<I, E> implements Entity<I, E>, Keyable<E> {

  private AuditData auditData;

  public BaseEntity() {}

  public AuditData getAuditData() {
    return auditData;
  }

  @OnSave
  protected void onSave() {
    if (auditData == null) {
      auditData = new AuditData(LocalDateTime.now(), null);
    }
  }

  @OnLoad
  protected void onLoad() {}
}
