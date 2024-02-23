package ru.itmo.soa.soaspacemarine.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "spacemarine")
class SpaceMarine(
    var name: String,
    @OneToOne(cascade = [CascadeType.ALL])
    var coordinates: Coordinates,
    var health: Long,
    var height: Double,
    @Enumerated(EnumType.STRING)
    var category: AstartesCategory? = null,
    @Enumerated(EnumType.STRING)
    var weaponType: Weapon,
    @OneToOne(cascade = [CascadeType.ALL])
    var chapter: Chapter,
    var creationDate: String,
) : BaseEntity<Long>()