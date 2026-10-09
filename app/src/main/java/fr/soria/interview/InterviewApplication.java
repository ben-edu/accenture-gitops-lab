package fr.soria.interview;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "service", "accenture-interview-app",
            "status", "running"
        );
    }
}
