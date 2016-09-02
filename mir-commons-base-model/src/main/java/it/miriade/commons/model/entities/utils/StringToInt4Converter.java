package it.miriade.commons.model.entities.utils;

import javax.persistence.AttributeConverter;

public class StringToInt4Converter implements AttributeConverter<String, Integer> {

	public Integer convertToDatabaseColumn(String uid) {
		return AttributeConverterUtils.castToInteger(uid);
	}

	public String convertToEntityAttribute(Integer integer) {
		return AttributeConverterUtils.convertFromInteger(integer);
	}

}