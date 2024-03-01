package ru.itmo.soa.soaspacemarinejava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.soa.soaspacemarinejava.domain.SpaceMarine;
import ru.itmo.soa.soaspacemarinejava.domain.StarShip;

@Repository
public interface StarShipRepository extends JpaRepository<StarShip, Long> {

    StarShip findBySpaceMarine(SpaceMarine spaceMarine);
}