package by.statistic.service.mapper;

import by.pm.model.Sport;
import by.statistic.model.SportType;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface SportTypeMapper {

    SportType toDbModel(Sport sport);

}
