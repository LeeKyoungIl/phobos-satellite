package me.phoboslabs.phobos.satellite.scanner;

import jakarta.servlet.http.HttpServletRequest;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import me.phoboslabs.phobos.satellite.enumeration.PackageType;
import me.phoboslabs.phobos.satellite.scanner.constant.ConstSatelliteProcessor;
import me.phoboslabs.phobos.satellite.scanner.thread.ScannerProcessorAsyncThread;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PhobosScanner {

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("PhobosScannerWorkerThreads");
    private static Thread THREAD;

    private PhobosScanner() {}

    @SuppressWarnings("java:S106")
    public static void init() {
        generateAsyncThread();

        System.out.println(ConstSatelliteProcessor.BANNER_TEXT);
        System.out.println(" - PhobosScanner is loaded");
        System.out.println("");
    }

    private static void generateAsyncThread() {
        ScannerProcessorAsyncThread scannerProcessorAsyncThread = new ScannerProcessorAsyncThread();
        THREAD = new Thread(THREAD_GROUP, scannerProcessorAsyncThread, "PhobosScannerAsyncThread");
        THREAD.setDaemon(true);
        THREAD.start();
    }

    @SuppressWarnings("java:S1144")
    private static PhobosSatellite getPhobosSatellite(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        PhobosSatellite phobosSatellite = method.getAnnotation(PhobosSatellite.class);

        if (phobosSatellite == null) {
            phobosSatellite = pjp.getTarget().getClass().getAnnotation(PhobosSatellite.class);
        }

        return phobosSatellite;
    }

    @SuppressWarnings("java:S112")
    public static Object execute(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Map<String, Object> originMethodExecute = getMethodExecuteResult(pjp);
        long elapsedTime = System.currentTimeMillis() - start;

        PhobosSatellite annotation = getAnnotation(pjp);
        PackageType packageType = (annotation != null) ? annotation.packageType() : PackageType.DEFAULT;
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Object[] args = pjp.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        ConstSatelliteProcessor.addToQueue(pjp);
        return originMethodExecute.get("result");
    }

    private static PhobosSatellite getAnnotation (final ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        PhobosSatellite phobosSatellite = method.getAnnotation(PhobosSatellite.class);
        if (phobosSatellite == null) {
            phobosSatellite = pjp.getTarget().getClass().getAnnotation(PhobosSatellite.class);
        }

        return phobosSatellite;
    }

    private static Map<String, Object> getMethodExecuteResult (final ProceedingJoinPoint pjp) {
        final Map<String, Object> originMethodExecute = new HashMap<>();

        try {
            originMethodExecute.put("result", pjp.proceed());
        } catch (Throwable ex) {
            originMethodExecute.put("throwable", ex);
            originMethodExecute.put("result", getExceptionMessageChain(ex));
        }

        return originMethodExecute;
    }

    public static String getExceptionMessageChain (Throwable throwable) {
        final StringBuilder result = new StringBuilder()
                .append("[PhobosSatelliteException] : An exception occurred while running")
                .append("\r\n\r\n");

        while (throwable != null) {
            result.append(throwable.toString());
            throwable = throwable.getCause();
        }

        return result.toString();
    }
}

