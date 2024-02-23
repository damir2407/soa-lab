package org.example.ejb.data

import lombok.NoArgsConstructor
import java.io.Serializable

@NoArgsConstructor
data class ErrorResponse(
    val timestamp: String,
    val errorMessage: String,
    private val serialVersionUID: Long = -981579L
) : Serializable
