package ru.itmo.soa.soaspacemarine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.itmo.soa.soaspacemarine.domain.SpaceMarine

@Repository
interface SpaceMarineRepository : JpaRepository<SpaceMarine, Long> {

    fun findAllByNameContaining(substring: String): List<SpaceMarine>

    fun findAllByNameStartsWith(substring: String): List<SpaceMarine>

    fun countAllByChapterMarinesCountLessThan(number: Int): Int

    @Query(nativeQuery = true, value = DEFAULT_QUERY)
    fun findByDefaultFields(name: String, category: String, weaponType: String, pageable: Pageable): Page<SpaceMarine>

    @Query(nativeQuery = true, value = QUERY_WITH_HEALTH)
    fun findByDefaultFieldsAndHealth(name: String, category: String, weaponType: String, health: Long, pageable: Pageable): Page<SpaceMarine>

    @Query(nativeQuery = true, value = QUERY_WITH_HEIGHT)
    fun findByDefaultFieldsAndHeight(name: String, category: String, weaponType: String, height: Double, pageable: Pageable): Page<SpaceMarine>

    @Query(nativeQuery = true, value = FULL_QUERY)
    fun findByAllFields(name: String, category: String, weaponType: String, health: Long, height: Double, pageable: Pageable): Page<SpaceMarine>


    companion object {
        private const val DEFAULT_QUERY = "select * from spacemarine as b where UPPER(b.name) like CONCAT('%',UPPER(?1),'%') " +
            "and UPPER(b.category) like CONCAT('%',UPPER(?2),'%') and " +
            "UPPER(b.weapon_type) like CONCAT('%',UPPER(?3),'%')"

        private const val QUERY_WITH_HEALTH = "$DEFAULT_QUERY and b.health=?4"

        private const val QUERY_WITH_HEIGHT = "$DEFAULT_QUERY and b.height=?4"

        private const val FULL_QUERY = "$DEFAULT_QUERY and b.health=?4 and b.height=?5"


    }
}
