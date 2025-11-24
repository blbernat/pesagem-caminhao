package com.desafio.pesagem.service;

import com.desafio.pesagem.dto.CustoDTO;
import com.desafio.pesagem.entities.*;
import com.desafio.pesagem.repositories.*;
import com.desafio.pesagem.service.exception.ResourceNotFoundException;
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
                    .orElseThrow(()-> new ResourceNotFoundException("Caminhão não cadastrado: " + placaCaminhao));
        }
        if (nomeFilial != null) {
            filial = filialRepo.findByName(nomeFilial)
                    .orElseThrow(()-> new ResourceNotFoundException("Filial não cadastrada: " + nomeFilial));
        }
        if (nomeGrao != null) {
            tipoGrao = tipoGraoRepo.findByName(nomeGrao)
                    .orElseThrow(()-> new ResourceNotFoundException("Tipo de grão não cadastrado: " + nomeGrao));
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
                    .orElseThrow(()-> new ResourceNotFoundException("Filial não encontrada: " + nome));

            idEntidade = filial.getId();
        }
        if ("caminhao".equalsIgnoreCase(entidade)) {
            caminhao = caminhaoRepo.findByPlate(nome)
                    .orElseThrow(()-> new ResourceNotFoundException("Caminhão não encontrado: " + nome));

            idEntidade = caminhao.getId();
        }
        if ("grao".equalsIgnoreCase(entidade)) {
            tipoGrao = tipoGraoRepo.findByName(nome)
                    .orElseThrow(()-> new ResourceNotFoundException("Tipo de grão não encontrado: " + nome));

            idEntidade = tipoGrao.getId();
        }

        List<TransacaoTransporte> transacoes = this.transacaoRepo.findCusto(entidade, idEntidade);

        return this.calcularValorCusto(transacoes, entidade, nome);
    }

    public CustoDTO calcularValorCusto(List<TransacaoTransporte> transacoes, String entidade, String nome) {
        BigDecimal valor = BigDecimal.ZERO;

        for (TransacaoTransporte t : transacoes) {
            valor = valor.add(t.getCustoCarga());
        }

        return new CustoDTO(entidade, nome, valor);
    }
}
