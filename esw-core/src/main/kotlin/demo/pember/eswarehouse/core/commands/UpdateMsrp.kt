package demo.pember.eswarehouse.core.commands

import demo.pember.eswarehouse.core.commands.support.InstantSkuCommand
import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode

class UpdateMsrp(
    employeeId: EmployeeId,
    val code: SkuCode,
    val updatedPrice: Long
    ): InstantSkuCommand(employeeId, code)