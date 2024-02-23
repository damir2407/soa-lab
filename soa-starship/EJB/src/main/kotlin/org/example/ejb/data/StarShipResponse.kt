package org.example.ejb.data

import lombok.NoArgsConstructor
import java.io.Serializable

@NoArgsConstructor
data class StarShipResponse(
    val id: Int,
    val name: String,
    val maxSpeed: Double,
    val spaceMarineId: Long,
    private val serialVersionUID: Long = -983578L
) : Serializable