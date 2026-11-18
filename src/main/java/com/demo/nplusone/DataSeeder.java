package com.demo.nplusone;

import com.demo.nplusone.model.Portfolio;
import com.demo.nplusone.model.Position;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataSeeder {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void seed(int portfolios, int positionsPerPortfolio) {
        for (int i = 0; i < portfolios; i++) {
            Portfolio portfolio = new Portfolio("Portfolio " + i);
            for (int j = 0; j < positionsPerPortfolio; j++) {
                portfolio.addPosition(new Position(BigDecimal.valueOf(100 + j)));
            }
            em.persist(portfolio);

            if (i % 500 == 0) {   // évite de saturer la mémoire
                em.flush();
                em.clear();
            }
        }
    }
}
