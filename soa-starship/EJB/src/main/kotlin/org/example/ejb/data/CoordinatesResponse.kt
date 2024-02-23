package org.example.ejb.data

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@NoArgsConstructor
@AllArgsConstructor
data class CoordinatesResponse(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("x")
    val x: Float,
    @JsonProperty("y")
    val y: Int
)