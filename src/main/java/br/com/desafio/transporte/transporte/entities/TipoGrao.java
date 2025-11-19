package br.com.desafio.transporte.transporte.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TipoGrao {
    private Long id;
    private String nome;
    private Double precoTon;
}
