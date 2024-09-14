package me.phoboslabs.phobos.satellite.scanner.thread;

public class ScannerProcessorAsyncThread implements Runnable {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("ScannerProcessorAsyncThread is running");
                Thread.sleep(3_000L);
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
