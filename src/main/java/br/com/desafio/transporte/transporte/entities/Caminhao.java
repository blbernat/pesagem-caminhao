package br.com.desafio.transporte.transporte.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Caminhao {
    private Long id;
    private String placa;
    private Double tara;
}
