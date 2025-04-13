package com.study

/**
 * Array 로 구현한 ArrayList
 * - 중간 삽입 시 이전 요소를 뒤로 밀어야 한다.
 * - 초기 사이즈 10, 공간이 부족하면 2배로 늘린다.
 */
class MyArrayList<T> private constructor(
    private var elements: Array<T?>,
) {
    constructor(capacity: Int = 10) : this(arrayOfNulls<Any?>(capacity) as Array<T?>)

    private var totalCount = 0

    fun add(element: T, index: Int = totalCount) {
        if (index < 0 || index > totalCount)
            throw IndexOutOfBoundsException("Index: $index, Size: $totalCount")

        if (totalCount == elements.size)
            resize(newSize = elements.size * 2)

        for (i in totalCount downTo index + 1) {
            elements[i] = elements[i - 1]
        }
        elements[index] = element
        totalCount++
    }

    fun get(index: Int): T {
        if (index < 0 || index >= totalCount) {
            throw IndexOutOfBoundsException("Index: $index, Size: $totalCount")
        }
        return elements[index]
            ?: throw NullPointerException("Element at index $index is null")
    }

    fun set(index: Int, element: T): T {
        if (index < 0 || index >= totalCount) {
            throw IndexOutOfBoundsException("Index: $index, Size: $totalCount")
        }
        val old = elements[index]
            ?: throw NullPointerException("Element at index $index is null")
        elements[index] = element
        return old
    }

    fun removeAt(index: Int): T? {
        if (index < 0 || index >= totalCount) {
            throw IndexOutOfBoundsException("Index: $index, Size: $totalCount")
        }
        val removed = elements[index]
        for (i in index until totalCount - 1) {
            elements[i] = elements[i + 1]
        }
        elements[--totalCount] = null
        return removed
    }

    fun remove(element: T): Boolean {
        val index = elements.indexOf(element)
        if (index == -1) return false
        removeAt(index)
        return true
    }

    fun size(): Int = totalCount

    fun isEmpty(): Boolean = totalCount == 0

    operator fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            var index = 0

            override fun hasNext(): Boolean = index < totalCount

            override fun next(): T {
                if (!hasNext()) throw NoSuchElementException()

                return elements[index++]
                    ?: throw NullPointerException("Element at index $index is null")
            }
        }
    }

    private fun resize(newSize: Int) {
        elements = elements.copyOf(newSize)
    }

    override fun toString(): String {
        return (0 until totalCount).joinToString(separator = ", ", prefix = "[", postfix = "]") {
            elements[it].toString()
        }
    }
}
