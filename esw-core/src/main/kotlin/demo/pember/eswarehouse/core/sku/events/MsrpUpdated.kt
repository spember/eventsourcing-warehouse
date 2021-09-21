package demo.pember.eswarehouse.core.sku.events

import io.cqrs.core.event.Event

class MsrpUpdated(val updatedMsrp: Long): Event