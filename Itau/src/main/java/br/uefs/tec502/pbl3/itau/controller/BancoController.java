package br.uefs.tec502.pbl3.itau.controller;

import br.uefs.tec502.pbl3.itau.dto.SaldoDTO;
import br.uefs.tec502.pbl3.itau.dto.TransferenciaDTO;
import br.uefs.tec502.pbl3.itau.model.Conta;
import br.uefs.tec502.pbl3.itau.model.Pessoa;
import br.uefs.tec502.pbl3.itau.service.BancoService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("banco")
public class BancoController {

    private final BancoService bancoService;

    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    @GetMapping()
    public boolean tokenAnel(){
        return bancoService.tokenAnel();
    }

    @GetMapping("teste-token")
    public String testandoAnel(){
        return bancoService.testeAnel();
    }

    @PostMapping("transferencia")
    public String transferencia(@RequestBody TransferenciaDTO transferenciaDTO){
        return bancoService.transferencia(transferenciaDTO);
    }

    @PostMapping("deposito")
    public Boolean deposito(@RequestParam(name = "numero_conta") Integer numeroConta, Double valor){
        return bancoService.deposito(numeroConta, valor, false);
    }

    @PostMapping("saque")
    public Boolean saque(@RequestParam("numero_conta") Integer numeroConta, Double valor, String senha){
        return bancoService.saque(numeroConta, valor, senha, false);
    }

    @PostMapping("criar-conta")
    public Conta criarConta(@RequestBody Conta conta){
        return bancoService.criarConta(conta);
    }

}
