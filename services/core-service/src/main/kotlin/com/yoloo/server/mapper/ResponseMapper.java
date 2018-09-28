package com.yoloo.server.mapper;

import java.util.function.Function;

public interface ResponseMapper<F, T> extends Function<F, T> {}
