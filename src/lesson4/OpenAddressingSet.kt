package lesson4

class OpenAddressingSet<T : Any>(private val bits: Int) : AbstractMutableSet<T>() {
    init {
        require(bits in 2..31)
    }

    private val capacity = 1 shl bits

    private val storage = Array<Any?>(capacity) { null }

    private val storageUsed = Array(capacity) { false }

    override var size: Int = 0

    private fun T.startingIndex(): Int {
        return hashCode() and (0x7FFFFFFF shr (31 - bits))
    }

    override fun contains(element: T): Boolean {
        var index = element.startingIndex()
        var current = storage[index]
        var checked = 0

        while (storageUsed[index] && checked < capacity) {
            if (current == element) return true
            index = (index + 1) % capacity
            current = storage[index]
            checked++
        }
        return false
    }

    override fun add(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                return false
            }
            index = (index + 1) % capacity
            check(index != startingIndex) { "Table is full" }
            current = storage[index]
        }
        storage[index] = element
        storageUsed[index] = true
        size++
        return true
    }

    /**
     * Для этой задачи пока нет тестов, но вы можете попробовать привести решение и добавить к нему тесты
     *
     *
     * Time : O(n), где n == capacity
     * Memory : O(1)
     */
    override fun remove(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]

        while (storageUsed[index]) {
            if (current == element) {
                storage[index] = null
                size--
                return true
            }

            index = (index + 1) % capacity
            if (index == startingIndex) {
                return false
            }
            current = storage[index]
        }
        return false
    }

    /**
     * Для этой задачи пока нет тестов, но вы можете попробовать привести решение и добавить к нему тесты
     */
    override fun iterator() = object : MutableIterator<T> {

        private var currentIndex = 0
        private var nextIndex = 0
        private var current: T? = null

        init {
            while (nextIndex < capacity && storage[nextIndex] == null) nextIndex++
        }

        // Time : O(1)
        // Memory : O(1)
        override fun hasNext(): Boolean {
            return nextIndex < capacity
        }

        // Time : O(n), где  n == capacity
        // Memory : O(1)
        override fun next(): T {
            currentIndex = nextIndex
            current = storage[nextIndex]!! as T
            nextIndex++
            while (nextIndex < capacity && storage[nextIndex] == null) nextIndex++
            return current!!
        }


        // Time : O(1)
        // Memory : O(1)
        override fun remove() {
            if (current != null) {
                storage[currentIndex] = null
                size--
            }
        }
    }
}