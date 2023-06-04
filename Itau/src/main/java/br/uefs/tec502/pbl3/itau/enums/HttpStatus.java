package br.uefs.tec502.pbl3.itau.enums;

public enum HttpStatus {

    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private Integer code;
    HttpStatus(Integer code) {
        this.code = code;
    }
}
