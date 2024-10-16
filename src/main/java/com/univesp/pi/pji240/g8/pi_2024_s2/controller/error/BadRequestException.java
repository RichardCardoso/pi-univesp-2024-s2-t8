package com.univesp.pi.pji240.g8.pi_2024_s2.controller.error;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
