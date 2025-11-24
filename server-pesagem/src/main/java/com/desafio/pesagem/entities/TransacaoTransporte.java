package com.desafio.pesagem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transacao_transporte")
public class TransacaoTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "caminhao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_caminhao_transacao")
    )
    private Caminhao caminhao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tipo_grao_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tipo_grao_transacao")
    )
    private TipoGrao tipoGrao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "balanca_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_balanca_transacao")
    )
    private Balanca balanca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "filial_id",
            foreignKey = @ForeignKey(name = "fk_filial_transacao")
    )
    private Filial filial;

    @Column(name = "peso_bruto", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoBruto;

    @Column(name = "tara", nullable = false, precision = 10, scale = 2)
    private BigDecimal tara;

    @Column(name = "peso_liquido", precision = 10, scale = 2)
    private BigDecimal pesoLiquido;

    @Column(name = "custo_carga", precision = 10, scale = 2)
    private BigDecimal custoCarga;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fim")
    private LocalDateTime fim;
}
