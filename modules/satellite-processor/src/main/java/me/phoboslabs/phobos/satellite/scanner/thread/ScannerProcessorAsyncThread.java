package me.phoboslabs.phobos.satellite.scanner.thread;

import me.phoboslabs.phobos.satellite.scanner.constant.ConstSatelliteProcessor;
import me.phoboslabs.phobos.satellite.scanner.vo.PhobosBaseModel;

public class ScannerProcessorAsyncThread implements Runnable {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                PhobosBaseModel phobosBaseModel = ConstSatelliteProcessor.getFromQueue();
                System.out.println("phobosBaseModel id: " + phobosBaseModel.uuid());
                System.out.println("ScannerProcessorAsyncThread is running");
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
