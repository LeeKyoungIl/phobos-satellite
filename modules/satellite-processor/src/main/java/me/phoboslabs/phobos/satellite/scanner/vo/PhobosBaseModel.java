package me.phoboslabs.phobos.satellite.scanner.vo;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import me.phoboslabs.phobos.satellite.enumeration.PackageType;
import org.aspectj.lang.reflect.MethodSignature;

public record PhobosBaseModel(
    String uuid,
    PhobosSatellite phobosSatellite,
    PackageType packageType,
    MethodSignature methodSignature,
    Object[] args,
    long elapsedTime,
    Map<String, Object> originMethodExecuteResult,
    HttpServletRequest httpServletRequest
) {

}
