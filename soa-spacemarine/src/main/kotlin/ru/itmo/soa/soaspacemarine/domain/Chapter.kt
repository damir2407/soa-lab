package ru.itmo.soa.soaspacemarine.domain

import jakarta.persistence.Entity

@Entity
class Chapter(
    var name: String,
    var parentLegion: String,
    var marinesCount: Int,
    var world: String
) : BaseEntity<Long>()