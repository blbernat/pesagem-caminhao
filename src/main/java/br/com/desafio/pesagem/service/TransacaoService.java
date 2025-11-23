package br.com.desafio.pesagem.service;

import br.com.desafio.pesagem.entities.*;
import br.com.desafio.pesagem.repositories.BalancaRepository;
import br.com.desafio.pesagem.repositories.CaminhaoRepository;
import br.com.desafio.pesagem.repositories.TipoGraoRepository;
import br.com.desafio.pesagem.repositories.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepo;
    private final CaminhaoRepository caminhaoRepo;
    private final BalancaRepository balancaRepo;
    private final TipoGraoRepository tipoGraoRepo;

    public TransacaoService(TransacaoRepository transacaoRepo, CaminhaoRepository caminhaoRepo, BalancaRepository balancaRepo, TipoGraoRepository tipoGraoRepo) {
        this.transacaoRepo = transacaoRepo;
        this.caminhaoRepo = caminhaoRepo;
        this.balancaRepo = balancaRepo;
        this.tipoGraoRepo = tipoGraoRepo;
    }

    public List<TransacaoTransporte> findTransacao(String nomeFilial, String placaCaminhao, String nomeGrao) {
        Caminhao caminhao = new Caminhao();
        Filial filial = new Filial();
        Balanca balanca = new Balanca();
        TipoGrao tipoGrao = new TipoGrao();

        if (placaCaminhao != null) {
            caminhao = caminhaoRepo.findByPlate(placaCaminhao)
                    .orElseThrow(()-> new IllegalStateException("Caminhão não cadastrado: " + placaCaminhao));
        }
        if (nomeFilial != null) {
            filial = balancaRepo.findFilial(nomeFilial)
                    .orElseThrow(()-> new IllegalStateException("Filial não cadastrada: " + nomeFilial));

            if (filial.getId() != null) {
                balanca = balancaRepo.findByFilial(filial.getId())
                        .orElseThrow(()-> new IllegalStateException("Balanças da filial não encontrada"));
            }
        }
        if (nomeGrao != null) {
            tipoGrao = tipoGraoRepo.findByName(nomeGrao)
                    .orElseThrow(()-> new IllegalStateException("Tipo de grão não cadastrado: " + nomeGrao));
        }

        return this.transacaoRepo.findTransacao(balanca.getId(), caminhao.getId(), tipoGrao.getId());
    }
}
