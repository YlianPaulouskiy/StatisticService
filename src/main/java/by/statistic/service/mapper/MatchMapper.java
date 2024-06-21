package by.statistic.service.mapper;

import by.pm.model.Match;
import by.statistic.utils.DateTimeUtils;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true), imports = DateTimeUtils.class)
public interface MatchMapper {

    @Mapping(target = "firstTeam", ignore = true)
    @Mapping(target = "secondTeam", ignore = true)
    @Mapping(target = "tourney", ignore = true)
    @Mapping(target = "stake", ignore = true)
    @Mapping(target = "date", expression = "java(DateTimeUtils.toLocalDateTime(match.getDate()))")
    by.statistic.model.Match toDbModel(Match match);

}
