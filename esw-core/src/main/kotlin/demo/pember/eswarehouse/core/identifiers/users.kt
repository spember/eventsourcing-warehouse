package demo.pember.eswarehouse.core.identifiers

import io.cqrs.core.identifiers.UserId
import java.util.*

class EmployeeId(employeeKey: String): UserId<String>(employeeKey)

class CustomerId(value: UUID): UserId<UUID>(value)