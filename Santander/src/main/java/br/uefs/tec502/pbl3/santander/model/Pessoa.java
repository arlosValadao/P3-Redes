package br.uefs.tec502.pbl3.santander.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Pessoa {
    private String nome;
    private String cpf;
    private LocalDate nascimento;
}
