package com.yoloo.server.feed.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.feed.vo.PostPermFlag
import org.springframework.stereotype.Component
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@NoArg
@Component
@Converter
class PostPermFlagCollectionConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<Set<PostPermFlag>, String> {

    override fun convertToDatabaseColumn(attribute: Set<PostPermFlag>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): Set<PostPermFlag> {
        return objectMapper.readValue(dbData, object : TypeReference<Set<PostPermFlag>>() {})
            ?: EnumSet.noneOf(PostPermFlag::class.java
        )
    }
}