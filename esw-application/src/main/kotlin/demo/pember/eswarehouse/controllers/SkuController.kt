package demo.pember.eswarehouse.controllers

import demo.pember.eswarehouse.core.commands.RegisterSku
import demo.pember.eswarehouse.core.commands.UpdateMsrp
import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode
import demo.pember.eswarehouse.core.sku.SkuService
import demo.pember.eswarehouse.users.UserLookupService
import io.cqrs.core.event.EventRepository
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
    @Inject private val eventRepository: EventRepository,
    @Inject private val userLookupService: UserLookupService
    ) {

    @Get(value="{skuCode}", produces = [MediaType.APPLICATION_JSON])
    fun fetch(skuCode: String): SkuDetails? {
        return skuService.fetch(SkuCode(skuCode))?.let {
            SkuDetails.from(it)
        }
    }

    @Get(value="{skuCode}/audit", produces = [MediaType.APPLICATION_JSON])
    fun fetchAudit(skuCode: String): List<PublicEvent> {
        return eventRepository.listAllByIds(SkuCode(skuCode)).map { eventEnvelope ->
            PublicEvent(
                eventEnvelope.eventCoreData.entityId.value.toString(),
                eventEnvelope.eventCoreData.revision,
                eventEnvelope.eventCoreData.createdBy,
                eventEnvelope.eventCoreData.instantOccurred.toString(),
                eventEnvelope.event.javaClass.simpleName
            )
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

    @Consumes(MediaType.APPLICATION_JSON)
    @Put(value="{skuCode}", produces = [MediaType.APPLICATION_JSON])
    fun adjustMsrp(request: HttpRequest<SkuPriceParameters>, skuCode: String): SkuDetails {
        val parameters = request.body.get()
        return SkuDetails.from(skuService.updateMsrp(
            UpdateMsrp(
                userLookupService.determineEmployeeId(request),
                SkuCode(skuCode),
                parameters.requestedMsrp
            )
        ))
    }

}