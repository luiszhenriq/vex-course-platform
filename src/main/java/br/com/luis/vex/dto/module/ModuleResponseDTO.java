package br.com.luis.vex.dto.module;

import java.util.List;
import java.util.UUID;

public record ModuleResponseDTO(
        UUID id,

        String title,

        List<?> lessons
) {
}
