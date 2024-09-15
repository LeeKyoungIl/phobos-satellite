package me.phoboslabs.phobos.satellite.scanner.thread;

import jakarta.servlet.http.HttpServletRequest;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import me.phoboslabs.phobos.satellite.scanner.constant.ConstSatelliteProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.net.http.HttpRequest;

public class ScannerProcessorAsyncThread implements Runnable {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("ScannerProcessorAsyncThread is running");
                ProceedingJoinPoint proceedingJoinPoint = ConstSatelliteProcessor.getFromQueue();
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("ScannerProcessorAsyncThread was interrupted");
                break;
            } catch (Exception ex) {
                System.err.println("Error in ScannerProcessorAsyncThread: " + ex.getMessage());
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }


}
