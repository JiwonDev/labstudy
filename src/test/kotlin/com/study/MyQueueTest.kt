package com.study

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlin.random.Random
import kotlin.system.measureTimeMillis

private fun printPerformanceComparison(
    name: String,
    firstLabel: String,
    firstTime: Long,
    secondLabel: String,
    secondTime: Long,
) {
    println("$firstLabel - $name $firstTime ms")
    println("$secondLabel - $name $secondTime ms")

    val diff = secondTime - firstTime
    val faster = if (diff > 0) "⏩ $firstLabel 가 ${diff}ms 더 빠릅니다" else "🐢 $firstLabel 가 ${-diff}ms 더 느립니다"
    println("> ($name) $faster\n")
}

class MyQueueTest : FreeSpec({

    "기본 동작 테스트" - {
        "enqueue, dequeue 순서 테스트" {
            val queue = MyQueue<Int>()
            queue.enqueue(1)
            queue.enqueue(2)
            queue.enqueue(3)

            queue.dequeue() shouldBe 1
            queue.dequeue() shouldBe 2
            queue.dequeue() shouldBe 3
        }

        "peek 테스트" {
            val queue = MyQueue<String>()
            queue.enqueue("a")
            queue.enqueue("b")

            queue.peek() shouldBe "a"
            queue.dequeue() shouldBe "a"
            queue.peek() shouldBe "b"
            queue.dequeue() shouldBe "b"
        }

        "isEmpty 테스트" {
            val queue = MyQueue<Int>()
            queue.isEmpty() shouldBe true
            queue.enqueue(10)
            queue.isEmpty() shouldBe false
            queue.dequeue()
            queue.isEmpty() shouldBe true
        }
    }

    "비어있을 때 dequeue / peek" - {
        "dequeue from empty queue" {
            val queue = MyQueue<Double>()
            queue.dequeue() shouldBe null
        }

        "peek from empty queue" {
            val queue = MyQueue<Double>()
            queue.peek() shouldBe null
        }
    }

    "enqueue 후 dequeue 1개, enqueue 추가 후 순서 유지" {
        val queue = MyQueue<Int>()
        queue.enqueue(1)
        queue.enqueue(2)
        queue.dequeue() shouldBe 1
        queue.enqueue(3)
        queue.dequeue() shouldBe 2
        queue.dequeue() shouldBe 3
    }

    "성능 테스트 - enqueue & dequeue 50,000,000개" - {
        val n = 50_000_000

        "순차 증가값" {
            val myQueue = MyQueue<Int>()
            val stdQueue = ArrayDeque<Int>()

            val enqueueMyTime = measureTimeMillis {
                for (i in 0 until n) myQueue.enqueue(i)
            }
            val enqueueStdTime = measureTimeMillis {
                for (i in 0 until n) stdQueue.addLast(i)
            }
            printPerformanceComparison("enqueue sequential", "MyQueue", enqueueMyTime, "ArrayDeque", enqueueStdTime)

            val dequeueMyTime = measureTimeMillis {
                while (!myQueue.isEmpty()) {
                    myQueue.dequeue()
                }
            }
            val dequeueStdTime = measureTimeMillis {
                while (stdQueue.isNotEmpty()) {
                    stdQueue.removeFirst()
                }
            }
            printPerformanceComparison("dequeue sequential", "MyQueue", dequeueMyTime, "ArrayDeque", dequeueStdTime)
        }

        "랜덤값" {
            val values = List(n) { Random.nextInt() }
            val myQueue = MyQueue<Int>()
            val stdQueue = ArrayDeque<Int>()

            val enqueueMyTime = measureTimeMillis {
                for (v in values) myQueue.enqueue(v)
            }
            val enqueueStdTime = measureTimeMillis {
                for (v in values) stdQueue.addLast(v)
            }
            printPerformanceComparison("enqueue random", "MyQueue", enqueueMyTime, "ArrayDeque", enqueueStdTime)

            val dequeueMyTime = measureTimeMillis {
                while (!myQueue.isEmpty()) {
                    myQueue.dequeue()
                }
            }
            val dequeueStdTime = measureTimeMillis {
                while (stdQueue.isNotEmpty()) {
                    stdQueue.removeFirst()
                }
            }
            printPerformanceComparison("dequeue random", "MyQueue", dequeueMyTime, "ArrayDeque", dequeueStdTime)
        }

        "min/max 반복값" {
            val myQueue = MyQueue<Int>()
            val stdQueue = ArrayDeque<Int>()

            val enqueueMyTime = measureTimeMillis {
                for (i in 0 until n) {
                    val value = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    myQueue.enqueue(value)
                }
            }
            val enqueueStdTime = measureTimeMillis {
                for (i in 0 until n) {
                    val value = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    stdQueue.addLast(value)
                }
            }
            printPerformanceComparison("enqueue min/max", "MyQueue", enqueueMyTime, "ArrayDeque", enqueueStdTime)

            val dequeueMyTime = measureTimeMillis {
                while (!myQueue.isEmpty()) {
                    myQueue.dequeue()
                }
            }
            val dequeueStdTime = measureTimeMillis {
                while (stdQueue.isNotEmpty()) {
                    stdQueue.removeFirst()
                }
            }
            printPerformanceComparison("dequeue min/max", "MyQueue", dequeueMyTime, "ArrayDeque", dequeueStdTime)
        }
    }
})
