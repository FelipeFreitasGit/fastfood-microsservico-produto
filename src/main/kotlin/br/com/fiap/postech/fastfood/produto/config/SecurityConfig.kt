package br.com.fiap.postech.fastfood.produto.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(HttpMethod.POST,"/produto", permitAll)
                authorize(HttpMethod.PUT,"/produto/**", permitAll)
                authorize(HttpMethod.DELETE,"/produto/**", permitAll)
                authorize(HttpMethod.GET, "/produto/**", permitAll)
            }
        }
        http.csrf { it.disable() }
        return http.build()
    }
}