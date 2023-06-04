package br.uefs.tec502.pbl3.caixaeconomica.controller;

import br.uefs.tec502.pbl3.caixaeconomica.service.BancoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
