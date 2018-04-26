package br.com.controlepartidascs.model.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter
		implements AttributeConverter<java.time.ZonedDateTime, java.sql.Timestamp> {

	public java.sql.Timestamp convertToDatabaseColumn(ZonedDateTime entityValue) {
		return Timestamp.from(entityValue.toInstant());
	}

	public ZonedDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
		LocalDateTime localDateTime = databaseValue.toLocalDateTime();
		return localDateTime.atZone(ZoneId.systemDefault());
	}

}