package demo.pember.eswarehouse.core.commands

import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode
import io.cqrs.core.Command
import java.time.Instant

class UpdateMsrp(
    employeeId: EmployeeId,
    val code: SkuCode,
    val updatedPrice: Long
    ): Command<EmployeeId>(employeeId, Instant.now())