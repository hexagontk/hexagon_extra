package com.hexagonkt.injection

import com.hexagonkt.core.logging.Logger

/**
 * Providers module and injector. This object keep tracks of generator functions or specific
 * instances bound to classes. Different providers can be bound to the same type using 'tags'.
 *
 * @property module Root module with the bindings.
 */
object InjectionManager {

    internal val logger: Logger by lazy { Logger(this::class) }

    val module: Module = Module()
    val injector: Injector = Injector(module)
}
