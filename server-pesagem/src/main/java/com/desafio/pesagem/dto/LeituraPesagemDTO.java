package com.desafio.pesagem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LeituraPesagemDTO(
        String idBalanca,
        String plate,
        BigDecimal weight,
        LocalDateTime inicio
) {}
