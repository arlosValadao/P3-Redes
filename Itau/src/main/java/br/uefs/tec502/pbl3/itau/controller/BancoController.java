package br.uefs.tec502.pbl3.itau.controller;

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
    public String transferencia(@RequestBody Pessoa pessoa){
        return bancoService.transferencia(pessoa);
    }

    @PostMapping("criar-conta")
    public Pessoa criarConta(@RequestBody Pessoa pessoa){
        return bancoService.criarConta(pessoa);
    }

    @GetMapping("saldo")
    public Pessoa saldo(@RequestParam("id") Integer id){
        return bancoService.consultarSaldo(id);
    }
}
