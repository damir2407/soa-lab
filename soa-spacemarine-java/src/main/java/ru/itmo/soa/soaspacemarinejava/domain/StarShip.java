package ru.itmo.soa.soaspacemarinejava.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "starship")
public class StarShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double maxSpeed;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "spacemarine_id")
    private SpaceMarine spaceMarine;

    public StarShip() {
    }

    public StarShip(Long id, String name, Double maxSpeed, SpaceMarine spaceMarine) {
        this.id = id;
        this.name = name;
        this.maxSpeed = maxSpeed;
        this.spaceMarine = spaceMarine;
    }

    public StarShip(String name, Double maxSpeed, SpaceMarine spaceMarine) {
        this.name = name;
        this.maxSpeed = maxSpeed;
        this.spaceMarine = spaceMarine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public SpaceMarine getSpaceMarine() {
        return spaceMarine;
    }

    public void setSpaceMarine(SpaceMarine spaceMarine) {
        this.spaceMarine = spaceMarine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
