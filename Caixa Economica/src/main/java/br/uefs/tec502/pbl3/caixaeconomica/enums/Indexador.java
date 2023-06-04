package br.uefs.tec502.pbl3.caixaeconomica.enums;

public enum Indexador {
    PUPANCA(1, "poupanca"),
    CORRENTE(2, "corrente");

    private Integer code;
    private String descricao;

    Indexador(Integer code, String descricao){
        this.code = code;
        this.descricao = descricao;
    }
}