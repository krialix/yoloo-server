package com.yoloo.server.common.vo;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuditData implements ValueObject<AuditData> {

  private LocalDateTime createdAt;
  private LocalDateTime deletedAt;

  private AuditData() {
  }

  public AuditData(LocalDateTime createdAt, LocalDateTime deletedAt) {
    this.createdAt = createdAt;
    this.deletedAt = deletedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public boolean isDeleted() {
    return deletedAt != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuditData auditData = (AuditData) o;
    return Objects.equals(createdAt, auditData.createdAt)
        && Objects.equals(deletedAt, auditData.deletedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt, deletedAt);
  }

  @Override
  public String toString() {
    return "AuditData{" + "createdAt=" + createdAt + ", deletedAt=" + deletedAt + '}';
  }
}
