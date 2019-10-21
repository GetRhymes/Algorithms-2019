@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File
import java.lang.Math.min


/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    val input = File(inputName).readLines()
    var minIndex = 0
    var maxIndex = input.size - 1
    var maxDifference = 0
    for (i in 1 until input.size - 1) {
        for (el in i + 1 until input.size) {
            val difference = input[i].toInt() - input[el].toInt()
            if (difference < maxDifference) {
                maxDifference = difference
                maxIndex = el
                minIndex = i
            }
        }
    }
    return Pair(minIndex + 1, maxIndex + 1)
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */

fun rek(menNumber: Int, choiceInterval: Int): Int {
    return if (menNumber == 1) 1
    else {
        var currentMenNumber = 2
        var result = 1
        while (currentMenNumber <= menNumber) {
            result = (result + choiceInterval - 1) % currentMenNumber + 1
            currentMenNumber++
        }
        result
    }
}

fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    return rek(menNumber, choiceInterval)
}
// Плохой случай n
// Средний случай n
// Решение из Википедии

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */

private fun index(s1: String, s2: String): Int {
    var result = 0
    for (index in 0 until s1.length) {
        val mutList = s1.toMutableList()
        mutList.removeAt(index)
        val k = mutList.joinToString(separator = "")
        if (!k.contains(s2)) {
            result = index
            break
        }
    }
    return result
}

fun longestCommonSubstring(first: String, second: String): String {
    val minLength = min(first.length, second.length)
    val minString = if (minLength == first.length) first else second
    val maxString = if (minString == first) second else first
    if (minString == maxString || maxString.endsWith(minString)) return minString
    var result = ""
    var indexResult = 0
    for (i in 0 until minString.length) {
        if (maxString.contains(minString.substring(i, i))) {
            if (minString.lastIndex == i) return result
            var count = i + 1
            var possibleResult = ""
            while (maxString.contains(minString.substring(i, count)) && count < minString.length) {
                possibleResult = minString.substring(i, count)
                count++
            } // этим циклом я наполняю подстроку, которая возможно будет являться результатом

            if (possibleResult.length > result.length) {
                result = possibleResult
                indexResult = index(first, possibleResult)
                // метод индекс позволяет выполнить условие задачи (* Если имеется несколько самых длинных общих подстрок одной длины,
                //* вернуть ту из них, которая встречается раньше в строке first.)
            }

            if (possibleResult.length == result.length && index(first, possibleResult) < indexResult) {
                result = possibleResult
            }
        }
    }
    return result
}
// Плохой n^2
// средний n^2
/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
fun calcPrimesNumber(limit: Int): Int {
    if (limit <= 1) return 0

    val checkList = mutableListOf<Int>()
    for (i in 0..limit) {
        checkList.add(1)
    }
    var count = checkList.size
    for (i in 2 until limit) {
        if (checkList[i] == 1) {
            var step = 2 * i
            while (step <= checkList.size - 1) {
                if (checkList[step] == 1) {
                    checkList[step] = 0
                    count--
                }
                step += i
            }
        } else continue
    }
    return count - 2
}
//Худший случай nloglogn
//Среднийй случай nloglogn
// алгоритм: Решето Эратосфена
/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    TODO()
}