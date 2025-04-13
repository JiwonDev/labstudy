package com.study

/**
 * MyArrayList 로 구현
 */
class MyStack<T> {
    private val items = MyArrayList<T>()

    fun isEmpty(): Boolean = items.isEmpty()

    fun size(): Int = items.size()

    fun push(element: T) {
        items.add(element)
    }

    fun pop(): T? {
        return if (isEmpty()) null else items.removeAt(items.size() - 1)
    }

    fun peek(): T? {
        return if (isEmpty()) null else items.get(items.size() - 1)
    }

    fun peekAt(index: Int): T {
        return items.get(index)
    }

    override fun toString(): String {
        if (isEmpty()) return "[]"
        return (items.size() - 1 downTo 0)
            .joinToString(separator = ", ", prefix = "[", postfix = "]") { items.get(it).toString() }
    }
}
