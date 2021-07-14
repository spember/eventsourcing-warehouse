package demo.pember.eswarehouse.controllers

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
class RootController {

    @Get(value="/", produces = [MediaType.TEXT_PLAIN])
    fun index(): String {
        return "Testing"
    }
}