package demo.pember.eswarehouse.core.commands

import EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode
import io.cqrs.core.Command
import java.time.Instant

/**
 * Signifies the attempt to register a new SKU within our warehouse.
 */
class RegisterSku(
    user: EmployeeId,
    val chosenSku: SkuCode,
    val name: String
    ): Command<EmployeeId>(user, Instant.now()) {
}