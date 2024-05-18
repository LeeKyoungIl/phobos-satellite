package me.phoboslabs.phobos.satellite;


import com.google.auto.service.AutoService;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.Writer;
import java.util.Optional;
import java.util.Set;

@SupportedAnnotationTypes({
    "me.phoboslabs.phobos.satellite.annotation.PhobosSatellite"
})
@AutoService(Processor.class)
public class SatelliteProcessor extends AbstractProcessor {

    private boolean done = false;
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

        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(PhobosSatellite.class);

        // 해당 요소들을 순회하며 처리
        for (Element element : annotatedElements) {
            // Element가 TypeElement인지 확인 (클래스, 인터페이스 등)
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;

                // TypeElement의 패키지 정보를 가져와 출력
                PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(typeElement);
                this.messager.printMessage(Diagnostic.Kind.NOTE, packageElement.toString());
            }
        }

        typeElements.stream().forEach(typeElement -> {
            this.messager.printMessage(Diagnostic.Kind.NOTE, processingEnv.getElementUtils().getPackageOf(typeElement).toString());
        });

        Optional<PackageElement> packageElementOpt = typeElements.stream()
            .map(typeElement -> processingEnv.getElementUtils().getPackageOf(typeElement))
            .findFirst();

        if (packageElementOpt.isEmpty()) {
            return true;
        }

        try (Writer writer = this.filer.createSourceFile("PhobosSatelliteCollectorGenerated").openWriter()) {
            if (writer != null) {
                writer.write(this.getSatelliteCollectorBodyClass(packageElementOpt.get().toString()));
                writer.close();
                this.messager.printMessage(Diagnostic.Kind.NOTE, "generated collector source code.");
            } else {
                this.messager.printMessage(Diagnostic.Kind.ERROR,
                        "Sorry, something is wrong in writer 'PhobosSatelliteCollectorGenerated.java' process.");
            }
        } catch (Exception ex) {
            this.messager.printMessage(Diagnostic.Kind.ERROR,
                    "Sorry, something is wrong in generated 'IlluminatiPointcutGenerated.java' process.");
        }

        this.done = true;
        return true;
    }

    private String getSatelliteCollectorBodyClass(String basePackageName) {
        return """
                package %s;
                 
                import me.phoboslabs.phobos.satellite.scanner.PhobosScanner;
                import org.aspectj.lang.ProceedingJoinPoint;
                import org.aspectj.lang.annotation.Aspect;
                import org.aspectj.lang.annotation.Pointcut;
                import org.springframework.stereotype.Component;
                 
                @Component
                @Aspect
                public class PhobosSatelliteCollectorGenerated {
                 
                    public PhobosSatelliteCollectorGenerated() {
                        PhobosScanner.init();
                    }
                 
                    @Pointcut("@within(me.phoboslabs.phobos.satellite.annotation.PhobosSatellite) || @annotation(me.phoboslabs.phobos.satellite.annotation.PhobosSatellite)")
                    public Object scanPhobosSatellite(ProceedingJoinPoint pjp) throws Throwable {
                        return PhobosScanner.execute(pjp);
                    }
                }
                """.formatted(basePackageName);
    }
}
