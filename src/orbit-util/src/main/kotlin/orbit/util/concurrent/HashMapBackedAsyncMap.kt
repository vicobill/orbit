/*
 Copyright (C) 2015 - 2019 Electronic Arts Inc.  All rights reserved.
 This file is part of the Orbit Project <https://www.orbit.cloud>.
 See license in LICENSE.
 */

package orbit.util.concurrent

import java.util.concurrent.ConcurrentHashMap

abstract class HashMapBackedAsyncMap<K, V> : AsyncMap<K, V> {
    private val map = ConcurrentHashMap<K, V>()

    override suspend fun get(key: K) = map[key]

    override suspend fun set(key: K, value: V) {
        map[key] = value
    }

    override suspend fun remove(key: K): Boolean {
        return map.remove(key) != null
    }

    override suspend fun entries(): Iterable<Pair<K, V>> =
        map.entries.map { it.toPair() }

    override suspend fun compareAndSet(key: K, initialValue: V?, newValue: V?): Boolean =
        runCatching {
            map.compute(key) { _, oldValue ->
                if (initialValue != oldValue) error("Could not map")
                newValue
            }
        }.isSuccess

}