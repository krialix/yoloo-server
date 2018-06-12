package com.yoloo.server.feed;

public abstract class ItemDelegate<T> {

  protected abstract boolean isForItemType(int total, int current);
}
