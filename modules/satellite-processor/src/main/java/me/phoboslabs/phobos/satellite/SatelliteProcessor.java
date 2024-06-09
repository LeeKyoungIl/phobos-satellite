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

    @SuppressWarnings(value = {"java:S3516"})
    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        if (this.done) {
            return true;
        } else {
            return processorGenerated(roundEnvironment);
        }
    }

    private boolean processorGenerated(RoundEnvironment roundEnvironment) {
        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(PhobosSatellite.class);

        PackageElement packageElement = null;
        for (Element element : annotatedElements) {
            if (element instanceof TypeElement typeElement) {
                packageElement = processingEnv.getElementUtils().getPackageOf(typeElement);
                break;
            }
        }

        if (packageElement != null) {
            this.generateCollectorClass(packageElement.toString());
            this.done = true;
            return true;
        } else {
            this.messager.printMessage(Diagnostic.Kind.ERROR, "Sorry, something is wrong in packageElement process.");
            return false;
        }
    }

    private void generateCollectorClass(String className) {
        try (Writer writer = this.filer.createSourceFile("PhobosSatelliteCollectorGenerated").openWriter()) {
            if (writer != null) {
                writer.write(this.getSatelliteCollectorBodyClass(className));
                this.messager.printMessage(Diagnostic.Kind.NOTE, "generated collector source code.");
            } else {
                this.messager.printMessage(Diagnostic.Kind.ERROR,
                        "Sorry, something is wrong in writer 'PhobosSatelliteCollectorGenerated.java' process.");
            }
        } catch (Exception ex) {
            this.messager.printMessage(Diagnostic.Kind.ERROR,
                    "Sorry, something is wrong in generated 'IlluminatiPointcutGenerated.java' process.");
        }
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
