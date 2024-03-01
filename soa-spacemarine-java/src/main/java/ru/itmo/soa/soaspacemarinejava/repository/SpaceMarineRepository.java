package ru.itmo.soa.soaspacemarinejava.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.soa.soaspacemarinejava.domain.SpaceMarine;

@Repository
public interface SpaceMarineRepository extends JpaRepository<SpaceMarine, Long> {

    String DEFAULT_QUERY =
        "select * from spacemarine as b where UPPER(b.name) like CONCAT('%',UPPER(?1),'%') " +
            "and UPPER(b.category) like CONCAT('%',UPPER(?2),'%') and " +
            "UPPER(b.weapon_type) like CONCAT('%',UPPER(?3),'%')";

    String QUERY_WITH_HEALTH = DEFAULT_QUERY + " and b.health=?4";

    String QUERY_WITH_HEIGHT = DEFAULT_QUERY + " and b.height=?4";
    String FULL_QUERY = DEFAULT_QUERY + " and b.health=?4 and b.height=?5";

    List<SpaceMarine> findAllByNameContaining(String substring);

    List<SpaceMarine> findAllByNameStartsWith(String substring);

    int countAllByChapterMarinesCountLessThan(int number);

    @Query(nativeQuery = true, value = DEFAULT_QUERY)
    Page<SpaceMarine> findByDefaultFields(String name, String category, String weaponType, Pageable pageable);

    @Query(nativeQuery = true, value = QUERY_WITH_HEALTH)
    Page<SpaceMarine> findByDefaultFieldsAndHealth(String name, String category, String weaponType, long health,
        Pageable pageable);

    @Query(nativeQuery = true, value = QUERY_WITH_HEIGHT)
    Page<SpaceMarine> findByDefaultFieldsAndHeight(String name, String category, String weaponType, double height,
        Pageable pageable);

    @Query(nativeQuery = true, value = FULL_QUERY)
    Page<SpaceMarine> findByAllFields(String name, String category, String weaponType, long health, double height,
        Pageable pageable);
}
