package by.statistic.service.mapper;

import by.statistic.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TeamMapper {

    public Team toDbModel(String teamName) {
        if (teamName == null) {
            return null;
        }
        Team team1 = new Team();
        team1.setName(teamName);
        return team1;
    }

}
