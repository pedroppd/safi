package br.com.safi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DarfDto {
    private boolean temDebito;
    private Double volumeCompra;
    private Double volumeVenda;
    private Double valorDebito;
    private String dataExpiracao;
    private String mes;
    private Double balanco;
}
