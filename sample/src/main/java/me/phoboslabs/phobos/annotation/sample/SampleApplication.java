package me.phoboslabs.phobos.annotation.sample;

import me.phoboslabs.phobos.satellite.annotation.PhobosSatellite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@PhobosSatellite
@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

}
