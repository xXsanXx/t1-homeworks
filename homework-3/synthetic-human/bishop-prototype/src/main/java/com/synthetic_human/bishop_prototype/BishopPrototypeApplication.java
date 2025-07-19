package com.synthetic_human.bishop_prototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.synthetic_human.core_starter",
        "com.synthetic_human.bishop_prototype"
})
public class BishopPrototypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BishopPrototypeApplication.class, args);
    }

}
