package com.sber.java13.ecoteam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcoTeamApplication implements CommandLineRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(EcoTeamApplication.class, args);
    }
    
    @Override
    public void run(String... args) {
        System.out.println("Ура! Заработало!");
    }
}
