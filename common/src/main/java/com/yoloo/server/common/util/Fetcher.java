package com.yoloo.server.common.util;

import java.io.IOException;

public interface Fetcher<I, O> {

  O fetch(I request) throws IOException;
}
