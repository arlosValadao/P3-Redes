package com.tec502.pbl3.model;

import java.time.LocalDateTime;
import java.util.List;

public class ConsumoCliente {
    private String dataMedicao;
    private Integer valorConsumido;

    private List<String> mensagem;

    /**
     * Construtor da classe ConsumoCliente
     * @param valorConsumido
     */
    public ConsumoCliente(Integer valorConsumido) {
        this.dataMedicao = LocalDateTime.now().toString();
        this.valorConsumido = valorConsumido;
        this.mensagem = null;
    }

    /**
     * Construtor da classe ConsumoCliente
     * @param valorConsumido
     */
    public ConsumoCliente(Integer valorConsumido, String dataMedicao) {
        this.dataMedicao = dataMedicao;
        this.valorConsumido = valorConsumido;
        this.mensagem = null;
    }

    /**
     * Construtor da classe ConsumoCliente
     * @param valorConsumido
     * @param mensagem
     */
    public ConsumoCliente(Integer valorConsumido, List<String> mensagem) {
        this.dataMedicao = LocalDateTime.now().toString();
        this.valorConsumido = valorConsumido;
        this.mensagem = mensagem;
    }
}
