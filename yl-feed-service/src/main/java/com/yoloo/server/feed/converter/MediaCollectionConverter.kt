package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.yoloo.server.common.vo.Media
import javax.persistence.Converter

@Converter
class MediaCollectionConverter : JsonCollectionConverter<List<Media>>() {

    override fun convertToEntityAttribute(dbData: String?): List<Media> {
        if (dbData.isNullOrBlank()) {
            return emptyList()
        }

        return objectMapper.readValue(dbData, object : TypeReference<List<Media>>() {})
    }
}
