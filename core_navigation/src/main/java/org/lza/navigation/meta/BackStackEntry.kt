package org.lza.navigation.meta

import org.lza.navigation.interfaces.BackStackInfo
import org.lza.navigation.interfaces.Navigator


/**
 * @author liuzhongao
 * @Date 2022/8/26 14:24
 */
internal class BackStackEntry(val target: Navigator.Destination, override val index: Int, internal val path: String): BackStackInfo {

    val id: String get() = "${target.hashCode()}_$index"

    override val name: String = path
}