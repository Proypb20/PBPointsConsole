package com.pbpoints.console.service.mapper;

import com.pbpoints.console.domain.*;
import com.pbpoints.console.service.dto.GameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, CategoryMapper.class })
public interface GameMapper extends EntityMapper<GameDTO, Game> {
    @Mapping(target = "teamA", source = "teamA")
    @Mapping(target = "teamB", source = "teamB")
    @Mapping(target = "category", source = "category")
    GameDTO toDto(Game s);
}
