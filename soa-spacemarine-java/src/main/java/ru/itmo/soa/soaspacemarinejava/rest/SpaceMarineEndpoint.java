package ru.itmo.soa.soaspacemarinejava.rest;

import io.spring.guides.gs_producing_web_service.*;

import jakarta.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.itmo.soa.soaspacemarinejava.service.SpaceMarineService;


import javax.xml.namespace.QName;


@Endpoint
public class SpaceMarineEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final SpaceMarineService spaceMarineService;
    private static final Logger logger = LoggerFactory.getLogger(SpaceMarineEndpoint.class);

    public SpaceMarineEndpoint(SpaceMarineService spaceMarineService) {
        this.spaceMarineService = spaceMarineService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addSpaceMarineRequest")
    @ResponsePayload
    public JAXBElement<AddSpaceMarineResponse> addSpaceMarine(@RequestPayload JAXBElement<AddSpaceMarineRequest> addSpaceMarineRequest) {
        var res =spaceMarineService.create(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(AddSpaceMarineResponse.class.getSimpleName()), AddSpaceMarineResponse.class, res);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSpaceMarineByIdRequest")
    @ResponsePayload
    public JAXBElement<GetSpaceMarineByIdResponse> getSpaceMarine(@RequestPayload JAXBElement<GetSpaceMarineByIdRequest> addSpaceMarineRequest) {
        var res = spaceMarineService.findById(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(GetSpaceMarineByIdResponse.class.getSimpleName()), GetSpaceMarineByIdResponse.class, res);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateSpaceMarineByIdRequest")
    @ResponsePayload
    public JAXBElement<UpdateSpaceMarineByIdResponse> updateSpaceMarine(@RequestPayload JAXBElement<UpdateSpaceMarineByIdRequest> addSpaceMarineRequest) {
        var res = spaceMarineService.update(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(UpdateSpaceMarineByIdResponse.class.getSimpleName()), UpdateSpaceMarineByIdResponse.class, res);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteSpaceMarineByIdRequest")
    @ResponsePayload
    public JAXBElement<DeleteSpaceMarineByIdResponse> deleteSpaceMarine(@RequestPayload JAXBElement<DeleteSpaceMarineByIdRequest> addSpaceMarineRequest) {
        var res = spaceMarineService.deleteById(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(DeleteSpaceMarineByIdResponse.class.getSimpleName()), DeleteSpaceMarineByIdResponse.class, res);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "countLowerChapterRequest")
    @ResponsePayload
    public JAXBElement<CountLowerChapterResponse> countLowerChapter(@RequestPayload JAXBElement<CountLowerChapterRequest> addSpaceMarineRequest) {
        var res = spaceMarineService.countChapterLower(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(CountLowerChapterResponse.class.getSimpleName()), CountLowerChapterResponse.class, res);
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "includeStarshipRequest")
    @ResponsePayload
    public JAXBElement<IncludeStarshipResponse> includeStarship(@RequestPayload JAXBElement<IncludeStarshipRequest> addSpaceMarineRequest) {
        var res = spaceMarineService.starShipInclude(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(IncludeStarshipResponse.class.getSimpleName()), IncludeStarshipResponse.class, res);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllRequest")
    @ResponsePayload
    public JAXBElement<GetAllResponse> getAll(@RequestPayload JAXBElement<GetAllRequest> addSpaceMarineRequest) {
        logger.error(addSpaceMarineRequest.getValue().getSortOrder());
        var res = spaceMarineService.fetchSpaceMarineDataAsPageWithFilteringAndSorting(addSpaceMarineRequest.getValue());
        return new JAXBElement<>(new QName(GetAllResponse.class.getSimpleName()), GetAllResponse.class, res);
    }
}
