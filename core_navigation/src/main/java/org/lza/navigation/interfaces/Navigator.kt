package org.lza.navigation.interfaces

import org.lza.navigation.meta.NavOptions

interface Navigator<D : Navigator.Destination, ArgumentsType: Any> {

    val type: Class<*>

    fun provideArguments(): Arguments<ArgumentsType>

    fun newAttempt(destination: D, args: Arguments<ArgumentsType>)

    fun navigate(destination: D, options: NavOptions?, arguments: Arguments<ArgumentsType>)

    fun popBackStack(currentDestination: D)

    interface Destination {
        val className: String
        val path: String
        val type: Class<*>
        val options: NavOptions
    }

    interface Factory {
        fun create(): List<Navigator<*, *>>
    }
}