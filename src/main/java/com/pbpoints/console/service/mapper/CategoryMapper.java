package com.pbpoints.console.service.mapper;

import com.pbpoints.console.domain.*;
import com.pbpoints.console.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { EventMapper.class })
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "event", source = "event", qualifiedByName = "name")
    CategoryDTO toDto(Category s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoName(Category category);
}
