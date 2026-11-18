package com.demo.nplusone.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    // LAZY par défaut pour @OneToMany : c'est la source du problème N+1
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<Position> positions = new ArrayList<>();

    protected Portfolio() {}

    public Portfolio(String name) {
        this.name = name;
    }

    public void addPosition(Position position) {
        positions.add(position);
        position.setPortfolio(this);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<Position> getPositions() { return positions; }
}
