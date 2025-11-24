package com.desafio.pesagem.dto;

import java.math.BigDecimal;

public record EnvioPesagemDTO(
        String id,
        String plate,
        BigDecimal weight
) {}
