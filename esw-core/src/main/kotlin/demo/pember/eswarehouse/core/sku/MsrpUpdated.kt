package demo.pember.eswarehouse.core.sku

import io.cqrs.core.event.Event

class MsrpUpdated(val updatedMsrp: Long): Event