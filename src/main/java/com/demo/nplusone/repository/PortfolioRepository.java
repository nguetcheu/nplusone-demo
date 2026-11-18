package com.demo.nplusone.repository;

import com.demo.nplusone.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // La correction : une seule requête avec jointure
    @Query("SELECT DISTINCT p FROM Portfolio p JOIN FETCH p.positions")
    List<Portfolio> findAllWithPositions();
}
