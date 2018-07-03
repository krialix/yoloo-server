package com.yoloo.server.auth.firebase;

import com.yoloo.server.auth.AuthConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({AuthConfig.class})
public @interface EnableFirebaseSecurity {}
