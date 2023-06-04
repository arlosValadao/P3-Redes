package com.tec502.pbl3.service;

import com.tec502.pbl3.AtendimentoThread;
import com.tec502.pbl3.dto.MedidorDTO;
import com.tec502.pbl3.enums.HttpMethod;
import com.tec502.pbl3.exception.HTTPException;
import com.tec502.pbl3.http.HttpRequest;
import com.tec502.pbl3.model.BoletoCliente;
import com.tec502.pbl3.model.ConsumoCliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConsumoService {

    /**
     * Metodo para preparar os dados de consumo de cliente para o retorno da API
     * @param httpRequest
     * @return
     * @throws HTTPException
     * @throws Error
     */
    public synchronized static ConsumoCliente getConsumoAtual(HttpRequest httpRequest) throws HTTPException, Error {
        if(!httpRequest.getMethod().equals(HttpMethod.GET.getDescricao()))
            throw new HTTPException();
        if(!httpRequest.getParams().containsKey("contrato") || !AtendimentoThread.getMedidores().containsKey(httpRequest.getParams().get("contrato")))
            throw new Error("'contrato' not found!");
        List<MedidorDTO> medidorDTOS = AtendimentoThread.getMedidores().get(httpRequest.getParams().get("contrato"));
        int ultimaMedicao = !medidorDTOS.isEmpty()? medidorDTOS.get(medidorDTOS.size()-1).getValorMedicao() : 0;
        List<MedidorDTO> alertas = medidorDTOS.stream().filter(MedidorDTO::isAlerta).collect(Collectors.toList());
        List<String> mensagem = new ArrayList<>();
        alertas.forEach(alerta -> {
            String aux = String.format("ALERTA: Houve um aumento significativo no seu consumo de energia na medição do momento: %s", alerta.getDataHoraMedicao());
            mensagem.add(aux);
        });
        return new ConsumoCliente(ultimaMedicao, mensagem);
    }

    /**
     * Modela um historico para retorno da requisição
     * @param httpRequest
     * @return
     * @throws HTTPException
     * @throws Error
     */
    public synchronized static List<ConsumoCliente> getHistorico(HttpRequest httpRequest) throws HTTPException, Error {
        if(!httpRequest.getMethod().equals(HttpMethod.GET.getDescricao()))
            throw new HTTPException();
        if(!httpRequest.getParams().containsKey("contrato") || !AtendimentoThread.getMedidores().containsKey(httpRequest.getParams().get("contrato")))
            throw new Error("'contrato' not found!");
        List<MedidorDTO> medidorDTOS = AtendimentoThread.getMedidores().get(httpRequest.getParams().get("contrato"));
        List<ConsumoCliente> consumoClientes = new ArrayList<>();
        if (!medidorDTOS.isEmpty() && medidorDTOS != null){
            medidorDTOS.forEach(medidorDTO -> {
                consumoClientes.add(new ConsumoCliente(medidorDTO.getValorMedicao(), medidorDTO.getDataHoraMedicao()));
            });
        }
        return consumoClientes;
    }

    /**
     * Metodo para gerar boleto e preparar o retorno para a API
     * @param httpRequest
     * @return
     * @throws HTTPException
     * @throws Error
     */
    public synchronized static BoletoCliente getBoleto(HttpRequest httpRequest) throws HTTPException, Error {
        if(!httpRequest.getMethod().equals(HttpMethod.GET.getDescricao()))
            throw new HTTPException();
        Objects.requireNonNull(httpRequest.getParams());
        if(!httpRequest.getParams().containsKey("contrato") || !AtendimentoThread.getMedidores().containsKey(httpRequest.getParams().get("contrato")))
            throw new Error("contrato Not Found");
        List<MedidorDTO> medidorDTOS = AtendimentoThread.getMedidores().get(httpRequest.getParams().get("contrato"));
        Integer consumoTotal = !medidorDTOS.isEmpty()? medidorDTOS.get(medidorDTOS.size()-1).getValorMedicao() : 0;
        return new BoletoCliente(httpRequest.getParams().get("contrato"), consumoTotal);
    }
}
