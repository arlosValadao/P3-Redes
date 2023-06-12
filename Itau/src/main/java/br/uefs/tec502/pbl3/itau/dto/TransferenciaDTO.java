package br.uefs.tec502.pbl3.itau.dto;

import lombok.Data;

@Data
public class TransferenciaDTO {

    private ContaTransferenciaDTO origem;
    private ContaTransferenciaDTO destino;
}
