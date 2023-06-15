package br.uefs.tec502.pbl3.bancodobrasil.controller;

import br.uefs.tec502.pbl3.bancodobrasil.service.BancoService;
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

    @PostMapping("deposito")
    public Boolean deposito(@RequestParam(name = "numero_conta") Integer numeroConta, Double valor){
        return bancoService.deposito(numeroConta, valor, false);
    }

    @PostMapping("saque")
    public Boolean saque(@RequestParam("numero_conta") Integer numeroConta, Double valor, String senha){
        return bancoService.saque(numeroConta, valor, senha, false);
    }

}
