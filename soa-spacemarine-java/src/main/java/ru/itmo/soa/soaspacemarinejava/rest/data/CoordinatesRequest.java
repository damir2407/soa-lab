package ru.itmo.soa.soaspacemarinejava.rest.data;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class CoordinatesRequest {

    @NotNull(message = "Поле x не может быть null!")
    private Float x;
    @NotNull(message = "Поле y не может быть null!")
    private Integer y;

    public CoordinatesRequest() {
    }

    public CoordinatesRequest(Float x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}