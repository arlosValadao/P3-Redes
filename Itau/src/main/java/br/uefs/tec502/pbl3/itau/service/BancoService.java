package br.uefs.tec502.pbl3.itau.service;

import br.uefs.tec502.pbl3.itau.enums.Banco;
import br.uefs.tec502.pbl3.itau.model.Pessoa;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BancoService {
    private boolean possuiToken = false;
    private List<Banco> bancos = List.of(new Banco[]{Banco.SANTANDER, Banco.BANCO_DO_BRASIL, Banco.CAIXA_ECONOMICA});
    private Map<Integer, Boolean> semaforo = new HashMap<>();
    private List<Pessoa> pessoas = new ArrayList<>();

    public boolean tokenAnel() {
        possuiToken = true;
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        RestTemplate restTemplate = new RestTemplate();
        /*Banco nextHope = bancos.stream().findFirst().get();
        for(Banco banco: bancos){
            if(banco.getAtivo()) {
                nextHope = banco;
                break;
            }
        }*/
        Banco nextHope = bancos.get(2);
        possuiToken = false;
        String endpoint = "/banco";
        ResponseEntity<Boolean> response = restTemplate.getForEntity(nextHope.getUrlBanco() + endpoint, Boolean.class);
        return true;
    }

    public String testeAnel() {
        while (!possuiToken) ;
        return "Estou com o token";
    }

    public String transferencia(Pessoa pessoa) {
        while (!possuiToken) ;
        if (!semaforo.get(pessoa.getId()))
            return "Já há uma requisição com esta conta sendo realizada. Aguarde um instante e tente novamente";

        if (semaforo.get(pessoa.getId()))
            semaforo.put(pessoa.getId(), false);
        Optional<Pessoa> optionalPessoa = pessoas.stream().filter(pessoa1 -> pessoa1.getId() == pessoa.getId()).findFirst();
        if (optionalPessoa.isEmpty()) {
            semaforo.put(pessoa.getId(), true);
            return "Pessoa não encontrada!";
        }
        optionalPessoa.get().setSaldo(optionalPessoa.get().getSaldo() + pessoa.getSaldo());
        semaforo.put(pessoa.getId(), true);
        return "transação realizada";
    }

    public Pessoa criarConta(Pessoa pessoa) {
        pessoa.setId(pessoas.size());
        pessoas.add(pessoa);
        semaforo.put(pessoa.getId(), true);
        return pessoa;
    }

    public Pessoa consultarSaldo(Integer id){
        return pessoas.stream().filter(pessoa -> pessoa.getId() == id).findFirst().orElseGet(null);
    }
}
