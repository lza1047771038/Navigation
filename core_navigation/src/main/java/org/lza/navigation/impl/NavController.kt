package org.lza.navigation.impl

import org.lza.navigation.interfaces.Arguments
import org.lza.navigation.interfaces.BackStackInfo
import org.lza.navigation.interfaces.INavController
import org.lza.navigation.interfaces.Navigator
import org.lza.navigation.utils.getDestination
import org.lza.navigation.meta.*
import org.lza.navigation.utils.ArgumentsInitializer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

/**
 * @author: liuzhongao
 * @date: 2022/9/8 15:02
 */
class NavController : INavController {

    companion object {
        private const val TAG = "Navigator"
    }

    private val _innerNavigators: MutableMap<Class<*>, Navigator<*, *>> = ConcurrentHashMap()
    private val _backStack = LinkedList<BackStackEntry>()

    private val backStackCount: Int get() = _backStack.size

    override val backStackInfo: List<BackStackInfo> get() = ArrayList(_backStack)

    override fun addNavigatorFactory(reset: Boolean, factory: Navigator.Factory) {
        if (reset) {
            _innerNavigators.clear()
        }
        factory.create().forEach { registerNavigator(it) }
    }

    override fun registerNavigator(navigator: Navigator<*, *>) {
        _innerNavigators[navigator.type] = navigator
    }

    override fun <T : Navigator<*, *>> getNavigator(host: Class<*>): T {
        return _innerNavigators[host] as T
    }

    override fun navigate(destination: String, options: NavOptions?): Boolean {
        return this.navigate<Any>(destination, options) {}
    }

    override fun <T : Any> navigate(destination: String, options: NavOptions?, arguments: ArgumentsInitializer<T>): Boolean {
        val navDestination = getDestination(destination) ?: return false
        return navigateInternal(navDestination, options ?: navDestination.options, arguments as ArgumentsInitializer<Any>)
    }

    override fun replaceWith(destination: String, options: NavOptions?): Boolean {
        val navDestination = getDestination(destination) ?: return false
        return navigateInternal(navDestination, options ?: navDestination.options) {}
    }

    override fun navigateUp(options: NavOptions?): Boolean {
        return popBackStack(options)
    }

    private fun navigateInternal(destination: Navigator.Destination, options: NavOptions, argumentInit: ArgumentsInitializer<Any>): Boolean {
        val navigator = getNavigator(destination.type) as? Navigator<Navigator.Destination, Any> ?: return false

        val arguments = navigator.provideArguments()
        arguments.argumentInit()
        if (options.mode != LaunchMode.Normal && navigator.processLaunchMode(destination, options, arguments)) {
            return true
        }
        if (options.popUpTo.isNotEmpty()) {
            popBackStack(options)
        }

        return realNavigateInternal(destination, navigator, arguments, options)
    }

    private fun <D : Navigator.Destination, A : Any> realNavigateInternal(destination: D, navigator: Navigator<D, A>, arguments: Arguments<A>, options: NavOptions): Boolean {
        navigator.navigate(destination, options, arguments)
        val backStackEntry = BackStackEntry(destination, backStackCount, destination.path)
        _backStack.add(backStackEntry)
        return true
    }

    override fun popBackStack(popUpTo: String): Boolean {
        return popBackStack(BaseOptions(popUpTo = popUpTo))
    }

    override fun popBackStack(options: BaseOptions?): Boolean {
        return if (options == null || options.popUpTo.isEmpty()) {
            if (backStackCount > 1) {
                popBackStackInternal()
            } else {
                false
            }
        } else {
            if (backStackCount > 1) {
                var popBackToIndex = if (options.popDesc) {
                    _backStack.indexOfLast { it.path == options.popUpTo }
                } else {
                    _backStack.indexOfFirst { it.path == options.popUpTo }
                }
                if (popBackToIndex > 0 && options.inclusive) {
                    popBackToIndex--
                }
                if (popBackToIndex >= 0) {
                    for (index in 0 until (backStackCount - 1 - popBackToIndex)) {
                        popBackStackInternal()
                    }
                } else {
                    popBackStackInternal()
                }
                true
            } else {
                false
            }
        }
    }

    /**
     * @return true means this method modified the backstack
     */
    private fun Navigator<Navigator.Destination, Any>.processLaunchMode(destination: Navigator.Destination, options: NavOptions, argument: Arguments<Any>): Boolean {
        if (options.mode == LaunchMode.SingleTop) {
            val last = _backStack.lastOrNull() ?: return false
            if (last.path == destination.path) {
                this.newAttempt(destination, argument)
                return true
            }
        } else if (options.mode == LaunchMode.SingleTask) {
            val popBackStackIndex = _backStack.indexOfFirst { it.path == destination.path }
            if (popBackStackIndex < 0) {
                return false
            }
            for (index in 0 until (backStackCount - 1 - popBackStackIndex)) {
                popBackStackInternal()
            }
            val last = _backStack.lastOrNull() ?: return false
            if (last.path == destination.path) {
                this.newAttempt(destination, argument)
            }
            return true
        }
        return false
    }

    private fun popBackStackInternal(): Boolean {
        if (_backStack.isEmpty()) {
            return false
        }
        val last = _backStack.peekLast() ?: return false
        val navigator = getNavigator(last.target.type) as? Navigator<Navigator.Destination, Any>
        if (navigator != null) {
            navigator.popBackStack(last.target)
            _backStack.removeLast()
        }
        return true
    }
}