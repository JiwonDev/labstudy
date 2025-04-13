package com.study

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.util.*
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

class MyTreeMapTest : FreeSpec({

    "기본 동작 테스트" - {
        "put & get 테스트" {
            val map = MyTreeMap<Int, String>()
            map.put(10, "ten")
            map.put(5, "five")
            map.put(15, "fifteen")

            map.get(10) shouldBe "ten"
            map.get(5) shouldBe "five"
            map.get(15) shouldBe "fifteen"
            map.get(20) shouldBe null
        }

        "containsKey 테스트" {
            val map = MyTreeMap<String, Int>()
            map.put("apple", 1)
            map.put("banana", 2)

            map.containsKey("apple") shouldBe true
            map.containsKey("banana") shouldBe true
            map.containsKey("cherry") shouldBe false
        }

        "중복 키 삽입 시 값 업데이트" {
            val map = MyTreeMap<Int, String>()
            map.put(1, "one")
            map.put(1, "uno")
            map.get(1) shouldBe "uno"
        }
    }

    "트리 순회(iter) 테스트" - {
        val map = MyTreeMap<Int, String>()
        map.put(10, "ten")
        map.put(5, "five")
        map.put(15, "fifteen")
        map.put(3, "three")
        map.put(7, "seven")

        "in-order 순회" {
            val result = map.iter(MyTreeMap.TreeOrder.IN_ORDER)
            result shouldBe listOf(
                3 to "three",
                5 to "five",
                7 to "seven",
                10 to "ten",
                15 to "fifteen"
            )
        }

        "pre-order 순회" {
            val result = map.iter(MyTreeMap.TreeOrder.PRE_ORDER)
            result shouldBe listOf(
                10 to "ten",
                5 to "five",
                3 to "three",
                7 to "seven",
                15 to "fifteen"
            )
        }

        "post-order 순회" {
            val result = map.iter(MyTreeMap.TreeOrder.POST_ORDER)
            result shouldBe listOf(
                3 to "three",
                7 to "seven",
                5 to "five",
                15 to "fifteen",
                10 to "ten"
            )
        }
    }

    "toString 출력 확인" {
        val map = MyTreeMap<Char, Int>()
        map.put('C', 3)
        map.put('A', 1)
        map.put('B', 2)
        map.put('D', 4)

        map.toString() shouldBe "{A=1, B=2, C=3, D=4}" // in-order 기준
    }

    "빈 트리에서 get/contains/iter 안전하게 동작" {
        val map = MyTreeMap<Int, String>()
        map.get(1) shouldBe null
        map.containsKey(1) shouldBe false
        map.iter() shouldBe emptyList()
        map.toString() shouldBe "{}"
    }
    "성능 테스트 - 삽입 (5_000_000)" - {
        val n = 5_000_000

        "순차 증가값" {
            val myMap = MyTreeMap<Int, Int>()
            val stdMap = TreeMap<Int, Int>()

            val myTime = measureTimeMillis {
                for (i in 0 until n) {
                    myMap.put(i, i)
                }
            }

            val stdTime = measureTimeMillis {
                for (i in 0 until n) {
                    stdMap[i] = i
                }
            }

            printPerformanceComparison("put sequential", "MyTreeMap", myTime, "TreeMap", stdTime)
        }

        "랜덤값" {
            val myMap = MyTreeMap<Int, Int>()
            val stdMap = TreeMap<Int, Int>()

            val randoms = List(n) { Random.nextInt() }

            val myTime = measureTimeMillis {
                for (key in randoms) {
                    myMap.put(key, key)
                }
            }

            val stdTime = measureTimeMillis {
                for (key in randoms) {
                    stdMap[key] = key
                }
            }

            printPerformanceComparison("put random", "MyTreeMap", myTime, "TreeMap", stdTime)
        }

        "min/max 반복값" {
            val myMap = MyTreeMap<Int, Int>()
            val stdMap = TreeMap<Int, Int>()

            val myTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    myMap.put(key, key)
                }
            }

            val stdTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    stdMap[key] = key
                }
            }

            printPerformanceComparison("put min/max", "MyTreeMap", myTime, "TreeMap", stdTime)
        }
    }

    "성능 테스트 - 조회 (5_000_000개)" - {
        val n = 5_000_000
        val myMap = MyTreeMap<Int, Int>()
        val stdMap = TreeMap<Int, Int>()

        for (i in 0 until n) {
            myMap.put(i, i)
            stdMap[i] = i
        }

        var dummy = 0
        val myTime = measureTimeMillis {
            for (i in 0 until n) {
                dummy += myMap.get(i) ?: 0
            }
        }

        val stdTime = measureTimeMillis {
            for (i in 0 until n) {
                dummy += stdMap[i] ?: 0
            }
        }

        printPerformanceComparison("get", "MyTreeMap", myTime, "TreeMap", stdTime)

        "랜덤값" - {
            val randomMap = MyTreeMap<Int, Int>()
            val randomStdMap = TreeMap<Int, Int>()
            val keys = List(n) { Random.nextInt() }

            for (key in keys) {
                randomMap.put(key, key)
                randomStdMap[key] = key
            }

            var randomDummy = 0
            val randomMyTime = measureTimeMillis {
                for (key in keys) {
                    randomDummy += randomMap.get(key) ?: 0
                }
            }

            val randomStdTime = measureTimeMillis {
                for (key in keys) {
                    randomDummy += randomStdMap[key] ?: 0
                }
            }

            printPerformanceComparison("get random", "MyTreeMap", randomMyTime, "TreeMap", randomStdTime)
        }

        "순차 증가값" - {
            val sequentialMap = MyTreeMap<Int, Int>()
            val sequentialStdMap = TreeMap<Int, Int>()
            for (i in 0 until n) {
                sequentialMap.put(i, i)
                sequentialStdMap[i] = i
            }
            var sequentialDummy = 0
            val sequentialMyTime = measureTimeMillis {
                for (i in 0 until n) {
                    sequentialDummy += sequentialMap.get(i) ?: 0
                }
            }

            val sequentialStdTime = measureTimeMillis {
                for (i in 0 until n) {
                    sequentialDummy += sequentialStdMap[i] ?: 0
                }
            }

            printPerformanceComparison("get sequential", "MyTreeMap", sequentialMyTime, "TreeMap", sequentialStdTime)
        }

        "min/max 반복값" - {
            val minMaxMap = MyTreeMap<Int, Int>()
            val minMaxStdMap = TreeMap<Int, Int>()
            for (i in 0 until n) {
                val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                minMaxMap.put(key, key)
                minMaxStdMap[key] = key
            }

            var minMaxDummy = 0
            val minMaxMyTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    minMaxDummy += minMaxMap.get(key) ?: 0
                }
            }

            val minMaxStdTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    minMaxDummy += minMaxStdMap[key] ?: 0
                }
            }

            printPerformanceComparison("get min/max", "MyTreeMap", minMaxMyTime, "TreeMap", minMaxStdTime)
        }
    }
})
