package br.uefs.tec502.pbl3.santander.dto;

import br.uefs.tec502.pbl3.santander.enums.Banco;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContaTransferenciaDTO {

    @JsonProperty("numero_da_conta")
    private Integer numeroDaConta;
    private String cpf;
    private Banco banco;
    private Double valor;
    private String senha;
}
