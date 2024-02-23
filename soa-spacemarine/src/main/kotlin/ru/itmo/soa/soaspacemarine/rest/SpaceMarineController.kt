package ru.itmo.soa.soaspacemarine.rest

import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.itmo.soa.soaspacemarine.rest.data.ErrorResponse
import ru.itmo.soa.soaspacemarine.rest.data.SpaceMarineRequest
import ru.itmo.soa.soaspacemarine.service.SpaceMarineService
import java.time.LocalDateTime


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/space-marines")
@Validated
class SpaceMarineController(
    private val service: SpaceMarineService
) {

    @PostMapping
    fun create(@Valid @RequestBody createReq: SpaceMarineRequest): ResponseEntity<*> {
        return service.create(createReq).handle()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<*> {
        println("я пришел с id $id")
        return service.findViaExecution(id) {}.handle()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long,
               @Valid @RequestBody updateReq: SpaceMarineRequest
    ): ResponseEntity<*> {
        return service.update(updateReq, id).handle()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<*> {
        return service.deleteById(id).handle()
    }

    @PostMapping("/count-chapter-lower")
    fun countChapterLower(@RequestParam number: Int): ResponseEntity<*> {
        return service.countChapterLower(number).handle()
    }

    @PostMapping("/search-name-contains")
    fun searchByNameContains(@RequestParam substring: String): ResponseEntity<*> {
        return service.searchByNameContains(substring).handle()
    }

    @PostMapping("/search-by-name-start")
    fun searchByNameStartsWith(@RequestParam substring: String): ResponseEntity<*> {
        return service.searchByNameStartsWith(substring).handle()
    }

    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "") name: String,
        @RequestParam health: Long? = null,
        @RequestParam height: Double? = null,
        @RequestParam(defaultValue = "") category: String,
        @RequestParam(defaultValue = "") weaponType: String,
        @RequestParam(defaultValue = "name") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortOrder: Sort.Direction,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): ResponseEntity<*> {
        return ResponseEntity.ok(service.fetchSpaceMarineDataAsPageWithFilteringAndSorting(
            name,
            health,
            height,
            category,
            weaponType,
            sortBy,
            sortOrder.toString(),
            page,
            size
        ))
    }

    @PutMapping("/{id}/starship-include/{starShipId}")
    fun includeStarship(@PathVariable id: Long,
                        @PathVariable starShipId: Long): ResponseEntity<*> {
        return service.starShipInclude(id,starShipId).handle()
    }

    private fun SpaceMarineService.OperationResult.handle(): ResponseEntity<*> {
        return when (this) {
            is SpaceMarineService.OperationResult.NotFound -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ErrorResponse(
                        LocalDateTime.now().toString(),
                        errorMessage
                    )
                )
            }

            is SpaceMarineService.OperationResult.Ok -> {
                println("отдаю $item")
                ResponseEntity
                    .status(HttpStatus.OK).body(item)
            }

            is SpaceMarineService.OperationResult.AlreadyExists -> {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).body(
                        ErrorResponse(
                            LocalDateTime.now().toString(),
                            errorMessage
                        )
                    )
            }
        }
    }
}