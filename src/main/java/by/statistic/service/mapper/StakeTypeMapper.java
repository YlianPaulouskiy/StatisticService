package by.statistic.service.mapper;

import by.pm.model.StakeType;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface StakeTypeMapper {

    by.statistic.model.StakeType toDbModel(StakeType stakeType);

}
