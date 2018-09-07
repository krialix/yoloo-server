package com.yoloo.server.feed.converter

import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter

abstract class JsonCollectionConverter<T> : AttributeConverter<T, String> {

    override fun convertToDatabaseColumn(attribute: T?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    companion object {
        val objectMapper = ObjectMapper()
    }
}
