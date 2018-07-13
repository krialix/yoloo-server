package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.vo.Media
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class MediaCollectionConverter : AttributeConverter<List<Media>, String> {

    override fun convertToDatabaseColumn(attribute: List<Media>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<Media> {
        return objectMapper.readValue(dbData, object : TypeReference<List<Media>>() {})
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}