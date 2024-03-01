package ru.itmo.soa.soaspacemarinejava.rest.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class ChapterRequest {

    @NotBlank(message = "Поле name не может быть пустым!")
    private String name;

    @NotNull(message = "Поле parentLegion не может быть null!")
    private String parentLegion;

    @NotNull(message = "Поле marinesCount не может быть null!")
    @Min(value = 1, message = "Значение поля marinesCount должно быть больше 0!")
    @Max(value = 1000, message = "Значение поля marinesCount не должно быть больше 1000!")
    private Integer marinesCount;

    @NotNull(message = "Поле world не может быть null!")
    private String world;

    public ChapterRequest(String name, String parentLegion, Integer marinesCount, String world) {
        this.name = name;
        this.parentLegion = parentLegion;
        this.marinesCount = marinesCount;
        this.world = world;
    }

    public ChapterRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentLegion() {
        return parentLegion;
    }

    public void setParentLegion(String parentLegion) {
        this.parentLegion = parentLegion;
    }

    public Integer getMarinesCount() {
        return marinesCount;
    }

    public void setMarinesCount(Integer marinesCount) {
        this.marinesCount = marinesCount;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}