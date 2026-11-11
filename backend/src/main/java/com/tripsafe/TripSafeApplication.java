package com.tripsafe;

import com.tripsafe.service.DemoDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TripSafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripSafeApplication.class, args);
    }

    /**
     * Seeds initial demo data on startup if database is empty.
     */
    @Bean
    public CommandLineRunner initDemoData(DemoDataService demoDataService) {
        return args -> {
            demoDataService.loadDemoData();
            System.out.println("=================================================");
            System.out.println("  TripSafe Attendance Risk Projector Backend Ready ");
            System.out.println("  API Running at: http://localhost:8080/api      ");
            System.out.println("=================================================");
        };
    }
}
