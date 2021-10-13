package demo.pember.eswarehouse.core.commands.support

import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode
import io.cqrs.core.Command
import java.time.Instant

/**
 * A shortcut for Command done by Employees for a SKU that occurs `now`
 */
open class InstantSkuCommand(user: EmployeeId, code: SkuCode ): Command<EmployeeId>(user, Instant.now())