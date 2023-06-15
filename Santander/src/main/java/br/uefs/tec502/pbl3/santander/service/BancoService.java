package br.uefs.tec502.pbl3.santander.service;

import br.uefs.tec502.pbl3.santander.dto.ContaTransferenciaDTO;
import br.uefs.tec502.pbl3.santander.dto.SaldoDTO;
import br.uefs.tec502.pbl3.santander.dto.TransferenciaDTO;
import br.uefs.tec502.pbl3.santander.enums.Banco;
import br.uefs.tec502.pbl3.santander.model.Conta;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.DoubleStream;

@Service
public class BancoService {
    private boolean possuiToken = false;
    private final RestTemplate restTemplate;
    private List<Banco> bancos = List.of(new Banco[]{Banco.BANCO_DO_BRASIL, Banco.CAIXA_ECONOMICA, Banco.ITAU});
    private Map<Integer, Boolean> semaforo = new HashMap<>();
    private List<Conta> contas = new ArrayList<>();

    public BancoService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean tokenAnel(){
        possuiToken = true;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        RestTemplate restTemplate = new RestTemplate();
        Banco nextHope = bancos.stream().filter(Banco::getAtivo).findFirst().orElse(bancos.stream().findFirst().get());
        possuiToken = false;
        String endpoint = "/banco";
        new Thread(()->{
            restTemplate.getForEntity(nextHope.getUrlBanco()+endpoint, Object.class);
        }).start();
        return true;
    }

    public String testeAnel(){
        while(!possuiToken);
        return "Estou com o token";
    }

    public String transferencia(TransferenciaDTO transferencia) {
        validaPayloadEntradaTransferencia(transferencia);

        Optional<ContaTransferenciaDTO> origemIgualDestino = transferencia.getOrigens().stream()
                .filter(origem -> Objects.equals(origem.getNumeroDaConta(), transferencia.getDestino().getNumeroDaConta())
                        && origem.getBanco().getCode().equals(transferencia.getDestino().getBanco().getCode()))
                .findFirst();

        if(origemIgualDestino.isPresent()){
            return "Não é possível transferir dinheiro para a mesma conta associada";
        }

        while (!possuiToken) ;

        Optional<Conta> contaOrigemPessoa = contas.stream()
                .filter(conta -> conta.getPessoas().stream()
                        .anyMatch(pessoa -> transferencia.getOrigens()
                                .stream().anyMatch(origem1 -> origem1.getCpf().equals(pessoa.getCpf()))))
                .findFirst();

        if (contaOrigemPessoa.isEmpty())
            return "Conta de origem não encontrada, verifique o numero da conta e a senha. Não há nenhum usuário com o cpf de origem";

        Map<ContaTransferenciaDTO, Boolean> resultadoSaques = new HashMap<>();
        //Primeiro realiza todos os saques para depois fazer o deposito na conta de destino
        for(ContaTransferenciaDTO transaferenciaOrigem: transferencia.getOrigens()){
            Optional<Conta> origem = contas.stream()
                    .filter(conta -> conta.getNumero().equals(transaferenciaOrigem.getNumeroDaConta()))
                    .findFirst();
            Boolean saque = false;
            if (transaferenciaOrigem.getBanco() == Banco.SANTANDER) {
                if(origem.isEmpty())
                    return "Uma das contas de origem é inexistente";
                saque = saque(origem.get().getNumero(), transaferenciaOrigem.getValor(), origem.get().getSenha(), false);

            } else {
                saque = saqueEmOutroBanco(transaferenciaOrigem.getNumeroDaConta(),
                        transaferenciaOrigem.getValor(),
                        transaferenciaOrigem.getSenha(),
                        transaferenciaOrigem.getBanco().getUrlBanco());
            }
            resultadoSaques.put(transaferenciaOrigem, saque);
            if (!saque) {
                break;
            }
        }

        if(resultadoSaques.containsValue(false)){
            AtomicBoolean desfazSaque = new AtomicBoolean(true);
            resultadoSaques.forEach((key, value) -> {
                if(value){
                    boolean aux = false;
                    Optional<Conta> origem = contas.stream()
                            .filter(conta -> conta.getNumero().equals(key.getNumeroDaConta()))
                            .findFirst();
                    if (key.getBanco() == Banco.SANTANDER) {
                        aux = deposito(origem.get().getNumero(), key.getValor(), false);

                    } else {
                        aux = depositoEmOutroBanco(key.getNumeroDaConta(),
                                key.getValor(),
                                key.getBanco().getUrlBanco());
                    }
                    if(desfazSaque.get() && !aux){
                        desfazSaque.set(false);
                    }
                }
            });
            if(!desfazSaque.get()){
                return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!";
            }
            return "Transferencia não realizada. Possiveis causas:" +
                    "Não há saldo suficiente, se a origem é outro banco é provavel que a conta não exista";
        } else {
            Boolean deposito = false;
            Double valorDestino = transferencia.getOrigens().stream()
                    .flatMapToDouble(origem -> DoubleStream.of(origem.getValor()))
                    .sum();
            if (transferencia.getDestino().getBanco() == Banco.SANTANDER) {
                deposito = deposito(transferencia.getDestino().getNumeroDaConta(), valorDestino, false);
            } else {
                deposito = depositoEmOutroBanco(transferencia.getDestino().getNumeroDaConta(),
                        valorDestino,
                        transferencia.getDestino().getBanco().getUrlBanco());
            }
            if (!deposito) {
                Boolean desfazSaque = false;
                for(ContaTransferenciaDTO transaferenciaOrigem: transferencia.getOrigens()){
                    Optional<Conta> origem = contas.stream()
                            .filter(conta -> conta.getNumero().equals(transaferenciaOrigem.getNumeroDaConta()))
                            .findFirst();
                    if (transaferenciaOrigem.getBanco() == Banco.SANTANDER) {
                        desfazSaque = deposito(origem.get().getNumero(), origem.get().getSaldo(), false);

                    } else {
                        desfazSaque = depositoEmOutroBanco(transaferenciaOrigem.getNumeroDaConta(),
                                transaferenciaOrigem.getValor(),
                                transaferenciaOrigem.getBanco().getUrlBanco());
                    }
                }
                if (!desfazSaque) {
                    return "Houve um erro crítico na aplicação ao realizar uma transferencia interna!";
                }
                return "Erro ao realizar transferencia: conta destino não encontrada";
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
                        (conta1.getNumero().equals(conta.getNumero())
                                && conta1.getBanco().getCode().equals(conta.getBanco().getCode()))
                ).findFirst();
        if(contaCadastrada.isPresent()){
            throw new Error("Conta já cadastrada no sistema");
        }
        conta.setId(contas.size());
        contas.add(conta);
        semaforo.put(conta.getId(), true);
        return conta;
    }

    public SaldoDTO consultarSaldo(Integer numeroConta) {
        Optional<Conta> contaResponse = contas.stream().filter(conta -> conta.getNumero().equals(numeroConta)).findFirst();
        if(contaResponse.isEmpty())
            throw new RuntimeException("Conta não encontrada");
        return new SaldoDTO(contaResponse.get().getNumero(), contaResponse.get().getSaldo());
    }

    public Boolean saque(Integer numeroConta, Double valor, String senha, boolean verifyToken) {
        Optional<Conta> contaOptional = contas.stream()
                .filter(conta -> conta.getNumero().equals(numeroConta)
                        && conta.getSenha().equals(senha))
                .findFirst();
        if (contaOptional.isEmpty())
            return false;
        while (!possuiToken && verifyToken);
        while(!semaforo.get(contaOptional.get().getId()));
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

    public Boolean deposito(Integer numeroConta, Double valor, boolean  verifyToken) {
        Optional<Conta> contaOptional = contas.stream().filter(conta -> conta.getNumero().equals(numeroConta)).findFirst();
        if (contaOptional.isEmpty())
            return false;
        while (!possuiToken && verifyToken);
        while(!semaforo.get(contaOptional.get().getId()));
        semaforo.put(contaOptional.get().getId(), false);
        contaOptional.get().setSaldo(contaOptional.get().getSaldo() + valor);
        semaforo.put(contaOptional.get().getId(), true);
        return true;
    }

    private Boolean depositoEmOutroBanco(Integer numeroConta, Double valor, String urlBanco){
        String endpoint = "banco/deposito";

        return saqueOuDepositoRequest(numeroConta, valor, urlBanco, endpoint, null);
    }

    private Boolean saqueEmOutroBanco(Integer numeroConta, Double valor, String senha, String urlBanco){
        String endpoint = "banco/saque";

        return saqueOuDepositoRequest(numeroConta, valor, urlBanco, endpoint, senha);
    }

    private Boolean saqueOuDepositoRequest(Integer numeroConta, Double valor, String urlBanco, String endpoint, String senha) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("numero_conta", numeroConta);
        map.add("valor", valor);
        if(senha != null && !senha.trim().isEmpty())
            map.add("senha", senha);


        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Boolean> response = restTemplate
                .postForEntity(urlBanco + endpoint,
                        request,
                        Boolean.class);
        return response.getStatusCode().equals(HttpStatus.OK) && Boolean.TRUE.equals(response.getBody());
    }

    private void validaPayloadEntradaTransferencia(TransferenciaDTO transferencia){
        transferencia.getOrigens().forEach(origem -> {
            Objects.requireNonNull(origem);
            Objects.requireNonNull(origem.getBanco());
            Objects.requireNonNull(origem.getNumeroDaConta());
            Objects.requireNonNull(origem.getValor());
        });

        Objects.requireNonNull(transferencia.getDestino());
        Objects.requireNonNull(transferencia.getDestino().getNumeroDaConta());
        Objects.requireNonNull(transferencia.getDestino().getBanco());
    }
}
