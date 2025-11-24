package com.desafio.pesagem.repositories;

import com.desafio.pesagem.entities.Balanca;

import java.util.Optional;

public interface BalancaRepository {
    Optional<Balanca> findByCodigoHardware(String idBalanca);
    Optional<Balanca> findByFilial(Long idFilial);
    Optional<Balanca> findById(Long id);
}
