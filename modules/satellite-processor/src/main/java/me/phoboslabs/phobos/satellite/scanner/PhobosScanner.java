package me.phoboslabs.phobos.satellite.scanner;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import me.phoboslabs.phobos.satellite.enumeration.PackageType;
import me.phoboslabs.phobos.satellite.scanner.constant.ConstSatelliteProcessor;
import me.phoboslabs.phobos.satellite.scanner.thread.ScannerProcessorAsyncThread;
import me.phoboslabs.phobos.satellite.scanner.vo.PhobosBaseModel;
import me.phoboslabs.phobos.satellite.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PhobosScanner {

    private static final Thread PHOBOS_DATA_PROCESS_THREAD;

    static {
        ScannerProcessorAsyncThread scannerProcessorAsyncThread = new ScannerProcessorAsyncThread();
        PHOBOS_DATA_PROCESS_THREAD = new Thread(
            new ThreadGroup("PhobosScannerWorkerThreads"),
            scannerProcessorAsyncThread,
            "PhobosScannerAsyncThread");
        PHOBOS_DATA_PROCESS_THREAD.setDaemon(true);
        PHOBOS_DATA_PROCESS_THREAD.start();
    }

    private static final ExecutorService PHOBOS_BASE_SEND_EXECUTORS = Executors.newWorkStealingPool(
        Runtime.getRuntime().availableProcessors());

    private PhobosScanner() {
    }

    @SuppressWarnings("java:S106")
    public static void init() {
        System.out.println(ConstSatelliteProcessor.BANNER_TEXT);
        System.out.println(" - PhobosScanner is loaded");
        System.out.println("");
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
        Map<String, Object> originMethodExecuteResult = getMethodExecuteResult(pjp);
        long elapsedTime = System.currentTimeMillis() - start;

        PHOBOS_BASE_SEND_EXECUTORS.submit(() -> {
            try {
                String uuid = StringUtils.sequenceGenerator();
                PhobosSatellite annotation = getAnnotation(pjp);
                PackageType packageType = (annotation != null) ? annotation.packageType() : PackageType.DEFAULT;
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                Object[] args = pjp.getArgs();
                HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

                PhobosBaseModel phobosBaseModel = new PhobosBaseModel(uuid, annotation, packageType, methodSignature, args, elapsedTime,
                    originMethodExecuteResult, request);
                ConstSatelliteProcessor.addToQueue(phobosBaseModel);
            } catch (Exception ex) {
                System.err.println("Error in PhobosScanner: " + ex.getMessage());
            }
        });

        return originMethodExecuteResult.get("result");
    }

    private static PhobosSatellite getAnnotation(final ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        PhobosSatellite phobosSatellite = method.getAnnotation(PhobosSatellite.class);
        if (phobosSatellite == null) {
            phobosSatellite = pjp.getTarget().getClass().getAnnotation(PhobosSatellite.class);
        }

        return phobosSatellite;
    }

    private static Map<String, Object> getMethodExecuteResult(final ProceedingJoinPoint pjp) {
        final Map<String, Object> originMethodExecute = new HashMap<>();

        try {
            originMethodExecute.put("result", pjp.proceed());
        } catch (Throwable ex) {
            originMethodExecute.put("throwable", ex);
            originMethodExecute.put("result", getExceptionMessageChain(ex));
        }

        return originMethodExecute;
    }

    public static String getExceptionMessageChain(Throwable throwable) {
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

