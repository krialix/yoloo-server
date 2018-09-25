package com.yoloo.server.mapper;

import java.util.function.BiFunction;

public interface ResponseMapperWithParams<F, T, P> extends BiFunction<F, P, T> {}
