package me.phoboslabs.phobos.satellite.scanner.constant;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConstSatelliteProcessor {

    public static final String BANNER_TEXT = """
                                         ▄▄▄           \s
                                       ▄▄   ▄▄         \s
                                      ▄▀▄   ▄▀▄        \s
                                   ▄▄  ▄▄▀▀▀▄▄▄ ▄▄     \s
                      ▄▄▄▄       ▄ ▀▄▄ ▄▄ ▄ ▄▄ ▄ ▄     \s
                    ▄▄    ▄▄    ▄▄▄▄ ▀▄▀▄▄▄▄▄▀▄▄▀      \s
                  ▄▄        ▄▄   ▀▄ ▄▄ ▀▄   ▄▄         \s
                 ▄            ▄▄ ▄▄   ▄▄▄▀▄▄▀          \s
                                ▄▄ ▄▀▀▄ ▄              \s
                  ▀▄              ▄▄   ▀▀              \s
                    ▀▄              ▄▄                 \s
                      ▀         ▄▄▀ ▄▄▄▄▄▄▄            \s
                 ▄▄▄▄  ▄▄     ▄▄ ▄▄         ▄▄         \s
               ▄▄ ▀▄ ▄▄▄ ▄▀▄  ▀ ▄          ▄▀▀         \s
              ▄  ▄▄ ▄▄  ▄   ▀▄ ▄         ▄▄▀           \s
           ▄▄▀▄  ▄▄▀▄ ▀▄ ▄▄            ▄▄  ▄▄   ▄▄   ▄▄\s
         ▄▄ ▄▄▄▀▀▀▄▄ ▄▄▀▀▀▀          ▄▀▀  ▄ ▄  ▄▄   ▄ ▄\s
         ▄    ▄  ▀▄   ▄▄       ▀   ▄▄▀ ▄▄▄  ▄  ▄ ▄    ▄\s
           ▄▄▀▄▄▄▄▄▀▄▄▀         ▀▄▄    ▀▄▄▀▀▄▄ ▄▄▀▄▄ ▄ \s
             ▀▄  ▄▄▄                    ▄▄▄▄ ▄▄▀ ▄▄ ▄▀ \s
              ▀▄ ▄▀                    ▄▄ ▄▄▄▀ ▄▄  ▄▀  \s
                                            ▄▄▄  ▄▀    \s
                                       ▄▄   ▄▄▄▀▀      \s
                                       ▀▀▀▀▀           \s
                        
        """;

    private static final BlockingQueue<ProceedingJoinPoint> SCANNER_PROCESSOR_QUEUE = new LinkedBlockingQueue<>(1000);

    private ConstSatelliteProcessor() {}

    public static Boolean addToQueue(ProceedingJoinPoint proceedingJoinPoint) {
        return SCANNER_PROCESSOR_QUEUE.offer(proceedingJoinPoint);
    }
}
