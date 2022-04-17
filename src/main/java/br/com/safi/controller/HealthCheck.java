package br.com.safi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/health-check")
public class HealthCheck {


    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok().body("up");
    }
}
