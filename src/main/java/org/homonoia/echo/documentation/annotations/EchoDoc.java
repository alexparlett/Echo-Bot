package org.homonoia.echo.documentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2015-2018 Homonoia Studios.
 *
 * @author alexparlett
 * @since 11/11/2018
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EchoDoc {
    String value();
    String description();
    String namespace();
    EchoDocExample[] examples() default {};
}
