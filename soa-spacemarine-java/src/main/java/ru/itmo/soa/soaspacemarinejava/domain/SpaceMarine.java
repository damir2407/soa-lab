package ru.itmo.soa.soaspacemarinejava.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "spacemarine")
public class SpaceMarine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "spacemarine"})
    private Coordinates coordinates;
    private Long health;
    private Double height;
    @Enumerated(EnumType.STRING)
    private AstartesCategory category;
    @Enumerated(EnumType.STRING)
    private Weapon weaponType;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "spacemarine"})
    private Chapter chapter;
    private String creationDate;


    public SpaceMarine() {
    }

    public SpaceMarine(String name, Coordinates coordinates, Long health, Double height, AstartesCategory category,
        Weapon weaponType, Chapter chapter, String creationDate) {
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.height = height;
        this.category = category;
        this.weaponType = weaponType;
        this.chapter = chapter;
        this.creationDate = creationDate;
    }

    public SpaceMarine(Long id, String name, Coordinates coordinates, Long health, Double height,
        AstartesCategory category,
        Weapon weaponType, Chapter chapter, String creationDate) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.height = height;
        this.category = category;
        this.weaponType = weaponType;
        this.chapter = chapter;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Long getHealth() {
        return health;
    }

    public void setHealth(Long health) {
        this.health = health;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public AstartesCategory getCategory() {
        return category;
    }

    public void setCategory(AstartesCategory category) {
        this.category = category;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Weapon weaponType) {
        this.weaponType = weaponType;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
