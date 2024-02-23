package ru.itmo.soa.soaspacemarine.rest.data

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import ru.itmo.soa.soaspacemarine.domain.AstartesCategory
import ru.itmo.soa.soaspacemarine.domain.Weapon

@Validated
data class SpaceMarineRequest(
    @field:NotBlank(message = "Поле name не может быть пустым!")
    val name: String,
    @field:Valid
    val coordinates: CoordinatesRequest,
    @field:Min(value = 1, message = "Значение поля health должно быть больше 0!")
    val health: Long,
    @field:Min(value = 1, message = "Значение поля height должно быть больше 0!")
    val height: Double,
    val category: AstartesCategory?,
    val weaponType: Weapon,
    @field:Valid
    val chapter: ChapterRequest,
    val starShipId: Long?
)

@Validated
data class CoordinatesRequest(
    val x: Float,
    val y: Int
)

@Validated
data class ChapterRequest(
    @field:NotBlank(message = "Поле name не может быть пустым!")
    val name: String,
    val parentLegion: String,
    @field:Min(value = 1, message = "Значение поля marinesCount должно быть больше 0!")
    @field:Max(value = 1000, message = "Значение поля marinesCount не должно быть больше 1000!")
    val marinesCount: Int,
    val world: String
)
