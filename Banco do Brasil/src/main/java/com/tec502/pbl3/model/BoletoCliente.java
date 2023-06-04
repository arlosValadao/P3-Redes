package com.tec502.pbl3.model;

import java.time.LocalDateTime;

public class BoletoCliente {
    private String contrato;
    private Integer consumo;
    private String validade;
    private String mesReferencia;

    /**
     * construtor da classe BoletoCliente
     * @param contrato
     * @param consumo
     */
    public BoletoCliente(String contrato, Integer consumo) {
        this.contrato = contrato;
        this.consumo = consumo;
        LocalDateTime dataAtual = LocalDateTime.now();
        this.validade =  dataAtual.plusDays(7).toString();
        this.mesReferencia = dataAtual.getMonth().toString();
    }
}
