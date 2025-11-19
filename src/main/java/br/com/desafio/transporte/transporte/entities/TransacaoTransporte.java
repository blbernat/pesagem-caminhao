package br.com.desafio.transporte.transporte.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransacaoTransporte {
    private Long id;
    private Caminhao caminhao;
    private TipoGrao tipoGrao;
    private Balanca balanca;

    private Double pesoBruto;
    private Double pesoLiquido;
    //private Double tara;
    private Double custoCarga;

    private LocalDateTime inicio;
    private LocalDateTime fim;
}
