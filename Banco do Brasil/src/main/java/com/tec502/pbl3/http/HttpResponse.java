package com.tec502.pbl3.http;

public class HttpResponse {

    /**
     * retorno do status 200 para as requisições
     * @param payload
     * @return
     */
    public static String OK (String payload) {
        return String.format("HTTP/1.1 200 OK\r\n\r\n%s", payload);
    }

    /**
     * retorno do status 400 das requisições
     * @param payload
     * @return
     */
    public static String BadRequest (String payload) {
        return String.format("HTTP/1.1 400 Bad Request\r\n\r\n%s", payload);
    }

    public static String NotFound (String payload) {
        return String.format("HTTP/1.1 404 Not Found\r\n\r\n%s", payload);
    }

    /**
     * retorno do status 500 das requisições
     * @param payload
     * @return
     */
    public static String InternalServerError (String payload) {
        return String.format("HTTP/1.1 500 Internal Server Error\r\n\r\n%s", payload);
    }

    /**
     * retorno do status 201 das requisições
     * @return
     */
    public static String Created () {
        return "HTTP/1.1 201 Created\r\n\r\n";
    }

    /**
     * retorno do status 200 das requisições
     * @return
     */
    public static String OK () {
        return "HTTP/1.1 200 OK\r\n\r\n";
    }

    /**
     * retorno do status 400 das requisições
     * @return
     */
    public static String BadRequest () {
        return "HTTP/1.1 400 Bad Request\r\n\r\n";
    }

    /**
     * retorno do status 404 das requisições
     * @return
     */
    public static String NotFound () {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }

    /**
     * retorno do status 500 das requisições
     * @return
     */
    public static String InternalServerError () {
        return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
    }

    /**
     * retorno do status 405 das requisições
     * @return
     */
    public static String NotAllowed () {
        return "HTTP/1.1 405 Not Allowed\r\n\r\n";
    }
}
