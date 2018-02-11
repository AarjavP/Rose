package me.phly.util
// Gist: https://gist.github.com/Miha-x64/5f626228b34175f827734596d6701008

import java.util.*

// maps

inline fun <reified K : Enum<K>, V> enumMapOf(): MutableMap<K, V> {
    return EnumMap<K, V>(K::class.java)
}

inline fun <reified K : Enum<K>, V> enumMapOf(k: K, v: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k, v)
    return map
}

inline fun <reified K : Enum<K>, V> enumMapOf(k0: K, v0: V, k1: K, v1: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k0, v0)
    map.put(k1, v1)
    return map
}

inline fun <reified K : Enum<K>, V> enumMapOf(k0: K, v0: V, k1: K, v1: V, k2: K, v2: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k0, v0)
    map.put(k1, v1)
    map.put(k2, v2)
    return map
}

inline fun <reified K : Enum<K>, V> enumMapOf(k0: K, v0: V, k1: K, v1: V, k2: K, v2: V, k3: K, v3: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k0, v0)
    map.put(k1, v1)
    map.put(k2, v2)
    map.put(k3, v3)
    return map
}

inline fun <reified K : Enum<K>, V> enumMapOf(k0: K, v0: V, k1: K, v1: V, k2: K, v2: V, k3: K, v3: V, k4: K, v4: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k0, v0)
    map.put(k1, v1)
    map.put(k2, v2)
    map.put(k3, v3)
    map.put(k4, v4)
    return map
}

inline fun <reified K : Enum<K>, V> enumMapOf(k0: K, v0: V, k1: K, v1: V, k2: K, v2: V, k3: K, v3: V, k4: K, v4: V, k5: K, v5: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k0, v0)
    map.put(k1, v1)
    map.put(k2, v2)
    map.put(k3, v3)
    map.put(k4, v4)
    map.put(k5, v5)
    return map
}

inline fun <reified K : Enum<K>, V> enumMapOf(k0: K, v0: V, k1: K, v1: V, k2: K, v2: V, k3: K, v3: V, k4: K, v4: V, k5: K, v5: V, k6: K, v6: V): MutableMap<K, V> {
    val map = EnumMap<K, V>(K::class.java)
    map.put(k0, v0)
    map.put(k1, v1)
    map.put(k2, v2)
    map.put(k3, v3)
    map.put(k4, v4)
    map.put(k5, v5)
    map.put(k6, v6)
    return map
}

// sets

inline fun <reified E : Enum<E>> enumSetOf(): MutableSet<E> = EnumSet.noneOf(E::class.java)

inline fun <E : Enum<E>> enumSetOf(single: E): MutableSet<E> = EnumSet.of(single)

inline fun <E : Enum<E>> enumSetOf(e0: E, e1: E): MutableSet<E> = EnumSet.of(e0, e1)

inline fun <E : Enum<E>> enumSetOf(e0: E, e1: E, e2: E): MutableSet<E> = EnumSet.of(e0, e1, e2)

// transforms

/**
 * Variation of groupBy optimized for enum keys; however, this map has is not linked, it has its own order.
 */
@JvmName("groupByEnum")
inline fun <T, reified K : Enum<K>> Iterable<T>.groupBy(keySelector: (T) -> K): Map<K, List<T>> {
    return groupByTo(EnumMap<K, MutableList<T>>(K::class.java), keySelector)
}

@JvmName("associateByEnum")
inline fun <T, reified K : Enum<K>> Iterable<T>.associateBy(keySelector: (T) -> K): MutableMap<K, T> {
    return associateByTo(EnumMap(K::class.java), keySelector)
}
