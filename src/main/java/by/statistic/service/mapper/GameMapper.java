package by.statistic.service.mapper;

import by.pm.model.Game;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface GameMapper {

    by.statistic.model.Game toDbModel(Game game);


}
