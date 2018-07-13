package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.feed.vo.PostPermFlag
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class PostPermFlagCollectionConverter: AttributeConverter<Set<PostPermFlag>, String> {

    override fun convertToDatabaseColumn(attribute: Set<PostPermFlag>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): Set<PostPermFlag> {
        return objectMapper.readValue(dbData, object : TypeReference<Set<PostPermFlag>>() {})
            ?: EnumSet.noneOf(PostPermFlag::class.java)
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}