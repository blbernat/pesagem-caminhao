package com.desafio.pesagem.dto;

import java.math.BigDecimal;

public record CustoDTO(String entidade,
                       String nomeEntidade,
                       BigDecimal custo) {
}
