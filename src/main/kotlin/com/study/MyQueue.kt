package com.study

/**
 * Queue 를 Stack 2개로 구현
 * - enqueue : stackIn 에 push
 * - dequeue : stackOut 에 pop
 * - stackOut 이 비어있으면 stackIn 의 모든 요소를 stackOut 으로 옮긴다. (리스트 뒤쪽으로만 추가하기 위함)
 */
class MyQueue<T> {
    private val stackIn = MyStack<T>()
    private val stackOut = MyStack<T>()

    fun enqueue(element: T) {
        stackIn.push(element)
    }

    fun dequeue(): T? {
        // stackOut 이 없으면 stackIn 전체 -> stackOut 으로 옮긴다.
        if (stackOut.isEmpty()) {
            while (!stackIn.isEmpty()) {
                val item = stackIn.pop() ?: continue
                stackOut.push(item)
            }
        }
        return stackOut.pop()
    }

    fun peek(): T? {
        if (stackOut.isEmpty()) {
            while (!stackIn.isEmpty()) {
                stackOut.push(stackIn.pop()!!)
            }
        }
        return stackOut.peek()
    }

    fun isEmpty() = stackIn.isEmpty() && stackOut.isEmpty()

    override fun toString(): String {
        val tempList = MyArrayList<T>()

        for (i in stackOutToList()) {
            tempList.add(i)
        }

        for (i in stackInToList().asReversed()) {
            tempList.add(i)
        }

        return tempList.toString()
    }

    private fun stackOutToList(): List<T> {
        return (0 until stackOut.size()).map { stackOut.peekAt(it) }
    }

    private fun stackInToList(): List<T> {
        return (0 until stackIn.size()).map { stackIn.peekAt(it) }
    }
}