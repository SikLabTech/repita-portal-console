package tech.siklab.portalrpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PortalRpaApplication {

    public static void main(String[] args) {
        System.setProperty("server.port", "50080");
        System.setProperty("file.encoding", "UTF-8");
        SpringApplication.run(PortalRpaApplication.class, args);
    }
}
