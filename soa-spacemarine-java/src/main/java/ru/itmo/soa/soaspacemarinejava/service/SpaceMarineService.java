package ru.itmo.soa.soaspacemarinejava.service;

import io.spring.guides.gs_producing_web_service.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ru.itmo.soa.soaspacemarinejava.domain.*;
import ru.itmo.soa.soaspacemarinejava.domain.AstartesCategory;
import ru.itmo.soa.soaspacemarinejava.domain.Chapter;
import ru.itmo.soa.soaspacemarinejava.domain.Coordinates;
import ru.itmo.soa.soaspacemarinejava.domain.SpaceMarine;
import ru.itmo.soa.soaspacemarinejava.domain.Weapon;
import ru.itmo.soa.soaspacemarinejava.exception.ResourceAlreadyExistException;
import ru.itmo.soa.soaspacemarinejava.exception.ResourceNotFoundException;
import ru.itmo.soa.soaspacemarinejava.exception.ServiceFault;
import ru.itmo.soa.soaspacemarinejava.exception.ServiceFaultException;
import ru.itmo.soa.soaspacemarinejava.repository.SpaceMarineRepository;
import ru.itmo.soa.soaspacemarinejava.repository.StarShipRepository;

@Service
public class SpaceMarineService {
    private static final Logger logger = LoggerFactory.getLogger(SpaceMarineService.class);

    private final SpaceMarineRepository spaceMarineRepository;
    private final StarShipRepository starShipRepository;
    private final ModelMapper mapper;

    public SpaceMarineService(SpaceMarineRepository spaceMarineRepository,
                              StarShipRepository starShipRepository,
                              ModelMapper mapper) {
        this.spaceMarineRepository = spaceMarineRepository;
        this.starShipRepository = starShipRepository;
        this.mapper = mapper;
    }

    public AddSpaceMarineResponse create(AddSpaceMarineRequest createReq) {
        SpaceMarine spaceMarine = new SpaceMarine();
        spaceMarine.setName(createReq.getName());
        spaceMarine.setCoordinates(
                new Coordinates(createReq.getCoordinates().getX(), createReq.getCoordinates().getY()));
        spaceMarine.setHealth(createReq.getHealth());
        spaceMarine.setHeight(createReq.getHeight());
        spaceMarine.setCategory(AstartesCategory.valueOf(createReq.getCategory().name()));
        spaceMarine.setWeaponType(Weapon.valueOf(createReq.getWeapon().name()));
        spaceMarine.setChapter(new Chapter(createReq.getChapter().getName(), createReq.getChapter().getParentLegion(),
                createReq.getChapter().getMarinesCount(), createReq.getChapter().getWorld()));
        spaceMarine.setCreationDate(LocalDateTime.now().toString());

        SpaceMarine spaceMarine1 = spaceMarineRepository.save(spaceMarine);
        var sp = mapper.map(spaceMarine1, io.spring.guides.gs_producing_web_service.SpaceMarine.class);
        AddSpaceMarineResponse addSpaceMarineResponse = new AddSpaceMarineResponse();
        addSpaceMarineResponse.setSpaceMarine(sp);
        return addSpaceMarineResponse;
    }

    @Transactional
    public UpdateSpaceMarineByIdResponse update(UpdateSpaceMarineByIdRequest updateReq) {
        StarShip starShipToUpdate = null;
        Long starShipId = updateReq.getStarShipId();
        if (starShipId != null) {
            starShipToUpdate = starShipRepository.findById(starShipId).orElseThrow(
                    () -> new ServiceFaultException("Error", new ServiceFault("404", "Воздушный корабль с id = " + starShipId + " не найден!")));

            if (starShipToUpdate.getSpaceMarine() != null) {
                if (starShipToUpdate.getSpaceMarine().getId().equals(updateReq.getId())) {
                    throw new ResourceAlreadyExistException("Воздушный десантник с id = " + updateReq.getId() +
                            " и так занял корабль с id = " + starShipId + "!");
                }
                throw new ServiceFaultException("Error", new ServiceFault("400", "Воздушный корабль с id = " + starShipId + " уже занят!"));
            }
        }

        SpaceMarine spaceMarine = spaceMarineRepository.findById(updateReq.getId()).orElseThrow(
                () -> new ServiceFaultException("Error", new ServiceFault("404","Воздушный десантник с id = " + updateReq.getId() + " не найден!")));

        spaceMarine.setName(updateReq.getName());
        spaceMarine.setCoordinates(
                new Coordinates(updateReq.getCoordinates().getX(), updateReq.getCoordinates().getY()));
        spaceMarine.setHealth(updateReq.getHealth());
        spaceMarine.setHeight(updateReq.getHeight());
        spaceMarine.setCategory(AstartesCategory.valueOf(updateReq.getCategory().value()));
        spaceMarine.setWeaponType(Weapon.valueOf(updateReq.getWeapon().value()));
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
        UpdateSpaceMarineByIdResponse response = new UpdateSpaceMarineByIdResponse();
        var sp = mapper.map(marine, io.spring.guides.gs_producing_web_service.SpaceMarine.class);
        response.setSpaceMarine(sp);
        return response;
    }

    public CountLowerChapterResponse countChapterLower(CountLowerChapterRequest number) {
        var res = spaceMarineRepository.countAllByChapterMarinesCountLessThan(number.getNumber());
        CountLowerChapterResponse response = new CountLowerChapterResponse();
        response.setNumber(res);
        return response;
    }

    public SpaceMarine findById(Long id) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(id).orElseThrow(
                () -> new ServiceFaultException("Error", new ServiceFault("404","Воздушный десантник с id = " + id + " не найден!")));
        return spaceMarine;
    }

    public GetSpaceMarineByIdResponse findById(GetSpaceMarineByIdRequest id) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(id.getId()).orElseThrow(
                () -> new ServiceFaultException("Error", new ServiceFault("404","Воздушный десантник с id = " + id.getId() + " не найден!")));
        GetSpaceMarineByIdResponse response = new GetSpaceMarineByIdResponse();
        var sp = mapper.map(spaceMarine, io.spring.guides.gs_producing_web_service.SpaceMarine.class);
        response.setSpaceMarine(sp);
        return response;
    }

    public DeleteSpaceMarineByIdResponse deleteById(DeleteSpaceMarineByIdRequest id) {
        spaceMarineRepository.findById(id.getId()).orElseThrow(
                () -> new ServiceFaultException("Error", new ServiceFault("404","Воздушный десантник с id = " + id.getId() + " не найден!")));

        spaceMarineRepository.deleteById(id.getId());
        DeleteSpaceMarineByIdResponse response = new DeleteSpaceMarineByIdResponse();
        response.setId(id.getId());
        return response;
    }

    public GetAllResponse fetchSpaceMarineDataAsPageWithFilteringAndSorting(GetAllRequest request) {

        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(createSortOrder(request.getSortBy(), request.getSortOrder())));
        Page<SpaceMarine> spaceMarines = null;
        if (request.getHealth() != null && request.getHeight() == null) {
            spaceMarines = spaceMarineRepository.findByDefaultFieldsAndHealth(request.getName(), request.getCategory(), request.getWeapon(), request.getHealth(), pageable);
        }
        if (request.getHealth() == null && request.getHeight() != null) {
            spaceMarines = spaceMarineRepository.findByDefaultFieldsAndHeight(request.getName(), request.getCategory(), request.getWeapon(), request.getHeight(), pageable);
        }
        if (request.getHealth() != null && request.getHeight() != null) {
            spaceMarines = spaceMarineRepository.findByAllFields(request.getName(), request.getCategory(), request.getWeapon(), request.getHealth(), request.getHeight(), pageable);
        }

        spaceMarines = spaceMarineRepository.findByDefaultFields(request.getName(), request.getCategory(), request.getWeapon(), pageable);

        GetAllResponse response = new GetAllResponse();
        response.setTotalPages(spaceMarines.getTotalPages());
        List<io.spring.guides.gs_producing_web_service.SpaceMarine> spaceMarines1 = new ArrayList<>();

        for (SpaceMarine spaceMarine : spaceMarines.getContent()) {
            SpaceMarine spaceMarine1 = findById(spaceMarine.getId());
            spaceMarines1.add(mapper.map(spaceMarine1, io.spring.guides.gs_producing_web_service.SpaceMarine.class));
        }
        Content content = new Content();
        content.getSpaceMarine().addAll(spaceMarines1);
        response.setContent(content);
        return response;
    }

    private List<Sort.Order> createSortOrder(String sort, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        sorts.add(new Sort.Order(direction, sort));
        return sorts;
    }

    public IncludeStarshipResponse starShipInclude(IncludeStarshipRequest request) {
        StarShip starShipToUpdate = starShipRepository.findById(request.getStarShipId()).orElseThrow(
                () -> new ServiceFaultException("Error", new ServiceFault("404","Воздушный корабль с id = " + request.getStarShipId() + " не найден!")));

        if (starShipToUpdate.getSpaceMarine() != null) {
            if (starShipToUpdate.getSpaceMarine().getId().equals(request.getId())) {
                throw new ServiceFaultException("Error", new ServiceFault("400","Воздушный десантник с id = " + request.getId() +
                        " и так занял корабль с id = " + request.getStarShipId() + "!"));
            }
            throw new ServiceFaultException("Error", new ServiceFault("400","Воздушный корабль с id = " + request.getStarShipId() + " уже занят!"));
        }

        SpaceMarine spaceMarine = spaceMarineRepository.findById(request.getId()).orElseThrow(
                () -> new ServiceFaultException("Error", new ServiceFault("404","Воздушный десантник с id = " + request.getId() + " не найден!")));

        starShipToUpdate.setSpaceMarine(spaceMarine);
        starShipRepository.save(starShipToUpdate);
        IncludeStarshipResponse response = new IncludeStarshipResponse();
        response.setStarShipId(response.getStarShipId());
        return response;
    }
}