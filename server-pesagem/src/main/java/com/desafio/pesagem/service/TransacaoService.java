package com.desafio.pesagem.service;

import com.desafio.pesagem.dto.CustoDTO;
import com.desafio.pesagem.dto.TransacaoDTO;
import com.desafio.pesagem.entities.*;
import com.desafio.pesagem.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepo;
    private final CaminhaoRepository caminhaoRepo;
    private final TipoGraoRepository tipoGraoRepo;
    private final FilialRepository filialRepo;

    public TransacaoService(TransacaoRepository transacaoRepo, CaminhaoRepository caminhaoRepo, TipoGraoRepository tipoGraoRepo, FilialRepository filialRepo) {
        this.transacaoRepo = transacaoRepo;
        this.caminhaoRepo = caminhaoRepo;
        this.tipoGraoRepo = tipoGraoRepo;
        this.filialRepo = filialRepo;
    }

    public List<TransacaoTransporte> findTransacao(String nomeFilial, String placaCaminhao, String nomeGrao) {
        Caminhao caminhao = new Caminhao();
        Filial filial = new Filial();
        TipoGrao tipoGrao = new TipoGrao();

        if (placaCaminhao != null) {
            caminhao = caminhaoRepo.findByPlate(placaCaminhao)
                    .orElseThrow(()-> new IllegalStateException("Caminhão não cadastrado: " + placaCaminhao));
        }
        if (nomeFilial != null) {
            filial = filialRepo.findByName(nomeFilial)
                    .orElseThrow(()-> new IllegalStateException("Filial não cadastrada: " + nomeFilial));
        }
        if (nomeGrao != null) {
            tipoGrao = tipoGraoRepo.findByName(nomeGrao)
                    .orElseThrow(()-> new IllegalStateException("Tipo de grão não cadastrado: " + nomeGrao));
        }

        return this.transacaoRepo.findTransacao(filial.getId(), caminhao.getId(), tipoGrao.getId());
    }

    public CustoDTO findCusto(String entidade, String nome) {
        Caminhao caminhao;
        Filial filial;
        TipoGrao tipoGrao;

        Long idEntidade = 0L;
        if ("filial".equalsIgnoreCase(entidade)) {
            filial = filialRepo.findByName(nome)
                    .orElseThrow(()-> new IllegalStateException("Filial não encontrada: " + nome));

            idEntidade = filial.getId();
        }
        if ("caminhao".equalsIgnoreCase(entidade)) {
            caminhao = caminhaoRepo.findByPlate(nome)
                    .orElseThrow(()-> new IllegalStateException("Caminhão não encontrado: " + nome));

            idEntidade = caminhao.getId();
        }
        if ("grao".equalsIgnoreCase(entidade)) {
            tipoGrao = tipoGraoRepo.findByName(nome)
                    .orElseThrow(()-> new IllegalStateException("Tipo de grão não encontrado: " + nome));

            idEntidade = tipoGrao.getId();
        }

        List<TransacaoDTO> transacoes = this.transacaoRepo.findCusto(entidade, idEntidade);

        return this.calcularValorCusto(transacoes, entidade, nome);
    }

    public CustoDTO calcularValorCusto(List<TransacaoDTO> transacoes, String entidade, String nome) {
        BigDecimal valor = BigDecimal.ZERO;

        for (TransacaoDTO t : transacoes) {
            valor = valor.add(t.custoCarga());
        }

        return new CustoDTO(entidade, nome, valor);
    }
}
