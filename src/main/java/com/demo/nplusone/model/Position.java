package com.demo.nplusone.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // "value" est un mot réservé dans H2, on nomme donc la colonne autrement
    @Column(name = "position_value")
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    protected Position() {}

    public Position(BigDecimal value) {
        this.value = value;
    }

    public Long getId() { return id; }
    public BigDecimal getValue() { return value; }
    public Portfolio getPortfolio() { return portfolio; }
    void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
}
