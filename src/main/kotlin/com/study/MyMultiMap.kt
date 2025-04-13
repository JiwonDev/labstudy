package com.study

/**
 * MyHashMap<K, MyArrayList<V>>() 을 이용한 MyMultiMap
 * - key : List<V> 형태로 저장
 */
class MyMultiMap<K, V> {
    private val map = MyHashMap<K, MyArrayList<V>>()

    fun put(key: K, value: V) {
        val currentList = map.get(key)

        if (currentList != null) {
            currentList.add(value)
            return
        }

        val newList = MyArrayList<V>()
        map.put(key, newList)
        newList.add(value)
    }

    fun get(key: K): List<V> {
        val list = map.get(key) ?: return emptyList()
        return List(list.size()) { index -> list.get(index) }
    }

    fun remove(key: K, value: V): Boolean {
        val list = map.get(key) ?: return false
        for (i in 0 until list.size()) {
            if (list.get(i) == value) {
                list.removeAt(i)
                if (list.isEmpty()) map.remove(key)
                return true
            }
        }
        return false
    }

    fun removeAll(key: K): List<V> {
        val list = map.remove(key) ?: return emptyList()
        return List(list.size()) { index -> list.get(index) }
    }

    fun containsKey(key: K): Boolean = map.containsKey(key)

    fun containsEntry(key: K, value: V): Boolean {
        val list = map.get(key) ?: return false
        for (i in 0 until list.size()) {
            if (list.get(i) == value) return true
        }
        return false
    }

    fun keys(): List<K> {
        val keys = mutableListOf<K>()
        for (entry in map.entries()) {
            keys.add(entry.key)
        }
        return keys
    }

    fun values(): List<V> {
        val result = mutableListOf<V>()
        for (entry in map.entries()) {
            val list = entry.value
            for (v in list) {
                result.add(v)
            }
        }
        return result
    }

    fun entries(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        for (entry in map.entries()) {
            val key = entry.key
            for (v in entry.value) {
                result.add(key to v)
            }
        }
        return result
    }

    fun size(): Int {
        var total = 0
        for (entry in map.entries()) {
            total += entry.value.size()
        }
        return total
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }
}
