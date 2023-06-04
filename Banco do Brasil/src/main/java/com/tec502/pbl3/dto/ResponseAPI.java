package com.tec502.pbl3.dto;

import com.tec502.pbl3.enums.HttpStatus;

public class ResponseAPI {
    private String payload;
    private HttpStatus status;

    public ResponseAPI() {
        this.payload = "Not Found!";
        this.status = HttpStatus.NOT_FOUND;
    }

    public ResponseAPI(String payload, HttpStatus status) {
        this.payload = payload;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getPayload() {
        return payload;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
