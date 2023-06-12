package br.uefs.tec502.pbl3.caixaeconomica.dto;

import lombok.Data;

@Data
public class TransferenciaDTO {

    private ContaTransferenciaDTO origem;
    private ContaTransferenciaDTO destino;
}
