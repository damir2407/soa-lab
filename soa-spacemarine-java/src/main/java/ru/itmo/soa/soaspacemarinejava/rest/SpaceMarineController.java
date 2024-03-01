//package ru.itmo.soa.soaspacemarinejava.rest;
//
//
//import jakarta.validation.Valid;
//import java.util.List;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Sort;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import ru.itmo.soa.soaspacemarinejava.domain.SpaceMarine;
//import ru.itmo.soa.soaspacemarinejava.rest.data.SpaceMarineRequest;
//import ru.itmo.soa.soaspacemarinejava.service.SpaceMarineService;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/v1/space-marines")
//@Validated
//public class SpaceMarineController {
//
//    private final SpaceMarineService service;
//
//
//    public SpaceMarineController(SpaceMarineService service) {
//        this.service = service;
//    }
//
//    @PostMapping
//    public SpaceMarine create(@Valid @RequestBody SpaceMarineRequest createReq) {
//        return service.create(createReq);
//    }
//
//    @GetMapping("/{id}")
//    public SpaceMarine get(@PathVariable long id) {
//        return service.findById(id);
//    }
//
//    @PutMapping("/{id}")
//    public SpaceMarine update(
//        @PathVariable long id,
//        @Valid @RequestBody SpaceMarineRequest updateReq
//    ) {
//        return service.update(updateReq, id);
//    }
//
//    @DeleteMapping("/{id}")
//    public Long delete(@PathVariable long id) {
//        return service.deleteById(id);
//    }
//
//    @PostMapping("/count-chapter-lower")
//    public int countChapterLower(@RequestParam int number) {
//        return service.countChapterLower(number);
//    }
//
//
//    @GetMapping
//    public Page<SpaceMarine> getAll(
//        @RequestParam(defaultValue = "") String name,
//        @RequestParam(required = false) Long health,
//        @RequestParam(required = false) Double height,
//        @RequestParam(defaultValue = "") String category,
//        @RequestParam(defaultValue = "") String weaponType,
//        @RequestParam(defaultValue = "name") String sortBy,
//        @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder,
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "5") int size
//    ) {
//        return service.fetchSpaceMarineDataAsPageWithFilteringAndSorting(
//            name,
//            health,
//            height,
//            category,
//            weaponType,
//            sortBy,
//            sortOrder.toString(),
//            page,
//            size
//        );
//    }
//
//    @PutMapping("/{id}/starship-include/{starShipId}")
//    public Long includeStarship(
//        @PathVariable long id,
//        @PathVariable long starShipId
//    ) {
//        return service.starShipInclude(id, starShipId);
//    }
//
//
//}
