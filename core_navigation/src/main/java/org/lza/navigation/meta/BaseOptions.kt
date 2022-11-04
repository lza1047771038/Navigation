package org.lza.navigation.meta


/**
 * author: liuzhongao
 * date: 2022/8/29 16:58
 */
open class BaseOptions(
    val popUpTo: String = "",
    val inclusive: Boolean = false,
    val popDesc: Boolean = true,
    val mode: LaunchMode = LaunchMode.Normal
)