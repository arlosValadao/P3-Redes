package com.tec502.pbl3.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    public HttpRequest(){
        this.getParams = new HashMap<>();
        this.headers = new HashMap<>();
    }

    private String method;
    private String endpoint;
    private Map<String, String> headers;
    private Map<String, String> getParams;
    private String body;

    private String host;

    /**
     * Metodo para tratamento de dados de entrada no servidor pra moldar as requisições na API
     *
     * @param cliente
     * @return
     * @throws IOException
     */
    public static HttpRequest getRequest(Socket cliente) throws IOException {

        HttpRequest httpRequest = new HttpRequest();
        InputStreamReader isr = new InputStreamReader(cliente.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        String line = null;

        if(reader.ready())
            line = reader.readLine();

        if(line == null)
            line = reader.readLine();

        if(line != null && line.contains("HTTP")) {
            String[] lineOne = line.split(" ");
            httpRequest.method = lineOne[0];
            String[] endpointAux = lineOne[1].split("\\?");
            httpRequest.endpoint = endpointAux[0];
            for (int i = 1; i < endpointAux.length; i++) {
                String[] param = endpointAux[i].split("=");
                httpRequest.getParams.put(param[0], param[1]);
            }

            httpRequest.host = reader.readLine();
            StringBuilder builder = new StringBuilder();

            while (reader.ready()) {
                builder.append((char) reader.read());
            }

            String body = builder.toString();
            body = body.replace("\n", "");
            body = body.replace("\t", "");

            httpRequest.body = body;
        }
        return httpRequest;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return getParams;
    }

    public void setParams(Map<String, String> params) {
        this.getParams = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
