package br.uefs.tec502.pbl3.caixaeconomica.controller;

import br.uefs.tec502.pbl3.caixaeconomica.dto.SaldoDTO;
import br.uefs.tec502.pbl3.caixaeconomica.dto.TransferenciaDTO;
import br.uefs.tec502.pbl3.caixaeconomica.model.Conta;
import br.uefs.tec502.pbl3.caixaeconomica.service.BancoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("conta")
public class ContaController {


    private final BancoService bancoService;

    public ContaController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    @PostMapping("transferencia")
    public String transferencia(@RequestBody TransferenciaDTO transferenciaDTO){
        return bancoService.transferencia(transferenciaDTO);
    }

    @PostMapping("deposito")
    public Boolean deposito(@RequestParam("numero_conta") Integer numeroConta, Double valor){
        return bancoService.deposito(numeroConta, valor, true);
    }

    @PostMapping("saque")
    public Boolean saque(@RequestParam("numero_conta") Integer numeroConta, String senha, Double valor){
        return bancoService.saque(numeroConta, valor, senha, true);
    }

    @PostMapping("criar-conta")
    public Conta criarConta(@RequestBody Conta conta){
        return bancoService.criarConta(conta);
    }

    @GetMapping("saldo")
    public SaldoDTO saldo(@RequestParam("numero_conta") Integer numeroConta){
        return bancoService.consultarSaldo(numeroConta);
    }
}
