package com.desafio.pesagem.repositories;

import com.desafio.pesagem.dto.TransacaoDTO;
import com.desafio.pesagem.entities.TransacaoTransporte;

import java.util.List;

public interface TransacaoRepository {

    Integer save(TransacaoDTO transacaoTransporte);
    List<TransacaoTransporte> findTransacao(Long filialId, Long caminhaoId, Long tipoGraoId);
    List<TransacaoDTO> findCusto(String entidade, Long idEntidade);
}
