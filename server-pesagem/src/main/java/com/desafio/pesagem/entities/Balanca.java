package com.desafio.pesagem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "balanca")
public class Balanca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo_hardware", nullable = false, unique = true, length = 50)
    private String codigoHardware;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "filial_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_filial_balanca")
    )
    private Filial filial;
}

