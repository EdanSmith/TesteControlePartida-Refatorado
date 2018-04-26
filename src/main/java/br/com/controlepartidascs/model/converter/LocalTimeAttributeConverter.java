package br.com.controlepartidascs.model.converter;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

	public Time convertToDatabaseColumn(LocalTime locTime) {
		return (locTime == null ? null : Time.valueOf(locTime));
	}

	public LocalTime convertToEntityAttribute(Time sqlTime) {
		return (sqlTime == null ? null : sqlTime.toLocalTime());
	}
}