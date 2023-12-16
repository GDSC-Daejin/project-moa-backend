package com.gdsc.moa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(servers = {@Server(url = "https://moa-backend.kro.kr", description = "Moa 프로젝트 API 명세서")})
@SpringBootApplication
@EnableScheduling
public class ProjectMoaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectMoaApplication.class, args);
    }

}
