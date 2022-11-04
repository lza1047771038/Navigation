package org.lza.navigation

import org.lza.navigation.interfaces.Arguments
import org.lza.navigation.interfaces.Navigator
import org.lza.navigation.meta.NavOptions

class TestNavigator: Navigator<TestNavigator.Destination, Map<String, Any>> {

    class MapArguments: Arguments<Map<String, Any>> {
        private val contains = HashMap<String, Any>()

        override val extras: Map<String, Any>
            get() = contains

        override fun <T : Any> set(key: String, value: T) {
            contains[key] = value
        }

        override fun <T : Any> get(key: String, default: T): T {
            return (contains[key] as? T) ?: default
        }

        override fun clear() = contains.clear()

        override fun putAll(another: Map<String, Any>?) {
            another ?: return
            contains.putAll(another)
        }
    }

    class Destination(
        override val className: String,
        override val path: String,
        override val options: NavOptions
    ) : Navigator.Destination {
        override val type: Class<*> = Map::class.java
    }

    override val type: Class<*>
        get() = Map::class.java

    override fun provideArguments(): Arguments<Map<String, Any>> {
        return MapArguments()
    }

    override fun popBackStack(currentDestination: Destination) {

    }

    override fun navigate(destination: Destination, options: NavOptions?, arguments: Arguments<Map<String, Any>>) {

    }

    override fun newAttempt(destination: Destination, args: Arguments<Map<String, Any>>) {

    }
}