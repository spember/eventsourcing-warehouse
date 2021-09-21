package demo.pember.eswarehouse.core.commands

import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode

/**
 * Signifies that we're applying a discount to a SKU - e.g. it's price changed from MSRP by some amount, typically
 * downwards
 */
class ApplyDiscount(user:EmployeeId, code: SkuCode, val amountChange: Long): InstantSkuCommand(user, code)