package by.statistic.service.mapper;


import by.pm.model.Tourney;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface TourneyMapper {

    @Mapping(target = "game", ignore = true)
    @Mapping(target = "sportType", ignore = true)
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "winRate", ignore = true)
    @Mapping(target = "losses", ignore = true)
    by.statistic.model.Tourney toDbModel(Tourney tourney);

}
