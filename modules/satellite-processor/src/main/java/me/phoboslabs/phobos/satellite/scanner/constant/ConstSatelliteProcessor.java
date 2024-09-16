package me.phoboslabs.phobos.satellite.scanner.constant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import me.phoboslabs.phobos.satellite.scanner.vo.PhobosBaseModel;

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

    private static final BlockingQueue<PhobosBaseModel> SCANNER_PROCESSOR_QUEUE = new LinkedBlockingQueue<>(1000);

    private ConstSatelliteProcessor() {
    }

    public static Boolean addToQueue(PhobosBaseModel phobosBaseModel) {
        return SCANNER_PROCESSOR_QUEUE.offer(phobosBaseModel);
    }

    public static PhobosBaseModel getFromQueue() throws InterruptedException {
        return SCANNER_PROCESSOR_QUEUE.take();
    }
}
