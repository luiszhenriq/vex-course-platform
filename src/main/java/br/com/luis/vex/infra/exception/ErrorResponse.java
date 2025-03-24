package br.com.luis.vex.infra.exception;

public record ErrorResponse(Long timestamp, Integer status, String error, String message, String path) {
}
