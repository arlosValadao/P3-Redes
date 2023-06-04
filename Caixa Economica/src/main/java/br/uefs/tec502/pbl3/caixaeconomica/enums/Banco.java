package br.uefs.tec502.pbl3.caixaeconomica.enums;

public enum Banco {
    BANCO_DO_BRASIL(1, "Banco do Brasil", "http://localhost", 8017),
    CAIXA_ECONOMICA(2, "Caixa Economica Federal", "http://localhost", 8018),
    SANTANDER(3, "Santander", "http://localhost", 8019),
    ITAU(4, "Itau", "http://localhost", 8020);

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
        return this.ip + ":" + this.porta;
    }
    public boolean getAtivo(){
        return this.ativo;
    }
}
