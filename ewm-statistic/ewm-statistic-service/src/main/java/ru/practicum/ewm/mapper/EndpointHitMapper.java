package ru.practicum.ewm.mapper;

import dto.HitCreateRequest;
import dto.ViewStats;
import org.mapstruct.Mapper;
import ru.practicum.ewm.model.EndpointHit;

@Mapper
public interface EndpointHitMapper {

    EndpointHit requestToEndpointHit(HitCreateRequest hitCreateRequest);

}