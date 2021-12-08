package com.pbpoints.console.service.mapper;

import com.pbpoints.console.domain.*;
import com.pbpoints.console.service.dto.TournamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tournament} and its DTO {@link TournamentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TournamentMapper extends EntityMapper<TournamentDTO, Tournament> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TournamentDTO toDtoName(Tournament tournament);
}
