package br.com.meubolso.config;

import br.com.meubolso.entity.ContaCorrenteEntity;
import br.com.meubolso.repository.ContaCorrenteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StartupLogger {

    private static final Logger logger = LogManager.getLogger(StartupLogger.class);

    @Bean
    public CommandLineRunner logContas(ContaCorrenteRepository contaCorrenteRepository) {
        return args -> {
            List<ContaCorrenteEntity> contas = contaCorrenteRepository.findAll();
            if (contas.isEmpty()) {
                logger.info("Nenhuma conta encontrada ao iniciar.");
            } else {
                logger.info("Contas correntes disponíveis para teste (id -> banco/agencia):");
                for (ContaCorrenteEntity c : contas) {
                    String banco = c.getBanco();
                    String agencia = c.getAgencia();
                    logger.info("- id={} -> {}/{}", c.getId(), banco, agencia);
                }
            }
        };
    }
}