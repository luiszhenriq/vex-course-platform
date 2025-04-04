package br.com.luis.vex.dto.payment;

import java.util.UUID;

public record Product(UUID externalId, String id, Integer quantity) {
}
