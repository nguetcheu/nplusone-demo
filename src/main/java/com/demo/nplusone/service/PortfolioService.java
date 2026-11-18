package com.demo.nplusone.service;

import com.demo.nplusone.model.Portfolio;
import com.demo.nplusone.model.Position;
import com.demo.nplusone.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /** Version "générée par l'IA" : propre, lisible... et N+1. */
    @Transactional(readOnly = true)
    public BigDecimal totalPortfolioValue() {
        List<Portfolio> portfolios = portfolioRepository.findAll();

        return portfolios.stream()
                .flatMap(p -> p.getPositions().stream())
                .map(Position::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Version corrigée : même résultat, une seule requête. */
    @Transactional(readOnly = true)
    public BigDecimal totalPortfolioValueOptimized() {
        List<Portfolio> portfolios = portfolioRepository.findAllWithPositions();

        return portfolios.stream()
                .flatMap(p -> p.getPositions().stream())
                .map(Position::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
