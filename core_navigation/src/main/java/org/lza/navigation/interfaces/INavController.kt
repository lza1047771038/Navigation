package org.lza.navigation.interfaces

import org.lza.navigation.meta.BaseOptions
import org.lza.navigation.meta.NavOptions
import org.lza.navigation.utils.ArgumentsInitializer

/**
 * @author: liuzhongao
 * @date: 2022/9/8 14:07
 */
interface INavController {

    val backStackInfo: List<BackStackInfo>

    fun navigate(destination: String, options: NavOptions? = null): Boolean

    fun replaceWith(destination: String, options: NavOptions? = null): Boolean

    fun navigateUp(options: NavOptions? = null): Boolean

    fun addNavigatorFactory(reset: Boolean = true, factory: Navigator.Factory)

    fun registerNavigator(navigator: Navigator<*, *>)

    fun <T : Navigator<*, *>> getNavigator(host: Class<*>): T

    /**
     * @param options may override options declared in Annotation
     */
    fun <T : Any> navigate(destination: String, options: NavOptions? = null, arguments: ArgumentsInitializer<T>): Boolean

    fun popBackStack(popUpTo: String = ""): Boolean

    fun popBackStack(options: BaseOptions?): Boolean
}