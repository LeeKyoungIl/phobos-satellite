package me.phoboslabs.phobos.satellite.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-1")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
