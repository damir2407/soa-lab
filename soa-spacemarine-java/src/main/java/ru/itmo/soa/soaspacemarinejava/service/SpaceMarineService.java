package ru.itmo.soa.soaspacemarinejava.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ru.itmo.soa.soaspacemarinejava.domain.Chapter;
import ru.itmo.soa.soaspacemarinejava.domain.Coordinates;
import ru.itmo.soa.soaspacemarinejava.domain.SpaceMarine;
import ru.itmo.soa.soaspacemarinejava.domain.StarShip;
import ru.itmo.soa.soaspacemarinejava.exception.ResourceAlreadyExistException;
import ru.itmo.soa.soaspacemarinejava.exception.ResourceNotFoundException;
import ru.itmo.soa.soaspacemarinejava.repository.SpaceMarineRepository;
import ru.itmo.soa.soaspacemarinejava.repository.StarShipRepository;
import ru.itmo.soa.soaspacemarinejava.rest.data.SpaceMarineRequest;

@Service
public class SpaceMarineService {

    private final SpaceMarineRepository spaceMarineRepository;
    private final StarShipRepository starShipRepository;

    public SpaceMarineService(SpaceMarineRepository spaceMarineRepository, StarShipRepository starShipRepository) {
        this.spaceMarineRepository = spaceMarineRepository;
        this.starShipRepository = starShipRepository;
    }

    public SpaceMarine create(SpaceMarineRequest createReq) {
        SpaceMarine spaceMarine = new SpaceMarine();
        spaceMarine.setName(createReq.getName());
        spaceMarine.setCoordinates(
            new Coordinates(createReq.getCoordinates().getX(), createReq.getCoordinates().getY()));
        spaceMarine.setHealth(createReq.getHealth());
        spaceMarine.setHeight(createReq.getHeight());
        spaceMarine.setCategory(createReq.getCategory());
        spaceMarine.setWeaponType(createReq.getWeaponType());
        spaceMarine.setChapter(new Chapter(createReq.getChapter().getName(), createReq.getChapter().getParentLegion(),
            createReq.getChapter().getMarinesCount(), createReq.getChapter().getWorld()));
        spaceMarine.setCreationDate(LocalDateTime.now().toString());

        return spaceMarineRepository.save(spaceMarine);
    }

    @Transactional
    public SpaceMarine update(SpaceMarineRequest updateReq, Long id) {
        StarShip starShipToUpdate = null;
        Long starShipId = updateReq.getStarShipId();
        if (starShipId != null) {
            starShipToUpdate = starShipRepository.findById(starShipId).orElseThrow(
                () -> new ResourceNotFoundException("Воздушный корабль с id = " + starShipId + " не найден!"));

            if (starShipToUpdate.getSpaceMarine() != null) {
                if (starShipToUpdate.getSpaceMarine().getId().equals(id)) {
                    throw new ResourceAlreadyExistException("Воздушный десантник с id = " + id +
                        " и так занял корабль с id = " + starShipId + "!");
                }
                throw new ResourceAlreadyExistException("Воздушный корабль с id = " + starShipId + " уже занят!");
            }
        }

        SpaceMarine spaceMarine = spaceMarineRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Воздушный десантник с id = " + id + " не найден!"));

        spaceMarine.setName(updateReq.getName());
        spaceMarine.setCoordinates(
            new Coordinates(updateReq.getCoordinates().getX(), updateReq.getCoordinates().getY()));
        spaceMarine.setHealth(updateReq.getHealth());
        spaceMarine.setHeight(updateReq.getHeight());
        spaceMarine.setCategory(updateReq.getCategory());
        spaceMarine.setWeaponType(updateReq.getWeaponType());
        spaceMarine.getChapter().setName(updateReq.getChapter().getName());
        spaceMarine.getChapter().setParentLegion(updateReq.getChapter().getParentLegion());
        spaceMarine.getChapter().setMarinesCount(updateReq.getChapter().getMarinesCount());
        spaceMarine.getChapter().setWorld(updateReq.getChapter().getWorld());

        var marine = spaceMarineRepository.save(spaceMarine);

        var starShipFinal = starShipRepository.findBySpaceMarine(marine);
        starShipFinal.setSpaceMarine(null);
        starShipRepository.save(starShipFinal);

        if (starShipToUpdate != null) {
            starShipToUpdate.setSpaceMarine(marine);
            starShipRepository.save(starShipToUpdate);
        }

        return marine;
    }

    public List<SpaceMarine> searchByNameContains(String substring) {
        return spaceMarineRepository.findAllByNameContaining(substring);
    }

    public List<SpaceMarine> searchByNameStartsWith(String substring) {
        return spaceMarineRepository.findAllByNameStartsWith(substring);
    }

    public int countChapterLower(int number) {
        return spaceMarineRepository.countAllByChapterMarinesCountLessThan(number);
    }

    public SpaceMarine findById(Long id) {
        return spaceMarineRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Воздушный десантник с id = " + id + " не найден!"));
    }

    public Long deleteById(Long id) {
        spaceMarineRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Воздушный десантник с id = " + id + " не найден!"));

        spaceMarineRepository.deleteById(id);
        return id;
    }

    public Page<SpaceMarine> fetchSpaceMarineDataAsPageWithFilteringAndSorting(
        String name, Long health, Double height, String category, String weaponType,
        String sortBy, String sortOrder, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortBy, sortOrder)));

        if (health != null && height == null) {
            return spaceMarineRepository.findByDefaultFieldsAndHealth(name, category, weaponType, health, pageable);
        }
        if (health == null && height != null) {
            return spaceMarineRepository.findByDefaultFieldsAndHeight(name, category, weaponType, height, pageable);
        }
        if (health != null && height != null) {
            return spaceMarineRepository.findByAllFields(name, category, weaponType, health, height, pageable);
        }

        return spaceMarineRepository.findByDefaultFields(name, category, weaponType, pageable);
    }

    private List<Sort.Order> createSortOrder(String sort, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        sorts.add(new Sort.Order(direction, sort));
        return sorts;
    }

    public Long starShipInclude(Long id, Long starShipId) {
        StarShip starShipToUpdate = starShipRepository.findById(starShipId).orElseThrow(
            () -> new ResourceNotFoundException("Воздушный корабль с id = " + starShipId + " не найден!"));

        if (starShipToUpdate.getSpaceMarine() != null) {
            if (starShipToUpdate.getSpaceMarine().getId().equals(id)) {
                throw new ResourceAlreadyExistException("Воздушный десантник с id = " + id +
                    " и так занял корабль с id = " + starShipId + "!");
            }
            throw new ResourceAlreadyExistException("Воздушный корабль с id = " + starShipId + " уже занят!");
        }

        SpaceMarine spaceMarine = spaceMarineRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Воздушный десантник с id = " + id + " не найден!"));

        starShipToUpdate.setSpaceMarine(spaceMarine);
        starShipRepository.save(starShipToUpdate);

        return starShipId;
    }
}