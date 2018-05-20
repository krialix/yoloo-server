package com.yoloo.server.common.shared;

import com.googlecode.objectify.annotation.OnSave;

import java.time.LocalDateTime;

public abstract class BaseEntity<Type> implements Keyable<Type> {
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private long schemaVersion;

  protected BaseEntity() {}

  public BaseEntity(long schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public long getSchemaVersion() {
    return schemaVersion;
  }

  @OnSave
  protected void onSave() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
    updatedAt = LocalDateTime.now();
  }
}
