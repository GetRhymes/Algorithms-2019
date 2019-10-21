@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.reflect.TypeVariable
import java.util.*
import kotlin.math.abs
import kotlin.math.min


/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

fun sortTimes(inputName: String, outputName: String) {
    val inpName = File(inputName).readLines()
    val timeInSecond = mutableListOf<Pair<Int, String>>()
    for (time in inpName) {
        val partOfLine = time.split(" ")
        val partOfTime = partOfLine[0].split(":")
        if (partOfLine[1] == "PM")
            if (partOfTime[0].toInt() != 12)
                timeInSecond.add(
                    Pair(
                        ((partOfTime[0].toInt() + 12) * 3600 + partOfTime[1].toInt() * 60 + partOfTime[2].toInt()), time
                    )
                )
            else timeInSecond.add(
                Pair((partOfTime[0].toInt() * 3600 + partOfTime[1].toInt() * 60 + partOfTime[2].toInt()), time)
            )
        else
            if (partOfTime[0].toInt() != 12)
                timeInSecond.add(
                    Pair((partOfTime[0].toInt() * 3600 + partOfTime[1].toInt() * 60 + partOfTime[2].toInt()), time)
                )
            else timeInSecond.add(Pair(partOfTime[1].toInt() * 60 + partOfTime[2].toInt(), time))
    }
    val sortedTime = timeInSecond.sortedBy { it.first }

    val out = File(outputName).bufferedWriter()

    for (time in sortedTime) {
        out.write(time.second)
        out.newLine()
    }
    out.close()
}
// Средний случай: n + nlogn + n = 2n + nlogn = n + nlogn = nlogn
// Плохой случай: n + n^2 + n = 2n + n^2 = n + n^2 = n^2
/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

class PeopleData {

    fun addData(lastName: String, name: String): MutableMap<String, MutableList<String>> {
        return mutableMapOf(lastName to mutableListOf(name))
    }

    fun addName(lastName: String, name: String, dataStore: MutableMap<String, MutableList<String>>) {
        if (dataStore.containsKey(lastName)) dataStore[lastName]!!.add(name)
        else dataStore[lastName] = mutableListOf(name)
    }
}

// Класс введен для лучшей читаемости сложной структры данных
fun sortContainer(dataStore: MutableMap<String, MutableList<String>>): SortedMap<String, MutableList<String>> {
    for (listOfNames in dataStore) {
        listOfNames.value.sortBy { it }
    }
    return dataStore.toSortedMap()
}


fun sortAddresses(inputName: String, outputName: String) {

    val input = File(inputName).readLines()

    val mapOfStreet = mutableMapOf<String, MutableMap<Int, MutableMap<String, MutableList<String>>>>()
    for (i in input) {
        val streetAndName = i.split(" - ")
        val streetAndNumber = streetAndName[1].split(" ")
        val nameAndLastName = streetAndName[0].split(" ")
        val name = nameAndLastName[1]
        val lastName = nameAndLastName[0]
        val street = streetAndNumber[0]
        val house = streetAndNumber[1].toInt()
        val peopleData = PeopleData()
        val containerOfPeople = PeopleData().addData(lastName, name)

        if (mapOfStreet.containsKey(street)) {
            if (mapOfStreet[street]!!.getOrPut(house) { containerOfPeople } != containerOfPeople)
                peopleData.addName(lastName, name, mapOfStreet[street]!![house]!!)
        } else mapOfStreet[street] = mutableMapOf(house to containerOfPeople)
    }

    val out = File(outputName).bufferedWriter()
    for (street in mapOfStreet.toSortedMap()) {
        for (house in street.value.toSortedMap()) {
            var lineOfName = ""
            val sortedHouseList = sortContainer(house.value).toList()
            for (i in 0 until sortedHouseList.size) {
                val lastName = sortedHouseList[i]
                for (name in 0 until lastName.second.size) {
                    lineOfName += "${lastName.first} ${lastName.second[name]}, "
                }
            }
            out.write("${street.key} ${house.key} - $lineOfName".replace(Regex(""", $"""), ""))
            out.newLine()
        }
    }
    out.close()
}

// Средний случай: n + nlogn + nlogn + n = nlogn
// Плохой случай: n + nlogn + nlogn + n = nlogn
// Не уверен, что правильно посчитал
/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */


fun sortTemperatures(inputName: String, outputName: String) {
    val input = File(inputName).readLines()
    val arrayOfTemp = Array(7731) { i -> i * 0 }
    for (temp in input) {
        val tempInInt = (temp.toDouble() * 10 + 2730).toInt()
        arrayOfTemp[tempInInt] += 1
    }

    val out = File(outputName).bufferedWriter()
    for (temp in 0 until arrayOfTemp.size) {
        if (arrayOfTemp[temp] != 0) {
            val realTemp = if (temp - 2730 >= 0) {
                "${abs(temp - 2730) / 10}.${abs(temp - 2730) % 10}"
            } else "-${abs(temp - 2730) / 10}.${abs(temp - 2730) % 10}"

            for (i in 0 until arrayOfTemp[temp]) {
                out.write(realTemp)
                out.newLine()
            }
        }
    }
    out.close()
}

// Средний случай: n
// Плохой случай: n

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */

private fun minPairValue(valueFirst: Pair<Int, Int>, valueSecond: Pair<Int, Int>): Pair<Int, Int> {
    val min = min(valueFirst.first, valueSecond.first)
    return if (valueFirst.first == min) valueFirst else valueSecond
}

fun sortSequence(inputName: String, outputName: String) {
    val input = File(inputName).readLines()
    val listOfNumbers = mutableMapOf<Int, Int>()
    for (line in input) {
        if (listOfNumbers.contains(line.toInt())) {
            listOfNumbers[line.toInt()] = listOfNumbers[line.toInt()]!! + 1
        } else listOfNumbers[line.toInt()] = 1
    }

    var maxCountOfNumber = listOfNumbers.toList().maxBy { it.second }

    for (i in listOfNumbers.toList()) {
        if (i.second == maxCountOfNumber!!.second) {
            maxCountOfNumber = minPairValue(maxCountOfNumber, i)
        }
    }

    val out = File(outputName).bufferedWriter()

    for (i in 0 until input.size) {
        if (input[i].toInt() != maxCountOfNumber!!.first) {
            out.write(input[i])
            out.newLine()
        }
    }

    for (i in 0 until maxCountOfNumber!!.second) {
        out.write(maxCountOfNumber.first.toString())
        out.newLine()
    }
    out.close()
}
// Средний случай: n + n + n + n = 4n = n
// Плохой случай: n + n + n + n = 4n = n
/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}
