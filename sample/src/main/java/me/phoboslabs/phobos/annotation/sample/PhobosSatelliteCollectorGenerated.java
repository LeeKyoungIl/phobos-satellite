package me.phoboslabs.phobos.annotation.sample;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PhobosSatelliteCollectorGenerated {

    @Pointcut("@within(me.phoboslabs.phobos.satellite.annotation.PhobosSatellite) || @annotation(me.phoboslabs.phobos.satellite.annotation.PhobosSatellite)")
    public Object scanPhobosSatellite(ProceedingJoinPoint pjp) throws Throwable {
        if (illuminatiAdaptor.checkIlluminatiIsIgnore(pjp)) {
            return pjp.proceed();
        }
        return null;
    }
}
