package com.yoloo.server.firebase.filter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({FirebaseConfig.class, FirebaseSecurityAdvice.class})
public @interface EnableFirebaseSecurity {}
