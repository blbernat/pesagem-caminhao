package com.desafio.pesagem.repositories;

import com.desafio.pesagem.entities.Caminhao;
import java.util.Optional;

public interface CaminhaoRepository {

    Optional<Caminhao> findByPlate(String placa);
    Optional<Caminhao> findById(Long id);

}
