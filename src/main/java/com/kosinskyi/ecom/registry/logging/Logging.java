package com.kosinskyi.ecom.registry.logging;

import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Logging {

  LogLevel level() default LogLevel.INFO;

}
