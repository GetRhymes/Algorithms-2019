@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
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
// память O(1 + 2n) = O(n)
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


class People {

    private val peopleData = mutableMapOf<String, MutableList<String>>()


    fun addData(lastName: String, name: String) {
        peopleData[lastName] = mutableListOf(name)
    }

    fun data() = peopleData

    fun addPeople(lastName: String, name: String) {
        if (peopleData.containsKey(lastName)) {
            println(true)
            peopleData[lastName]!!.add(name)
        } else {
            println(false)
            peopleData[lastName] = mutableListOf(name)
        }
    }

    fun sortedDataOfPeople(): SortedMap<String, MutableList<String>> {
        peopleData.forEach { it.value.sortBy { it } }
        return peopleData.toSortedMap()
    }

}

class Address {

    private val streetData = mutableMapOf<String, MutableMap<Int, People>>()

    fun addData(people: People, street: String, house: Int, lastName: String, name: String) {
        if (streetData.containsKey(street)) {
            println(true)
            if (streetData[street]!!.containsKey(house)) {
                println(true)
                people.addPeople(lastName, name)
            } else {
                println(false)
                streetData[street]!![house] = people.also { it.addData(lastName, name) }
            }
        } else {
            println(false)
            streetData[street] = mutableMapOf(house to people)
        }
    }

    fun sortedBlock(): MutableMap<String, MutableMap<Int, People>> {
        val sortedData = mutableMapOf<String, MutableMap<Int, People>>()
        streetData.toSortedMap().forEach {
            it.value.toSortedMap()
            sortedData[it.key] = it.value
        }
        return sortedData
    }

}

fun sortAddresses(inputName: String, outputName: String) {

    val input = File(inputName).readLines()
    val address = Address()
    //val mapOfStreet = mutableMapOf<String, MutableMap<Int, MutableMap<String, MutableList<String>>>>()

    for (i in input) {
        val streetAndName = i.split(" - ")
        val streetAndNumber = streetAndName[1].split(" ")
        val nameAndLastName = streetAndName[0].split(" ")
        val name = nameAndLastName[1]
        val lastName = nameAndLastName[0]
        val street = streetAndNumber[0]
        val house = streetAndNumber[1].toInt()
        val peopleData = PeopleData().also {
            it.addData(lastName, name)
        }
        //val containerOfPeople = PeopleData().addData(lastName, name)

        val people = People().also {
            it.addData(lastName, name)
        }
        // if (mapOfStreet.containsKey(street)) {
        //     if (mapOfStreet[street]!!.getOrPut(house) { containerOfPeople } != containerOfPeople)
        //         peopleData.addName(lastName, name, mapOfStreet[street]!![house]!!)
        // } else mapOfStreet[street] = mutableMapOf(house to containerOfPeople)

        address.addData(people, street, house, lastName, name)
    }

    val output = File(outputName).bufferedWriter()
    for (data in address.sortedBlock()) {
        for (house in data.value) {
            var lineOfName = ""
            for (lastName in house.value.sortedDataOfPeople()) {
                for (name in 0 until lastName.value.size) {
                    println("${data.key}  ${house.key} ${lastName.key} ${lastName.value}")
                    lineOfName += "${lastName.key} ${lastName.value[name]}, "
                }
                output.write("${data.key} ${house.key} - $lineOfName".replace(Regex(""", $"""), ""))
                output.newLine()
            }
        }
    }
    output.close()

    //val out = File(outputName).bufferedWriter()
    //for (street in ) {
    //    for (house in street.value.toSortedMap()) {
    //        var lineOfName = ""
    //        val sortedHouseList = sortContainer(house.value).toList()
    //        for (i in 0 until sortedHouseList.size) {
    //            val lastName = sortedHouseList[i]
    //            for (name in 0 until lastName.second.size) {
    //                lineOfName += "${lastName.first} ${lastName.second[name]}, "
    //            }
    //        }
    //        out.write("${street.key} ${house.key} - $lineOfName".replace(Regex(""", $"""), ""))
    //        out.newLine()
    //    }
    //}
    //out.close()
}

// Средний случай: n + nlogn + nlogn + n = nlogn
// Плохой случай: n + nlogn + nlogn + n = nlogn
// память O(n^4)
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

// Временные затраты : {
// средний случай: n
// Плохой случай: n
//}
// Память: { O(1) }

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

private fun minPairValue(valueFirst: Map.Entry<Int, Int>, valueSecond: Map.Entry<Int, Int>): Map.Entry<Int, Int> {
    val min = min(valueFirst.key, valueSecond.key)
    return if (valueFirst.key == min) valueFirst else valueSecond
}

fun sortSequence(inputName: String, outputName: String) {
    val input = File(inputName).readLines()
    val listOfNumbers = mutableMapOf<Int, Int>()
    for (line in input) {
        val lineInt = line.toInt()
        if (listOfNumbers.contains(lineInt)) {
            listOfNumbers[lineInt] = listOfNumbers[lineInt]!! + 1
        } else listOfNumbers[lineInt] = 1
    }

    var maxCountOfNumber = listOfNumbers.maxBy { it.value }

    for (i in listOfNumbers) {
        if (i.value == maxCountOfNumber!!.value) {
            maxCountOfNumber = minPairValue(maxCountOfNumber, i)
        }
    }

    val out = File(outputName).bufferedWriter()

    for (i in 0 until input.size) {
        if (input[i].toInt() != maxCountOfNumber!!.key) {
            out.write(input[i])
            out.newLine()
        }
    }

    for (i in 0 until maxCountOfNumber!!.value) {
        out.write(maxCountOfNumber.key.toString())
        out.newLine()
    }
    out.close()
}/// ??????????????????????
// Средний случай: n + n + n + n = 4n = n
// Плохой случай: n + n + n + n = 4n = n
// память O(n)
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
