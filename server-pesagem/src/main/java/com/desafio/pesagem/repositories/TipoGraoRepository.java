package com.desafio.pesagem.repositories;

import com.desafio.pesagem.entities.TipoGrao;

import java.util.Optional;

public interface TipoGraoRepository {

    Optional<TipoGrao> findAny();
    Optional<TipoGrao> findByName(String nome);
    Optional<TipoGrao> findById(Long id);
}
