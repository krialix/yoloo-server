package com.yoloo.server

import com.google.api.gax.grpc.GrpcTransportChannel
import com.google.api.gax.rpc.FixedTransportChannelProvider
import com.google.api.gax.rpc.TransportChannelProvider
import io.grpc.ManagedChannelBuilder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.gcp.autoconfigure.pubsub.GcpPubSubProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@EnableCaching
@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class CoreApplication {

    // WORKAROUND(6/18/2018): Spring Boot Google PubSub integration seems broken when PubSub emulator is active
    @Profile("dev")
    @Bean
    fun transportChannelProvider(gcpPubSubProperties: GcpPubSubProperties): TransportChannelProvider {
        val channel = ManagedChannelBuilder
            .forTarget(gcpPubSubProperties.emulatorHost)
            .usePlaintext()
            .build()
        return FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel))
    }
}

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
