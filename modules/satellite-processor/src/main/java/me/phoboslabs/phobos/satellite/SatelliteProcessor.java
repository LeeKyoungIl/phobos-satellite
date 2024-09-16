package me.phoboslabs.phobos.satellite;


import com.google.auto.service.AutoService;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;

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
            } else if (element instanceof ExecutableElement) {
                // 메소드 레벨 annotation
                Element enclosingElement = element.getEnclosingElement();
                if (enclosingElement instanceof TypeElement) {
                    packageElement = processingEnv.getElementUtils().getPackageOf((TypeElement) enclosingElement);
                    break;
                }
            }
        }

        if (packageElement != null) {
            this.generateCollectorClass(packageElement.toString());
            this.done = true;
            return true;
        } else {
            this.messager.printMessage(Diagnostic.Kind.ERROR, "Sorry, something is wrong in packageElement process.");
            return true;
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
             import org.aspectj.lang.annotation.Around;
             import jakarta.servlet.http.HttpServletRequest;
             import org.springframework.web.context.request.RequestContextHolder;
             import org.springframework.web.context.request.ServletRequestAttributes;
             
             @Component
             @Aspect
             public class PhobosSatelliteCollectorGenerated {
             
                 public PhobosSatelliteCollectorGenerated() {
                     PhobosScanner.init();
                 }
             
                 @Pointcut("@within(me.phoboslabs.phobos.satellite.annotation.PhobosSatellite) || " +
                           "@annotation(me.phoboslabs.phobos.satellite.annotation.PhobosSatellite)")
                 public void phobosSatellitePointcut() {}
             
                 @Around("phobosSatellitePointcut()")
                 public Object scanPhobosSatellite(ProceedingJoinPoint pjp) throws Throwable {
                     HttpServletRequest request = null;
                     try {
                         ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                         if (attributes != null) {
                             request = attributes.getRequest();
                         }
                     } catch (Exception e) {
                         // Log the exception or handle it as needed
                         System.err.println("Error getting HttpServletRequest: " + e.getMessage());
                     }
                     return PhobosScanner.execute(pjp, request);
                 }
             }
            """.formatted(basePackageName);
    }
}
