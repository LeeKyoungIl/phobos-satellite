package me.phoboslabs.phobos.satellite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PhobosSatellite {

    boolean ignore() default false;
    int samplingRate() default 100;
    String[] ignoreProfiles() default {};
    String profileKeyword() default "spring.profiles.active";
    boolean isTest() default false;
}
