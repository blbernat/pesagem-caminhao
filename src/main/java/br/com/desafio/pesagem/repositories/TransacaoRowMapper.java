package br.com.desafio.pesagem.repositories;

import br.com.desafio.pesagem.entities.Balanca;
import br.com.desafio.pesagem.entities.Caminhao;
import br.com.desafio.pesagem.entities.TipoGrao;
import br.com.desafio.pesagem.entities.TransacaoTransporte;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransacaoRowMapper implements RowMapper<TransacaoTransporte> {

    private final CaminhaoRepository caminhaoRepo;
    private final BalancaRepository balancaRepo;
    private final TipoGraoRepository tipoGraoRepo;


    public TransacaoRowMapper(CaminhaoRepository caminhaoRepo, BalancaRepository balancaRepo, TipoGraoRepository tipoGraoRepo) {
        this.caminhaoRepo = caminhaoRepo;
        this.balancaRepo = balancaRepo;
        this.tipoGraoRepo = tipoGraoRepo;
    }

    @Override
    public TransacaoTransporte mapRow(ResultSet rs, int rowNum) throws SQLException {
        TransacaoTransporte transacao = new TransacaoTransporte();
        transacao.setId(rs.getLong("id"));

        Caminhao caminhao = caminhaoRepo.findById(Long.valueOf(rs.getObject("caminhao_id").toString()))
                .orElseThrow(()-> new IllegalStateException("Caminhão não encontrado"));

        Balanca balanca = balancaRepo.findById(Long.valueOf(rs.getObject("balanca_id").toString()))
                .orElseThrow(()-> new IllegalStateException("Balanças não encontrada"));

        TipoGrao tipoGrao = tipoGraoRepo.findById(Long.valueOf(rs.getObject("tipo_grao_id").toString()))
                .orElseThrow(()-> new IllegalStateException("Tipo de grão não encontrado"));

        transacao.setCaminhao(caminhao);
        transacao.setBalanca(balanca);
        transacao.setTipoGrao(tipoGrao);

        transacao.setPesoBruto(rs.getDouble("peso_bruto"));
        transacao.setTara(rs.getDouble("tara"));
        transacao.setPesoLiquido(rs.getDouble("peso_liquido"));
        transacao.setCustoCarga(rs.getDouble("custo_carga"));
        transacao.setInicio(rs.getTimestamp("inicio").toLocalDateTime());
        transacao.setFim(rs.getTimestamp("fim").toLocalDateTime());

        return transacao;
    }
}
