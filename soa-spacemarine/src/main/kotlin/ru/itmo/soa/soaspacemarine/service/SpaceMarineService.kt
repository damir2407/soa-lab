package ru.itmo.soa.soaspacemarine.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.soa.soaspacemarine.domain.Chapter
import ru.itmo.soa.soaspacemarine.domain.Coordinates
import ru.itmo.soa.soaspacemarine.domain.SpaceMarine
import ru.itmo.soa.soaspacemarine.domain.StarShip
import ru.itmo.soa.soaspacemarine.repository.SpaceMarineRepository
import ru.itmo.soa.soaspacemarine.repository.StarShipRepository
import ru.itmo.soa.soaspacemarine.rest.data.SpaceMarineRequest
import java.time.LocalDateTime


@Service
class SpaceMarineService(
    private val spaceMarineRepository: SpaceMarineRepository,
    private val starShipRepository: StarShipRepository
) {

    fun create(createReq: SpaceMarineRequest): OperationResult {
        spaceMarineRepository.save(
            with(createReq) {
                SpaceMarine(
                    name = name,
                    coordinates = Coordinates(
                        x = coordinates.x,
                        y = coordinates.y
                    ),
                    health = health,
                    height = height,
                    category = category,
                    weaponType = weaponType,
                    chapter =
                    Chapter(
                        name = chapter.name,
                        parentLegion = chapter.parentLegion,
                        marinesCount = chapter.marinesCount,
                        world = chapter.world
                    ),
                    creationDate = LocalDateTime.now().toString()
                )
            }
        ).also {
            return OperationResult.Ok(it)
        }
    }

    @Transactional
    fun update(updateReq: SpaceMarineRequest, id: Long): OperationResult {
        var starShipToUpdate: StarShip? = null
        updateReq.starShipId?.let {
            starShipToUpdate = starShipRepository.findByIdOrNull(updateReq.starShipId)
                ?: return OperationResult.NotFound("Воздушный корабль с id = ${updateReq.starShipId} не найден!")
            if (starShipToUpdate?.spaceMarine != null) {
                if (starShipToUpdate?.spaceMarine?.id == id) {
                    return OperationResult.AlreadyExists("Воздушный десантник с id = $id и так занял корабль с id = ${updateReq.starShipId}!")
                }
                return OperationResult.AlreadyExists("Воздушный корабль с id = ${updateReq.starShipId} уже занят!")
            }
        }

        return findViaExecution(id) {
            spaceMarineRepository.save(
                it.apply {
                    name = updateReq.name
                    coordinates = it.coordinates.apply {
                        x = updateReq.coordinates.x
                        y = updateReq.coordinates.y
                    }
                    health = updateReq.health
                    height = updateReq.height
                    category = updateReq.category
                    weaponType = updateReq.weaponType
                    chapter = it.chapter.apply {
                        name = updateReq.chapter.name
                        parentLegion = updateReq.chapter.parentLegion
                        marinesCount = updateReq.chapter.marinesCount
                        world = updateReq.chapter.world
                    }

                }
            ).also { savedObj ->
                starShipRepository.findBySpaceMarine(savedObj)?.let {
                    it.spaceMarine = null
                    starShipRepository.save(it)
                }
                starShipToUpdate?.let {
                    it.spaceMarine = savedObj
                    starShipRepository.save(it)
                }
                OperationResult.Ok(savedObj)
            }

        }
    }

    fun searchByNameContains(substring: String): OperationResult {
        spaceMarineRepository.findAllByNameContaining(substring).also {
            return OperationResult.Ok(it)
        }
    }

    fun searchByNameStartsWith(substring: String): OperationResult {
        spaceMarineRepository.findAllByNameStartsWith(substring).also {
            return OperationResult.Ok(it)
        }
    }

    fun countChapterLower(number: Int): OperationResult {
        spaceMarineRepository.countAllByChapterMarinesCountLessThan(number).also {
            return OperationResult.Ok(it)
        }
    }

    fun deleteById(id: Long): OperationResult {
        return findViaExecution(id) {
            spaceMarineRepository.deleteById(id)
            OperationResult.Ok(id)
        }
    }

    fun fetchSpaceMarineDataAsPageWithFilteringAndSorting(
        name: String,
        health: Long?,
        height: Double?,
        category: String,
        weaponType: String,
        sortBy: String,
        sortOrder: String,
        page: Int,
        size: Int
    ): Page<SpaceMarine> {
        val pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortBy, sortOrder)))
        if (health != null && height == null) {
            return spaceMarineRepository.findByDefaultFieldsAndHealth(name, category, weaponType, health, pageable)
        }
        if (health == null && height != null) {
            return spaceMarineRepository.findByDefaultFieldsAndHeight(name, category, weaponType, height, pageable)
        }
        if (health != null && height != null) {
            return spaceMarineRepository.findByAllFields(name, category, weaponType, health, height, pageable)
        }
        return spaceMarineRepository.findByDefaultFields(name, category, weaponType, pageable)


    }

    private fun createSortOrder(sort: String, sortDirection: String): List<Sort.Order> {
        val sorts: MutableList<Sort.Order> = ArrayList()

        val direction: Sort.Direction? = Sort.Direction.fromString(sortDirection)
        sorts.add(Sort.Order(direction, sort))

        return sorts
    }

    fun findViaExecution(id: Long, block: (SpaceMarine) -> Unit): OperationResult {
        return spaceMarineRepository.findByIdOrNull(id)?.let {
            block(it)
            println(it)
            OperationResult.Ok(it)
        } ?: OperationResult.NotFound("Воздушный десантник с id = $id не найден!")
    }

    fun starShipInclude(id: Long, starShipId: Long): OperationResult {
        val starShipToUpdate = starShipRepository.findByIdOrNull(starShipId)
            ?: return OperationResult.NotFound("Воздушный корабль с id = $starShipId не найден!")
        if (starShipToUpdate.spaceMarine != null) {
            if (starShipToUpdate.spaceMarine?.id == id) {
                return OperationResult.AlreadyExists("Воздушный десантник с id = $id и так занял корабль с id = $starShipId!")
            }
            return OperationResult.AlreadyExists("Воздушный корабль с id = $starShipId уже занят!")
        }

        val spaceMarine = spaceMarineRepository.findByIdOrNull(id)
            ?: return OperationResult.NotFound("Воздушный десантник с id = $id не найден!")
        starShipToUpdate.spaceMarine = spaceMarine

        starShipRepository.save(starShipToUpdate)
        return OperationResult.Ok(starShipId)
    }

    sealed class OperationResult {
        class Ok(val item: Any) : OperationResult()
        class NotFound(val errorMessage: String) : OperationResult()
        class AlreadyExists(val errorMessage: String) : OperationResult()
    }
}