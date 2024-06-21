package by.statistic.service.mapper;

import by.pm.model.Stake;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true), imports = {BigDecimal.class})
public interface StakeMapper {

    @Mapping(target = "stakeType", ignore = true)
    @Mapping(target = "ratio", expression = "java(BigDecimal.valueOf(stake.getRatio()))")
    by.statistic.model.Stake toDbModel(Stake stake);

}
