package com.yoloo.server.user.infrastructure.objectify.translators

import com.google.appengine.api.datastore.Blob
import com.yoloo.server.objectify.translators.AbstractSimpleTranslatorFactory
import net.cinnom.nanocuckoo.NanoCuckooFilter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class CuckooFilterTranslatorFactory :
    AbstractSimpleTranslatorFactory<NanoCuckooFilter, Blob>(NanoCuckooFilter::class.java) {
    override fun createTranslator(): SimpleTranslator<NanoCuckooFilter, Blob> {
        return object : SimpleTranslator<NanoCuckooFilter, Blob> {
            override fun loadValue(datastoreValue: Blob): NanoCuckooFilter {
                val bytes = datastoreValue.bytes
                val byteInputStream = ByteArrayInputStream(bytes)
                val objectInputStream = ObjectInputStream(byteInputStream)
                return objectInputStream.readObject() as NanoCuckooFilter
            }

            override fun saveValue(pojoValue: NanoCuckooFilter?): Blob {
                val byteOutputStream = ByteArrayOutputStream()
                val objectOutputStream = ObjectOutputStream(byteOutputStream)
                objectOutputStream.writeObject(pojoValue)
                val bytes = byteOutputStream.toByteArray()
                return Blob(bytes)
            }

        }
    }
}