package com.study

import kotlin.math.absoluteValue

/**
 * Array[ MyArrayList ] 로 구현한 MyHashMap
 * - hash function : |key.hashCode()| % bucketCount
 * - 전체 버켓 리스트 중 80% 이상이 사용되고 있다면 버켓을 2배로 늘리고 리인덱싱
 */
class MyHashMap<K, V> {
    companion object {
        fun <K, V> create(): MyHashMap<K, V> = MyHashMap()
        private const val RESIZE_TOTAL_RATIO = 0.7
        private const val DEFAULT_BUCKET_COUNT = 16
    }

    data class Entry<K, V>(val key: K, var value: V)


    private var bucketList = Array<MyArrayList<Entry<K, V>>>(DEFAULT_BUCKET_COUNT) {
        MyArrayList()
    }

    private var totalCount = 0

    fun put(key: K, value: V) {
        val index = getIndex(key = key, bucketCount = bucketList.size)
        val bucket = bucketList[index]
        for (entry in bucketList[index]) {
            if (entry.key == key) {
                entry.value = value
                return
            }
        }
        // new entry
        bucket.add(Entry(key, value))
        totalCount++

        // 일정 비율 이상 사용중이면 크기 증가
        if (totalCount > (bucketList.size * RESIZE_TOTAL_RATIO))
            reindexing(newBucketCount = bucketList.size * 2)
    }

    fun get(key: K): V? {
        val index = getIndex(key = key, bucketCount = bucketList.size)
        val bucket = bucketList[index]
        for (entry in bucket) {
            if (entry.key == key) return entry.value
        }
        return null
    }

    fun remove(key: K): V? {
        val index = getIndex(key = key, bucketCount = bucketList.size)
        val bucket = bucketList[index]

        for (i in 0 until bucket.size()) {
            val entry: Entry<K, V> = bucket.get(i)
            if (entry.key == key) {
                bucket.removeAt(i)
                totalCount--
                return entry.value
            }
        }
        return null
    }

    fun containsKey(key: K): Boolean {
        return get(key) != null
    }

    fun size(): Int {
        return totalCount
    }

    fun isEmpty(): Boolean {
        return totalCount <= 0
    }

    fun entries(): List<Entry<K, V>> {
        val result = mutableListOf<Entry<K, V>>()
        for (bucket in bucketList) {
            for (entry in bucket) {
                result.add(entry)
            }
        }
        return result
    }


    private fun getIndex(key: K, bucketCount: Int): Int {
        return key.hashCode().absoluteValue % bucketCount
    }


    private fun reindexing(newBucketCount: Int) {
        val oldBucketList: Array<MyArrayList<Entry<K, V>>> = bucketList
        bucketList = Array(newBucketCount) { MyArrayList() }

        for (bucket in oldBucketList) {
            for (entry in bucket) {
                val index = getIndex(entry.key, bucketList.size)
                bucketList[index].add(entry)
            }
        }
    }
}
