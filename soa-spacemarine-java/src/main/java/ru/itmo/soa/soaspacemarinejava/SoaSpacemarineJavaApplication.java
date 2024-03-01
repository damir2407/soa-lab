package ru.itmo.soa.soaspacemarinejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SoaSpacemarineJavaApplication {

    @GetMapping("/my-health-check")
    public String myCustomCheck() {
        return "Testing my healh check function";
    }

    public static void main(String[] args) {
        SpringApplication.run(SoaSpacemarineJavaApplication.class, args);
    }

}
