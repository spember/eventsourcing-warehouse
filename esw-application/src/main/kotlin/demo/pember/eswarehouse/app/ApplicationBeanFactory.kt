package demo.pember.eswarehouse.app

import demo.pember.eswarehouse.users.UserLookupService
import io.micronaut.context.annotation.Factory
import javax.inject.Singleton

/**
 * Instantiates Beans for classes that are specific to the 'application' layer
 */
@Factory
class ApplicationBeanFactory {

    @Singleton
    fun createUserLookupService(): UserLookupService {
        return UserLookupService()
    }
}