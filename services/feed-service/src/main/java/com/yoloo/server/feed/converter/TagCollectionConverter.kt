package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import javax.persistence.Converter

@Converter
class TagCollectionConverter : JsonCollectionConverter<Set<String>>() {

    override fun convertToEntityAttribute(dbData: String?): Set<String> {
        if (dbData.isNullOrBlank()) {
            return emptySet()
        }

        return objectMapper.readValue(dbData, object : TypeReference<Set<String>>() {})
    }
}
