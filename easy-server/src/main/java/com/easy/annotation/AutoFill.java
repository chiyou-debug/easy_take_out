package com.easy.annotation;

import com.easy.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies which method properties need to be automatically populated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoFill {

    // Attribute - Indicates whether it's for adding (INSERT) or modifying (UPDATE).
    OperationType value();

}
