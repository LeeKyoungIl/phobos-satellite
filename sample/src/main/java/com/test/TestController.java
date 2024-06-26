package com.test;

import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PhobosSatellite
@RestController
public class TestController {

    @GetMapping("/test-1")
    public ResponseEntity<String> test(
        @RequestParam(value = "test", required = false) String test
    ) {
        return ResponseEntity.ok("test");
    }
}
