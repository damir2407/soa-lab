package ru.itmo.soa.soaspacemarine.domain

import jakarta.persistence.Entity

@Entity
class Coordinates(
    var x: Float,
    var y: Int
) : BaseEntity<Long>()