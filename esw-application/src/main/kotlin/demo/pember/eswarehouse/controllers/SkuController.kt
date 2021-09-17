package demo.pember.eswarehouse.controllers

import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode
import demo.pember.eswarehouse.core.sku.SkuService
import demo.pember.eswarehouse.users.UserLookupService
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import javax.inject.Inject

/**
 * Supports basic operations with the managing of a single sku or small groups of skus
 */
@Controller("/sku")
class SkuController(
    @Inject private val skuService: SkuService,
    @Inject private val userLookupService: UserLookupService
    ) {

    @Get(value="{foo}", produces = [MediaType.APPLICATION_JSON])
    fun fetch(foo: String): SkuDetails? {
        return skuService.fetch(SkuCode(foo))?.let {
            SkuDetails.from(it)
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Post
    fun register(request: HttpRequest<RegisterSkuParameters>): SkuDetails {
        return SkuDetails.from(skuService.registerSku(
            RegisterSku(
                userLookupService.determineEmployeeId(request),
                SkuCode(request.body.get().requestedCode),
                request.body.get().name
            )
        ))
    }
//
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Patch
//    fun adjustMsrp(request: HttpRequest<Long>): SkuDetails {
//
//
//    }

}