package demo.pember.eswarehouse.users

import demo.pember.eswarehouse.core.identifiers.EmployeeId
import io.micronaut.http.HttpRequest

/**
 * In a real system, this class might be responsible for determining details about the 'principal' or user associated
 * with the provided request. For example, it could validate a JWT value and return details.
 *
 * However, here we just use a canned user
 */
class UserLookupService {

    fun determineEmployeeId(request: HttpRequest<*>): EmployeeId {

        return if (request.userPrincipal.isPresent) {
            EmployeeId(request.userPrincipal.get().name)
        } else {
            EmployeeId("test-employee@testing.com")
        }
    }
}