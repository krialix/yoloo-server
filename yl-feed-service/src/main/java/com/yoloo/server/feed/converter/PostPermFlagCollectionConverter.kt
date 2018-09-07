package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.yoloo.server.feed.vo.PostPermFlag
import java.util.*
import javax.persistence.Converter

@Converter
class PostPermFlagCollectionConverter: JsonCollectionConverter<Set<PostPermFlag>>() {

    override fun convertToEntityAttribute(dbData: String?): Set<PostPermFlag> {
        if (dbData.isNullOrBlank()) {
            return EnumSet.noneOf(PostPermFlag::class.java)
        }

        return objectMapper.readValue(dbData, object : TypeReference<Set<PostPermFlag>>() {})
    }
}
