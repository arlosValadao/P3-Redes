package br.uefs.tec502.pbl3.itau.model;

import br.uefs.tec502.pbl3.itau.enums.Banco;
import br.uefs.tec502.pbl3.itau.enums.Indexador;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Banco banco;
    private Double saldo;
}
