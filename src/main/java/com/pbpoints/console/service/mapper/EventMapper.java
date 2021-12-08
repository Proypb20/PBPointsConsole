package com.pbpoints.console.service.mapper;

import com.pbpoints.console.domain.*;
import com.pbpoints.console.service.dto.EventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class })
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "name")
    EventDTO toDto(Event s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EventDTO toDtoName(Event event);
}
