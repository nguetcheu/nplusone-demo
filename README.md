# Démo N+1 (Spring Boot + H2)

Prérequis : Java 17+ et Maven (ou ouvre le dossier dans IntelliJ / VS Code).

## Lancer la mesure
    mvn spring-boot:run

Tu verras dans la console :

    AVANT (code généré, N+1)  -> requêtes SQL : 10 001 | temps : ... ms
    APRÈS (JOIN FETCH)        -> requêtes SQL : 1      | temps : ... ms

## Lancer les tests
    mvn test

## Voir les requêtes N+1 en direct
Dans application.properties : spring.jpa.show-sql=true et benchmark.portfolios=5
