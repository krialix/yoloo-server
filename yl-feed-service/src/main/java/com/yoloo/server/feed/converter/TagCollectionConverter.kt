package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class TagCollectionConverter : AttributeConverter<Set<String>, String> {

    override fun convertToDatabaseColumn(attribute: Set<String>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): Set<String> {
        if (dbData.isNullOrBlank()) {
            return emptySet()
        }

        return objectMapper.readValue(dbData, object : TypeReference<Set<String>>() {})
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}