package demo.pember.eswarehouse

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .defaultEnvironments("dev")
        .packages("demo.pember")
        .start()
}