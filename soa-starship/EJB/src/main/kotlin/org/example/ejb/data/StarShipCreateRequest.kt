package org.example.ejb.data

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable


@Data
@NoArgsConstructor
@AllArgsConstructor
open class StarShipCreateRequest(
    val name: String? = "",
    val maxSpeed: Double = 0.0,
    private val serialVersionUID: Long = -932311L
) : Serializable
