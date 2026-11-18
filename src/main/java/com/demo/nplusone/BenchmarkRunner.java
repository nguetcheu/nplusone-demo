package com.demo.nplusone;

import com.demo.nplusone.service.PortfolioService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Component
@ConditionalOnProperty(name = "benchmark.enabled", havingValue = "true", matchIfMissing = true)
public class BenchmarkRunner implements CommandLineRunner {

    private final DataSeeder seeder;
    private final PortfolioService service;
    private final Statistics statistics;

    @Value("${benchmark.portfolios}")
    private int portfolios;

    @Value("${benchmark.positions-per-portfolio}")
    private int positionsPerPortfolio;

    public BenchmarkRunner(DataSeeder seeder, PortfolioService service, EntityManagerFactory emf) {
        this.seeder = seeder;
        this.service = service;
        this.statistics = emf.unwrap(SessionFactory.class).getStatistics();
    }

    @Override
    public void run(String... args) {
        System.out.printf("%nInsertion de %d portefeuilles (x %d positions)...%n",
                portfolios, positionsPerPortfolio);
        seeder.seed(portfolios, positionsPerPortfolio);

        // Échauffement de la JVM (JIT) pour ne pas fausser les temps
        service.totalPortfolioValue();
        service.totalPortfolioValueOptimized();

        System.out.println("\n================ RÉSULTATS ================");
        measure("AVANT (code généré, N+1)", service::totalPortfolioValue);
        measure("APRÈS (JOIN FETCH)      ", service::totalPortfolioValueOptimized);
        System.out.println("===========================================\n");
    }

    private void measure(String label, Supplier<BigDecimal> action) {
        statistics.clear();
        long start = System.nanoTime();
        BigDecimal result = action.get();
        long ms = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("%s -> requêtes SQL : %,d | temps : %,d ms | total = %s%n",
                label, statistics.getPrepareStatementCount(), ms, result);
    }
}
