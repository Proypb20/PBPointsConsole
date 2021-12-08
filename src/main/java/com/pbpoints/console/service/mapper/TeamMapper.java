package com.pbpoints.console.service.mapper;

import com.pbpoints.console.domain.*;
import com.pbpoints.console.service.dto.TeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TeamDTO toDtoName(Team team);
}
