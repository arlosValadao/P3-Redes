package br.uefs.tec502.pbl3.caixaeconomica.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransferenciaDTO {

    private List<ContaTransferenciaDTO> origens;
    private ContaTransferenciaDTO destino;
}
