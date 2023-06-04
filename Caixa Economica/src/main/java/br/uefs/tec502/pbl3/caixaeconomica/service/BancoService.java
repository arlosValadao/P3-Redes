package br.uefs.tec502.pbl3.caixaeconomica.service;

import br.uefs.tec502.pbl3.caixaeconomica.enums.Banco;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BancoService {
    private boolean possuiToken = false;
    private List<Banco> bancos = List.of(new Banco[]{Banco.ITAU, Banco.SANTANDER, Banco.BANCO_DO_BRASIL});

    public boolean tokenAnel(){
        possuiToken = true;
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        RestTemplate restTemplate = new RestTemplate();
        Banco nextHope = bancos.stream().findFirst().get();
        for(Banco banco: bancos){
            if(banco.getAtivo()) {
                nextHope = banco;
                break;
            }
        }
        possuiToken = false;
        String endpoint = "/banco";
        ResponseEntity<Object> response = restTemplate.getForEntity(nextHope.getUrlBanco()+endpoint, Object.class);
        return true;
    }

    public String testeAnel(){
        while(!possuiToken);
        return "Estou com o token";
    }
}
