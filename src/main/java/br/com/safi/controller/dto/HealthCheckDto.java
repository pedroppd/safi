package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class HealthCheckDto {

    private String status;

    private String serviceName;

    private LocalDateTime timeStamp;
}
