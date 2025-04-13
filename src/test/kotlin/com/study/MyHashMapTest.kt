package com.study

import io.kotest.core.spec.style.FreeSpec
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

class MyHashMapTest : FreeSpec({

    "기본 동작 테스트" - {
        "put 테스트" - {
            val map = MyHashMap<String, Int>()
            map.put("a", 1)
            map.put("b", 2)
            map.put("c", 3)

            assert(map.get("a") == 1)
            assert(map.get("b") == 2)
            assert(map.get("c") == 3)
            assert(map.get("d") == null)

            map.put("b", 20)
            assert(map.get("b") == 20)
        }

        "remove 테스트" - {
            val map = MyHashMap<String, Int>()
            map.put("a", 1)
            map.remove("a")
            assert(map.get("a") == null)
        }
    }

    "성능 테스트 - 삽입 (5_000_000개)" - {
        val myMap = MyHashMap<Int, Int>()
        val stdMap = HashMap<Int, Int>()
        val n = 5_000_000

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

        printPerformanceComparison("put", "MyHashMap", myTime, "HashMap", stdTime)

        "랜덤값" - {
            val randomMap = MyHashMap<Int, Int>()
            val randomStdMap = HashMap<Int, Int>()

            val randomMyTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = Random.nextInt()
                    randomMap.put(key, key)
                }
            }

            val randomStdTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = Random.nextInt()
                    randomStdMap[key] = key
                }
            }

            printPerformanceComparison("put random", "MyHashMap", randomMyTime, "HashMap", randomStdTime)
        }

        "순차 증가값" - {
            val sequentialMap = MyHashMap<Int, Int>()
            val sequentialStdMap = HashMap<Int, Int>()

            val sequentialMyTime = measureTimeMillis {
                for (i in 0 until n) {
                    sequentialMap.put(i, i)
                }
            }

            val sequentialStdTime = measureTimeMillis {
                for (i in 0 until n) {
                    sequentialStdMap[i] = i
                }
            }

            printPerformanceComparison("put sequential", "MyHashMap", sequentialMyTime, "HashMap", sequentialStdTime)
        }

        "min/max 반복값" - {
            val minMaxMap = MyHashMap<Int, Int>()
            val minMaxStdMap = HashMap<Int, Int>()

            val minMaxMyTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    minMaxMap.put(key, key)
                }
            }

            val minMaxStdTime = measureTimeMillis {
                for (i in 0 until n) {
                    val key = if (i % 2 == 0) Int.MIN_VALUE else Int.MAX_VALUE
                    minMaxStdMap[key] = key
                }
            }

            printPerformanceComparison("put min/max", "MyHashMap", minMaxMyTime, "HashMap", minMaxStdTime)
        }
    }

    "성능 테스트 - 조회(5_000_000개)" - {
        val myMap = MyHashMap<Int, Int>()
        val stdMap = HashMap<Int, Int>()
        val n = 5_000_000
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

        printPerformanceComparison("get", "MyHashMap", myTime, "HashMap", stdTime)

        "랜덤값" - {
            val randomMap = MyHashMap<Int, Int>()
            val randomStdMap = HashMap<Int, Int>()
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

            printPerformanceComparison("get random", "MyHashMap", randomMyTime, "HashMap", randomStdTime)
        }

        "순차 증가값" - {
            val sequentialMap = MyHashMap<Int, Int>()
            val sequentialStdMap = HashMap<Int, Int>()
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

            printPerformanceComparison("get sequential", "MyHashMap", sequentialMyTime, "HashMap", sequentialStdTime)
        }

        "min/max 반복값" - {
            val minMaxMap = MyHashMap<Int, Int>()
            val minMaxStdMap = HashMap<Int, Int>()
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

            printPerformanceComparison("get min/max", "MyHashMap", minMaxMyTime, "HashMap", minMaxStdTime)
        }
    }
})
