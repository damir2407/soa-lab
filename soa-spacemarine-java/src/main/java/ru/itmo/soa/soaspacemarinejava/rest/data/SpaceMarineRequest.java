package ru.itmo.soa.soaspacemarinejava.rest.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.itmo.soa.soaspacemarinejava.domain.AstartesCategory;
import ru.itmo.soa.soaspacemarinejava.domain.Weapon;

@Validated
public class SpaceMarineRequest {

    @NotBlank(message = "Поле name не может быть пустым!")
    private String name;

    @Valid
    @NotNull(message = "Поле coordinates не может быть пустым!")
    private CoordinatesRequest coordinates;

    @NotNull(message = "Поле health не может быть пустым!")
    @Min(value = 1, message = "Значение поля health должно быть больше 0!")
    private Long health;

    @NotNull(message = "Поле height не может быть пустым!")
    @Min(value = 1, message = "Значение поля height должно быть больше 0!")
    private Double height;

    private AstartesCategory category;

    @NotNull(message = "Поле weaponType не может быть пустым!")
    private Weapon weaponType;

    @Valid
    @NotNull(message = "Поле chapter не может быть пустым!")
    private ChapterRequest chapter;

    private Long starShipId;

    public SpaceMarineRequest() {
    }

    public SpaceMarineRequest(String name, CoordinatesRequest coordinates, Long health, Double height,
        AstartesCategory category, Weapon weaponType, ChapterRequest chapter, Long starShipId) {
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.height = height;
        this.category = category;
        this.weaponType = weaponType;
        this.chapter = chapter;
        this.starShipId = starShipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinatesRequest getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesRequest coordinates) {
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

    public ChapterRequest getChapter() {
        return chapter;
    }

    public void setChapter(ChapterRequest chapter) {
        this.chapter = chapter;
    }

    public Long getStarShipId() {
        return starShipId;
    }

    public void setStarShipId(Long starShipId) {
        this.starShipId = starShipId;
    }
}