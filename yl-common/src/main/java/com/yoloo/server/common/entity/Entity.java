package com.yoloo.server.common.entity;

/** An entity, as explained in the DDD book. */
public interface Entity<I, E> {

  /**
   * Gets the entity's unique ID.
   *
   * @return ID
   */
  I getId();

  /**
   * Entities compare by identity, not by attributes.
   *
   * @param other The other entity.
   * @return true if the identities are the same, regardles of other attributes.
   */
  boolean sameIdentityAs(E other);
}
