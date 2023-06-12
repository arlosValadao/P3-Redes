package br.uefs.tec502.pbl3.itau.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaldoDTO {

    @JsonProperty("numero_da_conta")
    private Integer numeroConta;

    private Double saldo;
}
