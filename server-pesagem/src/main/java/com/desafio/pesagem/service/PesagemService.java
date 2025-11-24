package com.desafio.pesagem.service;

import com.desafio.pesagem.dto.LeituraPesagemDTO;
import com.desafio.pesagem.dto.TransacaoDTO;
import com.desafio.pesagem.entities.Balanca;
import com.desafio.pesagem.entities.Caminhao;
import com.desafio.pesagem.entities.TipoGrao;
import com.desafio.pesagem.repositories.BalancaRepository;
import com.desafio.pesagem.repositories.CaminhaoRepository;
import com.desafio.pesagem.repositories.TipoGraoRepository;
import com.desafio.pesagem.repositories.TransacaoRepository;
import com.desafio.pesagem.service.exception.CreateTransactionException;
import com.desafio.pesagem.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PesagemService {

    private final CaminhaoRepository caminhaoRepo;
    private final BalancaRepository balancaRepo;
    private final TipoGraoRepository tipoGraoRepo;
    private final TransacaoRepository transacaoRepo;

    public PesagemService(CaminhaoRepository caminhaoRepo,
                          BalancaRepository balancaRepo,
                          TipoGraoRepository tipoGraoRepo,
                          TransacaoRepository transacaoRepo) {
        this.caminhaoRepo = caminhaoRepo;
        this.balancaRepo = balancaRepo;
        this.tipoGraoRepo = tipoGraoRepo;
        this.transacaoRepo = transacaoRepo;
    }

    public void salvarPesagemEstabilizada(LeituraPesagemDTO dto) {
        Caminhao caminhao = caminhaoRepo.findByPlate(dto.plate())
                .orElseThrow(()-> new ResourceNotFoundException("Caminhão não cadastrado: " + dto.plate()));

        Balanca balanca = balancaRepo.findByCodigoHardware(dto.idBalanca())
                .orElseThrow(()-> new ResourceNotFoundException("Balança não cadastrada: " + dto.idBalanca()));

        // aqui se busca o primeiro tipo de grão cadastrado
        TipoGrao tipoGrao = tipoGraoRepo.findAny()
                .orElseThrow(()-> new ResourceNotFoundException("Tipo de grão não cadastrado"));

        BigDecimal tara = caminhao.getTara();
        BigDecimal pesoBruto = dto.weight();
        BigDecimal pesoLiquido = pesoBruto.subtract(tara);
        BigDecimal custo = pesoLiquido.multiply(tipoGrao.getPrecoTon());
        TransacaoDTO transacaoDTO = new TransacaoDTO(caminhao, tipoGrao, balanca, balanca.getFilial(), pesoBruto, tara, pesoLiquido, custo, dto.inicio());
        Integer save = transacaoRepo.save(transacaoDTO);
        if (save != 1) {
            throw new CreateTransactionException("Houve um erro ao criar transação!");
        }
    }
}
