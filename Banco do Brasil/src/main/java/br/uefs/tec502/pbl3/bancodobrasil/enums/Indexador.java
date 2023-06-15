package br.uefs.tec502.pbl3.bancodobrasil.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Indexador {
    @JsonProperty("poupanca")
    PUPANCA(1, "poupanca"),
    @JsonProperty("corrente")
    CORRENTE(2, "corrente");

    private Integer code;
    private String descricao;

    Indexador(Integer code, String descricao){
        this.code = code;
        this.descricao = descricao;
    }
}
