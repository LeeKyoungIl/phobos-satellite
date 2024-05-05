package me.phoboslabs.phobos.annotation.sample;

import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PhobosSatellite
@RestController
public class TestController {

    @GetMapping("/test-1")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
