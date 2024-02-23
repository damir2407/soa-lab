package ru.itmo.soa.soaspacemarine.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.soa.soaspacemarine.domain.SpaceMarine
import ru.itmo.soa.soaspacemarine.domain.StarShip

@Repository
interface StarShipRepository : JpaRepository<StarShip, Long> {

    fun findBySpaceMarine(spaceMarine: SpaceMarine): StarShip?
}