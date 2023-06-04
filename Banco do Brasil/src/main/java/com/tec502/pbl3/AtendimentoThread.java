package com.tec502.pbl3;

import com.google.gson.Gson;
import com.tec502.pbl3.dto.MedidorDTO;
import com.tec502.pbl3.dto.ResponseAPI;
import com.tec502.pbl3.dto.ResponseMessage;
import com.tec502.pbl3.enums.Banco;
import com.tec502.pbl3.enums.HttpStatus;
import com.tec502.pbl3.exception.Error;
import com.tec502.pbl3.exception.HTTPException;
import com.tec502.pbl3.exception.NotFoundException;
import com.tec502.pbl3.http.HttpRequest;
import com.tec502.pbl3.http.HttpResponse;
import com.tec502.pbl3.model.ConsumoCliente;
import com.tec502.pbl3.model.Conta;
import com.tec502.pbl3.model.Pessoa;
import com.tec502.pbl3.service.ConsumoService;
import com.tec502.pbl3.service.ContaService;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class AtendimentoThread extends Thread {

    private static UUID uuid;
    private static Map<String, List<MedidorDTO>> medidores = new HashMap<>();

    private Socket cliente;

    public AtendimentoThread(Socket socket) {
        this.cliente = socket;
    }

    @Override
    public void run() {
        uuid = UUID.randomUUID();
        //System.out.println(uuid);

        try {
            HttpRequest httpRequest = HttpRequest.getRequest(cliente);
            ResponseAPI responseAPI = redirectToRoute(httpRequest);

            switch (responseAPI.getStatus()) {
                case OK:
                    cliente.getOutputStream().write(HttpResponse.OK(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                case CREATED:
                    cliente.getOutputStream().write(HttpResponse.Created().getBytes("UTF-8"));
                    break;
                case BAD_REQUEST:
                    cliente.getOutputStream().write(HttpResponse.BadRequest(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                case NOT_FOUND:
                    cliente.getOutputStream().write(HttpResponse.NotFound(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                case NOT_ALLOWED:
                    cliente.getOutputStream().write(HttpResponse.NotAllowed().getBytes("UTF-8"));
                    break;
                case INTERNAL_SERVER_ERROR:
                    cliente.getOutputStream().write(HttpResponse.InternalServerError(responseAPI.getPayload()).getBytes("UTF-8"));
                    break;
                default:
                    cliente.getOutputStream().write(HttpResponse.OK(uuid.toString()).getBytes("UTF-8"));
                    break;
            }
            cliente.close();
        } catch (IOException ignored) {

        }
    }

    /**
     * Metodo para redirecionar cada requisição para a sua devida rota
     * @param httpRequest
     * @return
     */
    private synchronized static ResponseAPI redirectToRoute(HttpRequest httpRequest) {
        try {
            String payload = "";
            Gson gson = new Gson();
            switch (httpRequest.getEndpoint()) {
                case "/conta/criar":
                    Conta novaConta = gson.fromJson(httpRequest.getBody(), Conta.class);
                    ContaService.criarConta(novaConta);
                    payload = gson.toJson(new ResponseMessage("Conta cadastrada com sucesso!"));
                    return new ResponseAPI(payload, HttpStatus.CREATED);
                case "/conta/saldo":
                    Map<String, String> params = httpRequest.getParams();
                    if(!params.containsKey("numero_conta")) {
                        throw new Error("É necessário informar o numero da conta");
                    }
                    Integer numeroConta = Integer.parseInt(params.get("numero_conta"));
                    Double saldo = ContaService.getSaldo(numeroConta);
                    payload = gson.toJson(new ResponseMessage("Saldo disponivel: " + saldo));
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/conta/transferencia":
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/conta/pagamento":
                    payload = uuid.toString();
                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/banco/criar":

                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/banco/saldo":

                    return new ResponseAPI(payload, HttpStatus.OK);
                case "/banco/deposito":
                    return new ResponseAPI("{\"message\":\"Servidor socket funcionando!\"}", HttpStatus.OK);
                case "/banco/pagamento":
                    return new ResponseAPI("{\"message\":\"Servidor socket funcionando!\"}", HttpStatus.OK);
                case "/banco/transferencia":
                    return new ResponseAPI("{\"message\":\"Servidor socket funcionando!\"}", HttpStatus.OK);
                default:
                    return new ResponseAPI();
            }
        } catch (Error ex) {
            return new ResponseAPI(ex.getMessage(), HttpStatus.BAD_REQUEST);
        /*} catch (HTTPException ex) {
            return new ResponseAPI(ex.getMessage(), HttpStatus.NOT_ALLOWED);*/
        } catch (NotFoundException ex) {
            return new ResponseAPI(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseAPI(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo get de encapsulamento dos dados dos medidores de cada usuário
     * @return
     */
    public static Map<String, List<MedidorDTO>> getMedidores() {
        return medidores;
    }



}
