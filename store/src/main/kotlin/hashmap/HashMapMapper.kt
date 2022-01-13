package com.hexagonkt.store.hashmap

import com.hexagonkt.core.converters.convert
import com.hexagonkt.core.filterEmpty
import com.hexagonkt.core.logging.logger
import com.hexagonkt.store.Mapper
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

class HashMapMapper<T: Any>(private val type: KClass<T>): Mapper<T> {

    override val fields: Map<String, KProperty1<T, *>> by lazy {
        logger.time("REFLECT") { type.declaredMemberProperties }.associateBy { it.name }
    }

    override fun toStore(instance: T): Map<String, Any>  =
        instance.convert(Map::class)
            .filterEmpty()
            .mapKeys { it.key.toString() }
            .mapValues { it.value }

    @Suppress("UNCHECKED_CAST")
    override fun fromStore(map: Map<String, Any>): T =
        map.filterEmpty().convert(type)

    override fun fromStore(property: String, value: Any): Any =
        value
}
