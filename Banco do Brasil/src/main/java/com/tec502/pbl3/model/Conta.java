package com.tec502.pbl3.model;

import com.tec502.pbl3.enums.Banco;
import com.tec502.pbl3.enums.Indexador;
import lombok.Data;

import java.util.List;

@Data
public class Conta {
    private Integer numero;
    private Integer digito;
    private Indexador indexador;
    private String senha;
    private Boolean contaConjunta;
    private List<Pessoa> pessoas;
    private Banco banco;
    private Double saldo;
}
