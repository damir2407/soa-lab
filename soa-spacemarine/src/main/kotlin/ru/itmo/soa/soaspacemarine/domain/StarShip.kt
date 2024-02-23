package ru.itmo.soa.soaspacemarine.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "starship")
class StarShip(
    var name: String,
    var maxSpeed: Double,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "spacemarine_id")
    var spaceMarine: SpaceMarine? = null
) : BaseEntity<Long>()