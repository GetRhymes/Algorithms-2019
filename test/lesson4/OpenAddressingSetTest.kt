package lesson4

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag

class OpenAddressingSetTest {

    @Test
    @Tag("Example")
    fun add() {
        val set = OpenAddressingSet<String>(16)
        assertTrue(set.isEmpty())
        set.add("Alpha")
        set.add("Beta")
        set.add("Omega")
        assertSame(3, set.size)
        assertTrue("Beta" in set)
        assertFalse("Gamma" in set)
        assertTrue("Omega" in set)
    }

    @Test
    @Tag("Example")
    fun checkDoubleElements() {
        val set = OpenAddressingSet<String>(16)
        assertTrue(set.isEmpty())
        set.add("Beta")
        set.add("Alpha")
        set.add("Beta")
        set.add("Alpha")
        assertEquals(2, set.size)
    }

    @Test
    @Tag("Example")
    fun remove() {
        val kit = setOf("Alpha", "Beta", "Omega")
        val set = OpenAddressingSet<String>(16)
        assertTrue(set.isEmpty())
        for (i in kit) {
            set.add(i)
            assertTrue(i in set)
        }

        var result = set.remove("Beta")
        assertSame(true, result)
        assertSame(2, set.size)
        assertTrue("Alpha" in set)
        assertFalse("Beta" in set)
        assertTrue("Omega" in set)

        result = set.remove("Beta")
        assertSame(false, result)
        assertSame(2, set.size)
        assertTrue("Alpha" in set)
        assertFalse("Beta" in set)
        assertTrue("Omega" in set)
    }

    @Test
    @Tag("Example")
    fun iterator() {
        val kit = setOf("Alpha", "Beta", "Omega", "Gamma")
        val set = OpenAddressingSet<String>(16)
        assertTrue(set.isEmpty())
        for (i in kit) {
            set.add(i)
            assertTrue(i in set)
        }

        val iterator = set.iterator()
        var count = kit.size

        while (iterator.hasNext()) {
            assertTrue(iterator.next() in kit)
            count -= 1
        }
        assertSame(0, count)
    }

    @Test
    @Tag("Example")
    fun iteratorRemove() {
        val kit = setOf("Alpha", "Beta", "Omega", "Gamma")
        val set = OpenAddressingSet<String>(16)
        assertTrue(set.isEmpty())
        for (it in kit) {
            set.add(it)
            assertTrue(it in set)
        }

        var toRemove = "Beta"

        var iterator = set.iterator()
        var count = kit.size

        while (iterator.hasNext()) {
            val next = iterator.next()
            println(next)

            assertTrue(next in kit)
            count -= 1

            if (next == toRemove) {
                iterator.remove()
            }
        }

        assertSame(0, count)
        assertSame(3, set.size)
        assertTrue("Alpha" in set)
        assertTrue("Omega" in set)
        assertTrue("Gamma" in set)
        assertFalse("Beta" in set)

        toRemove = "Omega"

        iterator = set.iterator()
        count = set.size
        println(count)

        while (iterator.hasNext()) {
            val next = iterator.next()
            println(next)
            assertTrue(next in kit)
            count -= 1

            if (next == toRemove) {
                iterator.remove()
            }
        }

        assertSame(0, count)
        assertSame(2, set.size)
        assertTrue("Alpha" in set)
        assertFalse("Omega" in set)
        assertTrue("Gamma" in set)
        assertFalse("Beta" in set)
    }
}