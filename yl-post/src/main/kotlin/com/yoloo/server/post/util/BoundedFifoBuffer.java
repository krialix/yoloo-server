package com.yoloo.server.post.util;

import org.jetbrains.annotations.NotNull;

import java.nio.BufferOverflowException;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The BoundedFifoBuffer is a very efficient implementation of Buffer that does not alter the size
 * of the buffer at runtime.
 *
 * <p>The removal order of a <code>BoundedFifoBuffer</code> is based on the insertion order;
 * elements are removed in the same order in which they were added. The iteration order is the same
 * as the removal order.
 *
 * <p>The {@link #add(Object)}, {@link #remove()} and {@link #get()} operations all perform in
 * constant time. All other operations perform in linear time or worse.
 *
 * <p>Note that this implementation is not synchronized. The following can be used to provide
 * synchronized access to your <code>BoundedFifoBuffer</code>:
 *
 * <pre>
 *   Buffer fifo = BufferUtils.synchronizedBuffer(new BoundedFifoBuffer());
 * </pre>
 *
 * <p>This buffer prevents null objects from being added.
 *
 * @since Commons Collections 3.0 (previously in main package v2.1)
 * @version $Revision: 1.5 $ $Date: 2004/01/04 18:56:37 $
 * @author Avalon
 * @author Berin Loritsch
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Herve Quiroz
 */
public class BoundedFifoBuffer extends AbstractCollection {

  private final long[] elements;
  private final int maxElements;
  private int start = 0;
  private int end = 0;
  private boolean full = false;

  /** Constructs a new <code>BoundedFifoBuffer</code> big enough to hold 32 elements. */
  public BoundedFifoBuffer() {
    this(32);
  }

  /**
   * Constructs a new <code>BoundedFifoBuffer</code> big enough to hold the specified number of
   * elements.
   *
   * @param size the maximum number of elements for this fifo
   * @throws IllegalArgumentException if the size is less than 1
   */
  public BoundedFifoBuffer(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("The size must be greater than 0");
    }
    elements = new long[size];
    maxElements = elements.length;
  }

  /**
   * Returns the number of elements stored in the buffer.
   *
   * @return this buffer's size
   */
  public int size() {
    int size = 0;
    if (end < start) {
      size = maxElements - start + end;
    } else if (end == start) {
      size = (full ? maxElements : 0);
    } else {
      size = end - start;
    }
    return size;
  }

  /**
   * Returns true if this buffer is empty; false otherwise.
   *
   * @return true if this buffer is empty
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns true if this collection is full and no new elements can be added.
   *
   * @return <code>true</code> if the collection is full
   */
  public boolean isFull() {
    return size() == maxElements;
  }

  /**
   * Gets the maximum size of the collection (the bound).
   *
   * @return the maximum number of elements the collection can hold
   */
  public int maxSize() {
    return maxElements;
  }

  /** Clears this buffer. */
  public void clear() {
    full = false;
    start = 0;
    end = 0;
    Arrays.fill(elements, -1);
  }

  /**
   * Adds the given element to this buffer.
   *
   * @param element the element to add
   * @return true, always
   * @throws NullPointerException if the given element is null
   * @throws BufferOverflowException if this buffer is full
   */
  public boolean add(long element) {
    if (full) {
      throw new RuntimeException("The buffer cannot hold more than " + maxElements + " objects.");
    }
    elements[end++] = element;
    if (end >= maxElements) {
      end = 0;
    }
    if (end == start) {
      full = true;
    }
    return true;
  }

  /**
   * Returns the least recently inserted element in this buffer.
   *
   * @return the least recently inserted element
   * @throws RuntimeException if the buffer is empty
   */
  public Object get() {
    if (isEmpty()) {
      throw new RuntimeException("The buffer is already empty");
    }
    return elements[start];
  }

  /**
   * Removes the least recently inserted element from this buffer.
   *
   * @return the least recently inserted element
   * @throws RuntimeException if the buffer is empty
   */
  public Object remove() {
    if (isEmpty()) {
      throw new RuntimeException("The buffer is already empty");
    }
    long element = elements[start];
    if (element != -1) {
      elements[start++] = -1;
      if (start >= maxElements) {
        start = 0;
      }
      full = false;
    }
    return element;
  }

  /**
   * Increments the internal index.
   *
   * @param index the index to increment
   * @return the updated index
   */
  private int increment(int index) {
    index++;
    if (index >= maxElements) {
      index = 0;
    }
    return index;
  }

  /**
   * Decrements the internal index.
   *
   * @param index the index to decrement
   * @return the updated index
   */
  private int decrement(int index) {
    index--;
    if (index < 0) {
      index = maxElements - 1;
    }
    return index;
  }

  public long[] getElements() {
    return elements;
  }

  /**
   * Returns an iterator over this buffer's elements.
   *
   * @return an iterator over this buffer's elements
   */
  @NotNull
  public Iterator iterator() {
    return new Iterator() {
      private int index = start;
      private int lastReturnedIndex = -1;
      private boolean isFirst = full;

      public boolean hasNext() {
        return isFirst || (index != end);
      }

      public Object next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        isFirst = false;
        lastReturnedIndex = index;
        index = increment(index);
        return elements[lastReturnedIndex];
      }

      public void remove() {
        if (lastReturnedIndex == -1) {
          throw new IllegalStateException();
        }
        // First element can be removed quickly
        if (lastReturnedIndex == start) {
          BoundedFifoBuffer.this.remove();
          lastReturnedIndex = -1;
          return;
        }
        // Other elements require us to shift the subsequent elements
        int i = lastReturnedIndex + 1;
        while (i != end) {
          if (i >= maxElements) {
            elements[i - 1] = elements[0];
            i = 0;
          } else {
            elements[i - 1] = elements[i];
            i++;
          }
        }
        lastReturnedIndex = -1;
        end = decrement(end);
        elements[end] = -1;
        full = false;
        index = decrement(index);
      }
    };
  }
}
