package org.lza.navigation.meta

/**
 * @author liuzhongao
 * @Date 2022/8/26 14:19
 */
open class NavOptions(
    popUpTo: String = "",
    popDesc: Boolean = true,
    inclusive: Boolean = false,
    mode: LaunchMode = LaunchMode.Normal
): BaseOptions(popUpTo = popUpTo, inclusive = inclusive, popDesc = popDesc, mode = mode)