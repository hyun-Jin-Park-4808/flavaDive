package fd.flavadive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FlavaDiveApplication

fun main(args: Array<String>) {
    runApplication<FlavaDiveApplication>(*args)
}