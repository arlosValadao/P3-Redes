package com.tec502.pbl3.service;

import com.tec502.pbl3.enums.Banco;
import com.tec502.pbl3.exception.Error;
import com.tec502.pbl3.exception.NotFoundException;
import com.tec502.pbl3.model.Conta;
import com.tec502.pbl3.model.Pessoa;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ContaService {

    public static List<Conta> contas;

    public static void criarConta(Conta novaConta) throws Error {
        Optional<Conta> contaCadastrada = contas.stream()
                .filter(conta ->
                        (conta.getNumero() == novaConta.getNumero()
                                && conta.getBanco().getCode() == novaConta.getBanco().getCode())
                )
                .findFirst();
        if(contaCadastrada.isPresent()){
            throw new Error("Conta já cadastrada no sistema");
        }

        AtomicBoolean pessoaJaPossuiConta = new AtomicBoolean(false);
        contas.forEach(conta -> {
            novaConta.getPessoas().forEach(pessoaNew ->{
                Optional<Pessoa> pessoaExistente = conta.getPessoas().stream()
                        .filter(pessoa ->
                                (pessoa.getCpf().equals(pessoaNew.getCpf())
                                        && conta.getContaConjunta() == novaConta.getContaConjunta()
                                        && conta.getBanco().getCode() == novaConta.getBanco().getCode()
                                )
                        )
                        .findFirst();
                if(pessoaExistente.isPresent()){
                    pessoaJaPossuiConta.set(true);
                }
            });
        });

        if(pessoaJaPossuiConta.get()) {
            throw new Error("Essa pessoa já possui conta nesse banco");
        }


        Arrays.stream(Banco.values())
                .filter(banco -> banco.getCode() != Banco.BANCO_DO_BRASIL.getCode())
                .forEach(banco -> {

                });
        novaConta.setBanco(Banco.BANCO_DO_BRASIL);
        contas.add(novaConta);
    }

    public static Double getSaldo(Integer numeroConta) throws NotFoundException {
        Optional<Conta> contaCliente = contas.stream().filter(conta -> conta.getNumero() == numeroConta).findFirst();
        if(!contaCliente.isPresent()){
            throw new NotFoundException("Conta não encontrada");
        }
        return contaCliente.get().getSaldo();
    }
}
