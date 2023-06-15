package br.uefs.tec502.pbl3.bancodobrasil.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Banco {
    @JsonProperty("BB")
    BANCO_DO_BRASIL(1, "Banco do Brasil", "http://172.16.103.6", 8017),
    @JsonProperty("CEF")
    CAIXA_ECONOMICA(2, "Caixa Economica Federal", "http://172.16.103.6", 8018),
    @JsonProperty("SA")
    SANTANDER(3, "Santander", "http://172.16.103.6", 8019),
    @JsonProperty("IT")
    ITAU(4, "Itau", "http://172.16.103.6", 8020);

    private Integer code;
    private String descricao;
    private String ip;
    private Integer porta;
    private boolean ativo;

    Banco(Integer code, String descricao, String ip, Integer porta){
        this.code = code;
        this.descricao = descricao;
        this.ip = ip;
        this.porta = porta;
        this.ativo = true;
    }

    public Integer getCode() {
        return code;
    }

    public String getUrlBanco(){
        return this.ip + ":" + this.porta + "/";
    }
    public boolean getAtivo(){
        return this.ativo;
    }
}
