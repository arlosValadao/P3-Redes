package com.tec502.pbl3.dto;

import java.time.LocalDateTime;

public class MedidorDTO {

    private String codigoContrato;

    private Integer valorMedicao;

    private String dataHoraMedicao;

    private boolean alerta;

    /**
     * Construtor da classe MedidorDTO
     * @param codigoContrato
     * @param valorMedicao
     */
    public MedidorDTO(String codigoContrato, Integer valorMedicao) {
        this.codigoContrato = codigoContrato;
        this.valorMedicao = valorMedicao;
        this.dataHoraMedicao = LocalDateTime.now().toString();
    }

    public String getCodigoContrato() {
        return codigoContrato;
    }

    public void setCodigoContrato(String codigoContrato) {
        this.codigoContrato = codigoContrato;
    }

    public Integer getValorMedicao() {
        return valorMedicao;
    }

    public void setValorMedicao(Integer valorMedicao) {
        this.valorMedicao = valorMedicao;
    }

    public String getDataHoraMedicao() {
        return dataHoraMedicao;
    }

    public void setDataHoraMedicao(String dataHoraMedicao) {
        this.dataHoraMedicao = dataHoraMedicao;
    }

    public boolean isAlerta() {
        return alerta;
    }

    public void setAlerta(boolean alerta) {
        this.alerta = alerta;
    }
}
