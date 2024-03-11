package br.com.fiap.postech.fastfood.produto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@ComponentScan(basePackages = arrayOf("br.com.fiap.postech.fastfood.produto*"))
@EnableJpaRepositories("br.com.fiap.postech.fastfood.produto*")
@EntityScan("br.com.fiap.postech.fastfood.produto*")
class FastFoodApplication

fun main(args: Array<String>) {
	runApplication<FastFoodApplication>(*args)
}
