package com.desafio.pesagem.repositories;

import com.desafio.pesagem.entities.Balanca;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class BalancaRepositoryImp implements BalancaRepository {
    private final JdbcClient jdbcClient;
    private final FilialRepository filialRepo;

    public BalancaRepositoryImp(JdbcClient jdbcClient, FilialRepository filialRepo) {
        this.jdbcClient = jdbcClient;
        this.filialRepo = filialRepo;
    }

    @Override
    public Optional<Balanca> findByCodigoHardware(String idBalanca) {
        Optional<Balanca> balancaOpt = this.jdbcClient.sql("SELECT * FROM balanca where codigo_hardware = :idBalanca")
                .param("idBalanca", idBalanca)
                .query(Balanca.class)
                .optional();

        // if present, load filial separately and set it on the Balanca
        if (balancaOpt.isPresent()) {
            Balanca balanca = balancaOpt.get();
            Optional<Long> filialIdOpt = this.jdbcClient.sql("SELECT filial_id FROM balanca where codigo_hardware = :idBalanca")
                    .param("idBalanca", idBalanca)
                    .query(Long.class)
                    .optional();

            filialIdOpt.flatMap(filialRepo::findById).ifPresent(balanca::setFilial);
        }

        return balancaOpt;
    }

    @Override
    public Optional<Balanca> findByFilial(Long idFilial) {
        return this.jdbcClient.sql("SELECT * FROM balanca where filial_id = :idFilial")
                .param("idFilial", idFilial)
                .query(Balanca.class)
                .optional();
    }

    @Override
    public Optional<Balanca> findById(Long id) {
        return this.jdbcClient.sql("SELECT * FROM balanca where id = :id")
                .param("id", id)
                .query(Balanca.class)
                .optional();
    }
}
