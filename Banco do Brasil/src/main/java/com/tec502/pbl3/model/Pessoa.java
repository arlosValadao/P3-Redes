package com.tec502.pbl3.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Pessoa {
    private Integer id;
    private String nome;
    private String cpf;
    private LocalDate nascimento;
}
