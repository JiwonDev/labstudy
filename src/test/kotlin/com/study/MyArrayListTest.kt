import com.study.MyArrayList
import io.kotest.assertions.assertSoftly
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

class MyArrayListTest : FreeSpec({

    "기본 동작 테스트" - {

        "1. 요소를 추가하고 get으로 가져오면 값이 같아야 한다" {
            val list = MyArrayList<Int>()
            list.add(10)

            assertSoftly {
                list.get(0) shouldBe 10
            }
        }

        "2. 여러 요소를 추가하고 size가 올바른지 확인한다" {
            val list = MyArrayList<Int>()
            list.add(1)
            list.add(2)
            list.add(3)

            assertSoftly {
                list.size() shouldBe 3
            }
        }

        "3. 중간에 값을 삽입하면 인덱스가 밀려야 한다" {
            val list = MyArrayList<Int>()
            list.add(element = 1)
            list.add(element = 3)
            list.add(element = 2, index = 1) // 중간 삽입

            assertSoftly {
                list.get(index = 0) shouldBe 1
                list.get(index = 1) shouldBe 2
                list.get(index = 2) shouldBe 3
            }
        }

        "4. 중간에 값을 제거하면 사이즈가 줄고, 인덱스가 당겨진다" {
            val list = MyArrayList<Int>()
            list.add(1)
            list.add(2)
            list.add(3)
            val removed: Int? = list.removeAt(1) // 중간 삭제

            assertSoftly {
                removed shouldBe 2
                list.get(0) shouldBe 1
                list.get(1) shouldBe 3

                list.size() shouldBe 2
            }
        }
    }

    "성능 테스트 (Kotlin ArrayList 와 비교)" - {
        val dataSize = 50_000_000
        val randomData = List(dataSize) { Random.nextInt() }
        val sequentialData = List(dataSize) { it }
        val alternatingData = List(dataSize) {
            if (it % 2 == 0) it / 2
            else Int.MAX_VALUE - (it / 2)
        }

        "삽입 성능 비교(50_000_000 개)" - {
            "1. 랜덤값 삽입 성능" {
                val myList = MyArrayList<Int>()
                val myListTime: Long = measureTimeMillis {
                    repeat(dataSize) {
                        myList.add(element = randomData[it])
                    }
                }
                val kotlinList = ArrayList<Int>()
                val kotlinListTime: Long = measureTimeMillis {
                    repeat(dataSize) {
                        kotlinList.add(element = randomData[it])
                    }
                }

                printPerformanceComparison(
                    name = "랜덤값 삽입",
                    firstLabel = "MyArrayList",
                    firstTime = myListTime,
                    secondLabel = "ArrayList",
                    secondTime = kotlinListTime,
                )
            }

            "2. 순차 증가값 삽입 성능" {
                val myList = MyArrayList<Int>()
                val myListTime = measureTimeMillis {
                    repeat(dataSize) {
                        myList.add(element = sequentialData[it])
                    }
                }

                val kotlinList = ArrayList<Int>()
                val kotlinListTime = measureTimeMillis {
                    repeat(dataSize) {
                        kotlinList.add(element = sequentialData[it])
                    }
                }

                printPerformanceComparison(
                    name = "순차 증가값 삽입",
                    firstLabel = "MyArrayList",
                    firstTime = myListTime,
                    secondLabel = "ArrayList",
                    secondTime = kotlinListTime,
                )
            }

            "3. min/max 반복값 삽입 성능" {
                val myList = MyArrayList<Int>()
                val myListTime = measureTimeMillis {
                    repeat(dataSize) {
                        myList.add(element = alternatingData[it])
                    }
                }

                val kotlinList = ArrayList<Int>()
                val kotlinListTime = measureTimeMillis {
                    repeat(dataSize) {
                        kotlinList.add(element = alternatingData[it])
                    }
                }

                printPerformanceComparison(
                    name = "min/max 반복값 삽입",
                    firstLabel = "MyArrayList",
                    firstTime = myListTime,
                    secondLabel = "ArrayList",
                    secondTime = kotlinListTime,
                )
            }
        }

        "중간 삽입/삭제 성능 비교(50_000 개)" - {
            val midInsertSize = 50_000
            val midDeleteSize = 50_000

            "1. 중간 삽입 성능" {
                val baseList = List(midInsertSize) { it }

                val myList = MyArrayList<Int>().apply {
                    baseList.forEach { add(it) }
                }
                val myInsertTime = measureTimeMillis {
                    repeat(midInsertSize / 2) {
                        myList.add(element = -1, index = myList.size() / 2)
                    }
                }

                val kotlinList = ArrayList<Int>().apply {
                    baseList.forEach { add(it) }
                }
                val kotlinInsertTime = measureTimeMillis {
                    repeat(midInsertSize / 2) {
                        kotlinList.add(kotlinList.size / 2, -1)
                    }
                }

                printPerformanceComparison(
                    name = "중간 삽입",
                    firstLabel = "MyArrayList",
                    firstTime = myInsertTime,
                    secondLabel = "ArrayList",
                    secondTime = kotlinInsertTime,
                )
            }

            "2. 중간 삭제 성능" {
                val baseList = List(midDeleteSize) { it }

                val myList = MyArrayList<Int>().apply {
                    baseList.forEach { add(it) }
                }
                val myDeleteTime = measureTimeMillis {
                    repeat(midDeleteSize / 2) {
                        myList.removeAt(myList.size() / 2)
                    }
                }

                val kotlinList = ArrayList<Int>().apply {
                    baseList.forEach { add(it) }
                }
                val kotlinDeleteTime = measureTimeMillis {
                    repeat(midDeleteSize / 2) {
                        kotlinList.removeAt(kotlinList.size / 2)
                    }
                }

                printPerformanceComparison(
                    name = "중간 삭제",
                    firstLabel = "MyArrayList",
                    firstTime = myDeleteTime,
                    secondLabel = "ArrayList",
                    secondTime = kotlinDeleteTime,
                )
            }
        }

    }
})