package lesson3


import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max


// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(var value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null

        fun minimum(): Node<T> {
            var result = this
            while (result.left != null)
                result = result.left!!
            return result
        }
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode


            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */

    private fun removeRecursion(root: Node<T>?, element: T): Pair<Node<T>?, Boolean> {
        var resultTree: Node<T>? = root
        if (resultTree == null) return Pair(resultTree, false)
        if (element < resultTree.value) resultTree.left = removeRecursion(resultTree.left!!, element).first
        else if (element > resultTree.value) resultTree.right = removeRecursion(resultTree.right!!, element).first
        else if (resultTree.left != null && resultTree.right != null) {
            resultTree.value = resultTree.right!!.minimum().value
            resultTree.right = removeRecursion(resultTree.right!!, resultTree.value).first
        } else if (resultTree.left != null) resultTree = resultTree.left else resultTree = resultTree.right
        return Pair(resultTree, true)
    }

    override fun remove(element: T): Boolean {
        size--
        val result = removeRecursion(this.root, element)
        root = result.first
        return result.second
    }

    // Рекурсивная реализация
// Время O(h), где h - высота дерева
    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        //private var stack = Stack<Node<T>>()

        private var current: Node<T>? = null

        private var temp: Node<T>? = root?.minimum()
        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        override fun hasNext(): Boolean {
            return temp != null
            //return (!stack.isEmpty() || current != null)
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        override fun next(): T {
            //while (current != null) {
            //    stack.push(current)
            //    current = current!!.left
            //}
            //current = stack.pop()
            //val node = current
            //current = current!!.right
            //return node!!.value                 реализация через стек

            current = temp
            temp = when {
                root == null -> null
                temp == null -> root?.minimum()
                else -> nextElement(temp!!.value)
            }

            return current!!.value
        }

        private fun nextElement(element: T): Node<T>? {
            var current = root
            var successor: Node<T>? = null
            while (current != null) {
                if (current.value > element) {
                    successor = current
                    current = current.left
                } else current = current.right
            }
            return successor
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        // время O(h)
        override fun remove() {
            current?.let { remove(it.value) }
            current = null
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */

    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
