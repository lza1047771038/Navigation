package org.lza.navigation

import org.lza.navigation.impl.NavController
import org.lza.navigation.meta.BaseOptions
import org.lza.navigation.meta.LaunchMode
import org.lza.navigation.meta.NavOptions
import org.lza.navigation.utils.addDestination

const val PATH_MAIN = "home/main"
const val PATH_MINE = "home/mine"
const val PATH_LOGIN_MAIN = "login/main"
const val PATH_LOGIN_PHONE = "login/phone"
const val PATH_LOGIN_VERIFY = "login/verify"

fun main() {
    val controller = NavController()
    controller.registerNavigator(TestNavigator())

    addDestination(TestNavigator.Destination(PATH_MAIN, PATH_MAIN, NavOptions(mode = LaunchMode.SingleTask)))
    addDestination(TestNavigator.Destination(PATH_MINE, PATH_MINE, NavOptions(mode = LaunchMode.SingleTask)))
    addDestination(TestNavigator.Destination(PATH_LOGIN_MAIN, PATH_LOGIN_MAIN, NavOptions(mode = LaunchMode.SingleTask)))
    addDestination(TestNavigator.Destination(PATH_LOGIN_PHONE, PATH_LOGIN_PHONE, NavOptions()))
    addDestination(TestNavigator.Destination(PATH_LOGIN_VERIFY, PATH_LOGIN_VERIFY, NavOptions(popUpTo = PATH_LOGIN_MAIN)))

    controller.navigate(PATH_MAIN)
    controller.printBackStack()

    controller.replaceWith(PATH_MINE)
    controller.printBackStack()

    controller.navigate(PATH_MINE)
    controller.printBackStack()

    controller.navigate(PATH_MINE)
    controller.printBackStack()

    controller.popBackStack(BaseOptions(popUpTo = PATH_MAIN))
    controller.printBackStack()

    controller.navigate(PATH_MAIN)
    controller.printBackStack()

    controller.navigate(PATH_LOGIN_MAIN)
    controller.printBackStack()

    controller.navigate(PATH_LOGIN_PHONE)
    controller.printBackStack()

    controller.navigate(PATH_LOGIN_VERIFY)
    controller.printBackStack()

    controller.navigate(PATH_LOGIN_MAIN)
    controller.printBackStack()
}

private fun NavController.printBackStack() {
    println(backStackInfo.joinToString(", ") { it.name })
}