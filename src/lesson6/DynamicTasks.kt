@file:Suppress("UNUSED_PARAMETER")

package lesson6


import java.io.*
import java.lang.Math.min


/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    val firstLength = first.length
    val secondLength = second.length
    if (firstLength == 0 || secondLength == 0) return ""
    val array = Array(first.length + 1) { IntArray(second.length + 1) }
    for (i in 0 until firstLength) {
        for (j in 0 until secondLength) {
            if (first[i] == second[j]) {
                array[i + 1][j + 1] = array[i][j] + 1
            } else array[i + 1][j + 1] = maxOf(array[i][j + 1], array[i + 1][j])
        }
    }
    val result = mutableListOf<Char>()
    var indexFirst = firstLength
    var indexSecond = secondLength
    while (indexFirst != 0 && indexSecond != 0) {
        when {
            array[indexFirst][indexSecond] == array[indexFirst][indexSecond - 1] -> indexSecond--
            array[indexFirst][indexSecond] == array[indexFirst - 1][indexSecond] -> indexFirst--
            else -> {
                result.add(0, first[indexFirst - 1])
                indexFirst--
                indexSecond--
            }
        }
    }
    return result.joinToString(separator = "")
}
// Time: O(mn), где m - длина первого слова, n - длина второго слова
/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    TODO()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    val input = File(inputName).readLines()
    val lines = input.size
    val columns = input[0].split(" ").size
    val table = Array(lines) { IntArray(columns) }
    for (i in 0 until table.size) {
        val lineInInput = input[i].split(" ")
        for (j in 0 until table[i].size) table[i][j] = lineInInput[j].toInt()
    }

    for (i in 0 until lines)
        for (j in 0 until columns)
            when {
                i > 0 && j > 0 -> table[i][j] += min(min(table[i - 1][j], table[i][j - 1]), table[i - 1][j - 1])
                i > 0 -> table[i][j] += table[i - 1][j]
                j > 0 -> table[i][j] += table[i][j - 1]
            }

    return table[lines - 1][columns - 1]
}

// Time: O(n*m), где m и n являются длинами прямоугольного поля (кол-во значений по длине и ширине)


// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5