package by.statistic.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;

@Converter
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime attribute) {
        return Date.valueOf(attribute.toLocalDate());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date dbData) {
        return dbData.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
