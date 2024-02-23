package org.example.ejb.data

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.NoArgsConstructor

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
data class SpaceMarineResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("coordinates")
    val coordinates: CoordinatesResponse,
    @JsonProperty("health")
    val health: Long,
    @JsonProperty("height")
    val height: Double,
    @JsonProperty("category")
    val category: AstartesCategory? = null,
    @JsonProperty("weaponType")
    val weaponType: Weapon,
    @JsonProperty("chapter")
    val chapter: ChapterResponse,
    @JsonProperty("creationDate")
    val creationDate: String
)