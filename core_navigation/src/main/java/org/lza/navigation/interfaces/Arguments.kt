package org.lza.navigation.interfaces

interface Arguments<ArgumentType: Any> {
    val extras: ArgumentType

    operator fun <T: Any> set(key: String, value: T)
    operator fun <T: Any> get(key: String, default: T): T

    fun putAll(another: ArgumentType?)
    fun clear()
}