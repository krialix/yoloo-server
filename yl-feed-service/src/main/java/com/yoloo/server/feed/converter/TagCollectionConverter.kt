package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class TagCollectionConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return objectMapper.readValue(dbData, object : TypeReference<List<String>>() {})
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}