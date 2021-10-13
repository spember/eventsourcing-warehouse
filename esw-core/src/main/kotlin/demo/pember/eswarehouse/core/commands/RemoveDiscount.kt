package demo.pember.eswarehouse.core.commands

import demo.pember.eswarehouse.core.commands.support.InstantSkuCommand
import demo.pember.eswarehouse.core.identifiers.EmployeeId
import demo.pember.eswarehouse.core.identifiers.SkuCode

class RemoveDiscount(user:EmployeeId, code: SkuCode): InstantSkuCommand(user, code)