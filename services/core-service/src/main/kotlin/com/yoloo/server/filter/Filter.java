package com.yoloo.server.filter;

import com.googlecode.objectify.Key;

public interface Filter {

  Key<?> toFilterKey();
}
