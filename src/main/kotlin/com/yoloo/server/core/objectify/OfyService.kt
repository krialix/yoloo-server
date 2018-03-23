package com.yoloo.server.core.objectify

import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyFactory
import com.googlecode.objectify.ObjectifyService
import com.yoloo.server.core.objectify.translator.Jsr310Translators
import com.yoloo.server.core.user.LocaleTranslatorFactory
import com.yoloo.server.core.user.domain.User
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch
import javax.annotation.ManagedBean
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

@ManagedBean
class OfyService : ServletContextListener {

    override fun contextInitialized(sce: ServletContextEvent?) {
        val factory = ObjectifyService.factory()
        Jsr310Translators.addTo(factory.translators)
        factory.translators.add(LocaleTranslatorFactory())

        val stopWatch = StopWatch()
        stopWatch.start();

        factory.register(User::class.java)

        stopWatch.stop()

        log.info("Entities registered in ${stopWatch.totalTimeMillis} ms")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {

    }

    companion object {
        val log = LoggerFactory.getLogger(OfyService::class.java)

        fun ofy(): Objectify {
            return ObjectifyService.ofy()
        }

        fun factory(): ObjectifyFactory {
            return ObjectifyService.factory()
        }
    }
}
