package com.study

import kotlin.math.max

class MyTreeMap<K : Comparable<K>, V> {
    private var root: Node? = null

    inner class Node(
        val key: K,
        var value: V,
        var left: Node? = null,
        var right: Node? = null,
        var height: Int = 1,
    )

    fun put(key: K, value: V) {
        root = put(root, key, value)
    }

    private fun put(node: Node?, key: K, value: V): Node {
        node ?: return Node(key, value)

        when {
            key < node.key -> node.left = put(node.left, key, value)
            key > node.key -> node.right = put(node.right, key, value)
            else -> {
                node.value = value
                return node
            }
        }

        updateHeight(node)
        return rebalance(node)
    }

    fun get(key: K): V? {
        var current = root
        while (current != null) {
            current = when {
                key < current.key -> current.left
                key > current.key -> current.right
                else -> return current.value
            }
        }
        return null
    }

    fun containsKey(key: K): Boolean = get(key) != null

    enum class TreeOrder {
        IN_ORDER,
        PRE_ORDER,
        POST_ORDER
    }

    fun iter(order: TreeOrder = TreeOrder.IN_ORDER): List<Pair<K, V>> {
        return when (order) {
            TreeOrder.IN_ORDER -> inOrderIterative()
            TreeOrder.PRE_ORDER -> {
                val result = mutableListOf<Pair<K, V>>()
                preOrder(root, result)
                result
            }

            TreeOrder.POST_ORDER -> {
                val result = mutableListOf<Pair<K, V>>()
                postOrder(root, result)
                result
            }
        }
    }

    private fun inOrderIterative(): List<Pair<K, V>> {
        val result = mutableListOf<Pair<K, V>>()
        val stack = ArrayDeque<Node>()
        var current = root

        while (current != null || stack.isNotEmpty()) {
            while (current != null) {
                stack.addLast(current)
                current = current.left
            }

            current = stack.removeLast()
            result.add(current.key to current.value)
            current = current.right
        }

        return result
    }

    private fun preOrder(node: Node?, result: MutableList<Pair<K, V>>) {
        if (node != null) {
            result.add(node.key to node.value)
            preOrder(node.left, result)
            preOrder(node.right, result)
        }
    }

    private fun postOrder(node: Node?, result: MutableList<Pair<K, V>>) {
        if (node != null) {
            postOrder(node.left, result)
            postOrder(node.right, result)
            result.add(node.key to node.value)
        }
    }

    override fun toString(): String {
        return iter().joinToString(prefix = "{", postfix = "}") { (k, v) -> "$k=$v" }
    }

    // AVL 관련 함수들
    private fun height(node: Node?): Int = node?.height ?: 0

    private fun updateHeight(node: Node) {
        node.height = 1 + max(
            height(node.left), height(node.right)
        )
    }

    private fun getBalance(node: Node?): Int =
        if (node == null) 0 else height(node.left) - height(node.right)

    private fun rebalance(node: Node): Node {
        val balance = getBalance(node)

        return when {
            // Left Left
            //      3         -->       2
            //     /                   / \
            //    2                   1   3
            //   /
            //  1
            balance > 1 && getBalance(node.left) >= 0 -> {
                rightRotate(node)
            }

            // Left Right
            //    3         -->        3         -->         2
            //   /                    /                     / \
            //  1                    2                     1   3
            //   \                  /
            //    2                1
            balance > 1 && getBalance(node.left) < 0 -> {
                node.left = leftRotate(node.left!!)
                rightRotate(node)
            }

            // Right Right
            //  1           -->         2
            //   \                     / \
            //    2                   1   3
            //     \
            //      3
            balance < -1 && getBalance(node.right) <= 0 -> {
                leftRotate(node)
            }

            // Right Left
            //  1           -->         1           -->       2
            //   \                       \                   / \
            //    3                       2                 1   3
            //   /                         \
            //  2                           3
            balance < -1 && getBalance(node.right) > 0 -> {
                node.right = rightRotate(node.right!!)
                leftRotate(node)
            }

            else -> node
        }
    }

    private fun rightRotate(y: Node): Node {
        //      y                    x
        //     /       -->          / \
        //    x                    z   y
        //   /
        //  z
        val x = y.left!!
        val t2 = x.right

        x.right = y
        y.left = t2

        updateHeight(y)
        updateHeight(x)

        return x
    }

    private fun leftRotate(x: Node): Node {
        //    x                      y
        //     \       -->          / \
        //      y                  x   z
        //       \
        //        z
        val y = x.right!!
        val t2 = y.left

        y.left = x
        x.right = t2

        updateHeight(x)
        updateHeight(y)

        return y
    }
}
