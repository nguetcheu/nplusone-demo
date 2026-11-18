package com.demo.nplusone;

import com.demo.nplusone.repository.PortfolioRepository;
import com.demo.nplusone.service.PortfolioService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Un test peut détecter le N+1 : on compte les requêtes SQL.
 * Le benchmark est désactivé pour ne pas insérer 10 000 lignes pendant les tests.
 */
@SpringBootTest(properties = "benchmark.enabled=false")
class NPlusOneTest {

    @Autowired DataSeeder seeder;
    @Autowired PortfolioService service;
    @Autowired PortfolioRepository repository;
    @Autowired EntityManagerFactory emf;

    @Test
    void generatedVersionTriggersNPlusOne() {
        seeder.seed(50, 3);
        long count = repository.count();
        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();

        stats.clear();
        service.totalPortfolioValue();

        // 1 requête pour les portefeuilles + 1 par portefeuille
        assertThat(stats.getPrepareStatementCount()).isEqualTo(count + 1);
    }

    @Test
    void optimizedVersionUsesOneQuery() {
        seeder.seed(50, 3);
        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();

        stats.clear();
        service.totalPortfolioValueOptimized();

        assertThat(stats.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void bothVersionsReturnTheSameResult() {
        seeder.seed(50, 3);

        assertThat(service.totalPortfolioValue())
                .isEqualByComparingTo(service.totalPortfolioValueOptimized());
    }
}
