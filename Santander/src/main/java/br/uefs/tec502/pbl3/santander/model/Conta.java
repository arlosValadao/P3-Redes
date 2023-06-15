package br.uefs.tec502.pbl3.santander.model;

import br.uefs.tec502.pbl3.santander.enums.Banco;
import br.uefs.tec502.pbl3.santander.enums.Indexador;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Conta {

    private Integer id;
    private Integer numero;
    private Integer digito;
    private Indexador indexador;
    private String senha;
    @JsonProperty("conta_conjunta")
    private Boolean contaConjunta;
    private List<Pessoa> pessoas;
    @JsonIgnore
    private Banco banco;
    private Double saldo;
}
