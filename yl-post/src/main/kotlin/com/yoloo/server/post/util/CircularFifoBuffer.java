package com.yoloo.server.post.util;

public class CircularFifoBuffer extends BoundedFifoBuffer {

  /** Constructor that creates a buffer with the default size of 32. */
  public CircularFifoBuffer() {
    super(32);
  }

  /**
   * Constructor that creates a buffer with the specified size.
   *
   * @param size the size of the buffer (cannot be changed)
   * @throws IllegalArgumentException if the size is less than 1
   */
  public CircularFifoBuffer(int size) {
    super(size);
  }

  /**
   * If the buffer is full, the least recently added element is discarded so that a new element can
   * be inserted.
   *
   * @param element the element to add
   * @return true, always
   */
  public boolean add(long element) {
    if (isFull()) {
      remove();
    }
    return super.add(element);
  }
}
