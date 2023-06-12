package br.uefs.tec502.pbl3.itau.service;

import br.uefs.tec502.pbl3.itau.dto.SaldoDTO;
import br.uefs.tec502.pbl3.itau.dto.TransferenciaDTO;
import br.uefs.tec502.pbl3.itau.enums.Banco;
import br.uefs.tec502.pbl3.itau.model.Conta;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BancoService {
    private boolean possuiToken = false;
    private final RestTemplate restTemplate;
    private List<Banco> bancos = List.of(new Banco[]{Banco.SANTANDER, Banco.BANCO_DO_BRASIL, Banco.CAIXA_ECONOMICA});
    private Map<Integer, Boolean> semaforo = new HashMap<>();
    private List<Conta> contas = new ArrayList<>();

    public BancoService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean tokenAnel() {
        possuiToken = true;
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
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
        new Thread(()->{
            restTemplate.getForEntity(nextHope.getUrlBanco() + endpoint, Boolean.class);
        }).start();
        return true;
    }

    public String testeAnel() {
        while (!possuiToken) ;
        return "Estou com o token";
    }

    public String transferencia(TransferenciaDTO transferencia) {
        Objects.requireNonNull(transferencia.getOrigem());
        Objects.requireNonNull(transferencia.getOrigem().getBanco());
        Objects.requireNonNull(transferencia.getOrigem().getNumeroDaConta());
        Objects.requireNonNull(transferencia.getOrigem().getValor());
        Objects.requireNonNull(transferencia.getDestino());
        Objects.requireNonNull(transferencia.getDestino().getNumeroDaConta());
        Objects.requireNonNull(transferencia.getDestino().getBanco());
        if (transferencia.getOrigem().getNumeroDaConta() == transferencia.getDestino().getNumeroDaConta()) {
            return "Não é possível transferir dinheiro para a mesma conta associada";
        }

        while (!possuiToken) ;

        Optional<Conta> origem = contas.stream()
                .filter(conta -> conta.getPessoas().stream()
                        .filter(pessoa -> pessoa.getCpf().equals(transferencia.getOrigem().getCpf()))
                        .findFirst().isPresent())
                .findFirst();

        if (origem.isEmpty())
            return "Conta de origem não encontrada, verifique o numero da conta e a senha. Não há nenhum usuário com o cpf de origem";
        if (transferencia.getOrigem().getBanco() == Banco.ITAU) {

            while (!semaforo.get(origem.get().getId()));
            Boolean saque = saque(origem.get().getNumero(), origem.get().getSaldo(), origem.get().getSenha(), false);
            if (!saque) {
                return "Transferencia realizada pois não há saldo suficiente";
            }
            if (transferencia.getDestino().getBanco() == Banco.ITAU) {
                Boolean deposito = deposito(transferencia.getDestino().getNumeroDaConta(), transferencia.getOrigem().getValor(), false);
                if (!deposito) {
                    saque = deposito(origem.get().getNumero(), origem.get().getSaldo(), false);
                    if (!saque) {
                        return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!";
                    }
                }
            } else {
                Boolean deposito = depositoEmOutroBanco(transferencia.getDestino().getNumeroDaConta(),
                        transferencia.getOrigem().getValor(),
                        transferencia.getDestino().getBanco().getUrlBanco());

                if (!deposito) {
                    Boolean desfazSaque = deposito(origem.get().getNumero(), transferencia.getOrigem().getValor(), false);
                    if (!desfazSaque) {
                        return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!";
                    }
                }
            }

        }
        else {
            Boolean saque = saqueEmOutroBanco(transferencia.getOrigem().getNumeroDaConta(),
                    transferencia.getOrigem().getValor(),
                    transferencia.getOrigem().getBanco().getUrlBanco());

            if (!saque) {
                return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!" +
                        "Possível erro: Transferencia realizada pois não há saldo suficiente.";
            }
            if (transferencia.getDestino().getBanco() == Banco.ITAU) {
                Boolean deposito = deposito(transferencia.getDestino().getNumeroDaConta(), transferencia.getOrigem().getValor(), false);
                if (!deposito) {
                    Boolean desfazSaque = depositoEmOutroBanco(transferencia.getOrigem().getNumeroDaConta(),
                            transferencia.getOrigem().getValor(), transferencia.getOrigem().getBanco().getUrlBanco());
                    if (!desfazSaque) {
                        return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!";
                    }
                    return "Houve um erro e não foi possível completar a transação." +
                            "Conta destino não encontrada";
                }
            } else {
                Boolean deposito = depositoEmOutroBanco(transferencia.getDestino().getNumeroDaConta(),
                        transferencia.getOrigem().getValor(),
                        transferencia.getDestino().getBanco().getUrlBanco());

                if (!deposito) {
                    Boolean desfazSaque = depositoEmOutroBanco(transferencia.getOrigem().getNumeroDaConta(),
                            transferencia.getOrigem().getValor(),
                            transferencia.getOrigem().getBanco().getUrlBanco());
                    if (!desfazSaque) {
                        return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!";
                    }
                    return "Houve um erro e a transação não pode ser realizada." +
                            "Conta destino não encontrada!";
                }
            }
        }
        return "transação realizada";
    }

    public Conta criarConta(Conta conta) {
        Objects.requireNonNull(conta.getNumero(), "o numero da conta é obrigatório");
        Objects.requireNonNull(conta.getIndexador(), "o indexador é obrigatório");
        Objects.requireNonNull(conta.getContaConjunta(), "é necessário informar se é uma conta conjunta");
        Objects.requireNonNull(conta.getPessoas(), "é necessário informar as pessoas associadas a esta conta");
        Objects.requireNonNull(conta.getSenha(), "é necessário informar uma senha para a conta");
        Optional<Conta> contaCadastrada = contas.stream()
                .filter(conta1 ->
                        (conta1.getNumero() == conta.getNumero()
                                && conta1.getBanco().getCode() == conta.getBanco().getCode())
                ).findFirst();
        if(contaCadastrada.isPresent()){
            throw new Error("Conta já cadastrada no sistema");
        }
        conta.setId(contas.size());
        contas.add(conta);
        semaforo.put(conta.getId(), true);
        return conta;
    }

    public SaldoDTO consultarSaldo(Integer id) {
        Conta contaResponse = contas.stream().filter(conta -> conta.getId() == id).findFirst().orElseGet(null);
        return new SaldoDTO(contaResponse.getNumero(), contaResponse.getSaldo());
    }

    public Boolean saque(Integer numeroConta, Double valor, String senha, boolean verifyToken) {
        Optional<Conta> contaOptional = contas.stream()
                .filter(conta -> conta.getNumero().equals(numeroConta)
                        && conta.getSenha().equals(senha))
                .findFirst();
        if (contaOptional.isEmpty())
            return false;
            //throw new Exception("Conta não encontrada, verifique o numero da conta e a senha");
        while (!possuiToken && verifyToken);
        while (!semaforo.get(contaOptional.get().getId()));
        semaforo.put(contaOptional.get().getId(), false);
        Double saldoAtualizado = contaOptional.get().getSaldo() - valor;
        if (saldoAtualizado < 0) {
            semaforo.put(contaOptional.get().getId(), true);
            return false;
        }
        contaOptional.get().setSaldo(saldoAtualizado);
        semaforo.put(contaOptional.get().getId(), true);
        return true;
    }

    public Boolean deposito(Integer numeroConta, Double valor, boolean verifyToken) {
        Optional<Conta> contaOptional = contas.stream().filter(conta -> Objects.equals(conta.getNumero(), numeroConta)).findFirst();
        if (contaOptional.isEmpty())
            return false;
        while (!possuiToken && verifyToken);
        while (!semaforo.get(contaOptional.get().getId()));
        semaforo.put(contaOptional.get().getId(), false);
        contaOptional.get().setSaldo(contaOptional.get().getSaldo() + valor);
        semaforo.put(contaOptional.get().getId(), true);
        return true;
    }

    private Boolean depositoEmOutroBanco(Integer numeroConta, Double valor, String urlBanco){
        String endpoint = "banco/deposito";

        return saqueOuDepositoRequest(numeroConta, valor, urlBanco, endpoint);
    }

    private Boolean saqueEmOutroBanco(Integer numeroConta, Double valor, String urlBanco){
        String endpoint = "banco/saque";

        return saqueOuDepositoRequest(numeroConta, valor, urlBanco, endpoint);
    }

    private Boolean saqueOuDepositoRequest(Integer numeroConta, Double valor, String urlBanco, String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("numero_conta", numeroConta);
        map.add("valor", valor);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Boolean> response = restTemplate
                .postForEntity(urlBanco + endpoint,
                        request,
                        Boolean.class);
        return response.getStatusCode().equals(HttpStatus.OK) && response.getBody();
    }

}
