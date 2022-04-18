package br.com.safi.controller;

import br.com.safi.controller.dto.HealthCheckDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/health-check")
public class HealthCheck {


    @GetMapping
    public ResponseEntity<HealthCheckDto> healthCheck() {
        var result = HealthCheckDto
                .builder().status("up")
                .serviceName("safi-finance")
                .timeStamp(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .build();
        return ResponseEntity.ok().body(result);
    }
}
