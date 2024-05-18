package me.phoboslabs.phobos.satellite.scanner;

import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import me.phoboslabs.phobos.satellite.constant.ConstSatelliteProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class PhobosScanner {

    public static void init() {
        System.out.println(ConstSatelliteProcessor.BANNER_TEXT);
        System.out.println("PhobosScanner is loaded");
    }

    private static PhobosSatellite getPhobosSatellite(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        PhobosSatellite phobosSatellite = method.getAnnotation(PhobosSatellite.class);

        if (phobosSatellite == null) {
            phobosSatellite = pjp.getTarget().getClass().getAnnotation(PhobosSatellite.class);
        }

        return phobosSatellite;
    }

    public static Object execute(ProceedingJoinPoint pjp) throws Throwable {
        try {
            PhobosSatellite phobosSatellite = getPhobosSatellite(pjp);
            if (phobosSatellite == null || phobosSatellite.ignore()) {
                return pjp.proceed();
            }

            Object result = pjp.proceed();
            return result;
        } catch (Exception ex) {
            return pjp.proceed();
        }
    }

    private static void saveRequestLog() {

    }

    private static void saveResponseLog() {

    }

    private static void saveErrorLog() {

    }
}

