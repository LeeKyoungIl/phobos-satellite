package me.phoboslabs.phobos.satellite;


import com.google.auto.service.AutoService;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Optional;
import java.util.Set;

@SupportedAnnotationTypes({
    "me.phoboslabs.phobos.satellite.annotation.PhobosSatellite"
})
@AutoService(Processor.class)
public class SatelliteProcessor extends AbstractProcessor {

    private boolean done = false;
    private final String bannerText = """
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
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(PhobosSatellite.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        if (this.done) {
            return true;
        }

        Optional<PackageElement> packageElementOpt = typeElements.stream()
            .map(typeElement -> processingEnv.getElementUtils().getPackageOf(typeElement))
            .findFirst();

        if (packageElementOpt.isEmpty()) {
            return true;
        }

        this.messager.printMessage(Diagnostic.Kind.NOTE, "Hello, Activate Phobos Satellite Completed!\n" + this.bannerText);

        this.done = true;
        return true;
    }

    private String getSatelliteCollectorBodyClass(String basePackageName) {
        return """
            package %s;

            public class SatelliteCollectorBody {
                public SatelliteCollectorBody() {
                    System.out.println("SatelliteCollectorBody is created.");
                }
            }
            """.formatted(basePackageName);
    }
}
