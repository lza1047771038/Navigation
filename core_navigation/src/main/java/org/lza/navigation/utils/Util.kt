package org.lza.navigation.utils

import org.lza.navigation.interfaces.Navigator
import java.util.concurrent.ConcurrentHashMap

private val destinations = ConcurrentHashMap<String, Navigator.Destination>()

fun addDestination(destination: Navigator.Destination) {
    destinations[destination.path] = destination
}

fun getDestination(path: String): Navigator.Destination? {
    return destinations[path]
}