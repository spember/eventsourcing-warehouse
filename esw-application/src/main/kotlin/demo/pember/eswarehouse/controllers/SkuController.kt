package demo.pember.eswarehouse.controllers

import EmployeeId
import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.identifiers.SkuCode
import demo.pember.eswarehouse.core.sku.SKU
import demo.pember.eswarehouse.core.sku.SkuService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import javax.inject.Inject

/**
 * Supports basic operations with the managing of a single sku or small groups of skus
 */
@Controller("/sku")
class SkuController(@Inject private val skuService: SkuService) {

    @Get(value="{foo}", produces = [MediaType.APPLICATION_JSON])
    fun fetch(foo: String): SkuDetails? {
        return skuService.fetch(SkuCode(foo))?.let {
            println("Ah I have sku: ${it}")
            SkuDetails.from(it)
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Post
    fun register(@Body parameters: RegisterSkuParameters): SkuDetails {
        return SkuDetails.from(skuService.registerSku(
            RegisterSku(
                EmployeeId("test@test.com"),
                SkuCode(parameters.requestedCode),
                parameters.name
            )
        ))
    }

}