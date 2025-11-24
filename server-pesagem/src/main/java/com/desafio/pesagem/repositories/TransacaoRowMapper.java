package com.desafio.pesagem.repositories;

import com.desafio.pesagem.entities.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransacaoRowMapper implements RowMapper<TransacaoTransporte> {

    private final CaminhaoRepository caminhaoRepo;
    private final BalancaRepository balancaRepo;
    private final TipoGraoRepository tipoGraoRepo;
    private final FilialRepository filialRepo;

    public TransacaoRowMapper(CaminhaoRepository caminhaoRepo, BalancaRepository balancaRepo, TipoGraoRepository tipoGraoRepo, FilialRepository filiapRepo) {
        this.caminhaoRepo = caminhaoRepo;
        this.balancaRepo = balancaRepo;
        this.tipoGraoRepo = tipoGraoRepo;
        this.filialRepo = filiapRepo;
    }

    @Override
    public TransacaoTransporte mapRow(ResultSet rs, int rowNum) throws SQLException {
        TransacaoTransporte transacao = new TransacaoTransporte();

        Caminhao caminhao = caminhaoRepo.findById(Long.valueOf(rs.getObject("caminhao_id").toString()))
                .orElseThrow(()-> new IllegalStateException("Caminhão não encontrado"));

        Balanca balanca = balancaRepo.findById(Long.valueOf(rs.getObject("balanca_id").toString()))
                .orElseThrow(()-> new IllegalStateException("Balanças não encontrada"));

        Filial filial = filialRepo.findById(Long.valueOf(rs.getObject("filial_id").toString()))
                    .orElseThrow(()-> new IllegalStateException("Filial não encontrada"));

        TipoGrao tipoGrao = tipoGraoRepo.findById(Long.valueOf(rs.getObject("tipo_grao_id").toString()))
                .orElseThrow(()-> new IllegalStateException("Tipo de grão não encontrado"));

        transacao.setId(rs.getLong("id"));
        transacao.setCaminhao(caminhao);
        transacao.setBalanca(balanca);
        transacao.setTipoGrao(tipoGrao);
        transacao.setFilial(filial);

        transacao.setPesoBruto(rs.getBigDecimal("peso_bruto"));
        transacao.setTara(rs.getBigDecimal("tara"));
        transacao.setPesoLiquido(rs.getBigDecimal("peso_liquido"));
        transacao.setCustoCarga(rs.getBigDecimal("custo_carga"));
        transacao.setInicio(rs.getTimestamp("inicio").toLocalDateTime());
        transacao.setFim(rs.getTimestamp("fim").toLocalDateTime());

        return transacao;
    }
}
