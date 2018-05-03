package com.yoloo.server.user.infrastructure.configuration

import com.google.appengine.api.taskqueue.Queue
import com.yoloo.server.DemoPullServlet
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ServerSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/api/**")
            .permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("krialix")
            .password("1234")
            .roles("DEMO")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun pullServlet(@Qualifier("subscription-queue") queue: Queue) : ServletRegistrationBean<DemoPullServlet> {
        val bean = ServletRegistrationBean(DemoPullServlet(queue), "/api/v1/tasks/pull")
        bean.setLoadOnStartup(1)
        return bean
    }
}