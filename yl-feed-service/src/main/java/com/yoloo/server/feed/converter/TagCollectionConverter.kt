package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.util.NoArg
import org.springframework.stereotype.Component
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@NoArg
@Component
@Converter
class TagCollectionConverter(private val objectMapper: ObjectMapper) : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return objectMapper.readValue(dbData, object : TypeReference<List<String>>() {})
    }
}