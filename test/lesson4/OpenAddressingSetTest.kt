package lesson4

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

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
        val set = OpenAddressingSet<String>(16)
        assertTrue(set.isEmpty())
        set.add("Alpha")
        set.add("Beta")
        set.add("Omega")
        set.add("Delta")
        set.add("Gamma")
        assertEquals(setOf("Alpha", "Beta", "Omega", "Delta", "Gamma"), set)
        val iterator = set.iterator()

        for (i in set.size - 1 downTo 0) {
            iterator.next()
            iterator.remove()
            assertEquals(i, set.size)
        }
        assertFalse(iterator.hasNext())
        assertFalse(set.contains("Alpha"))
        assertFalse(set.contains("Beta"))
        assertFalse(set.contains("Omega"))
        assertFalse(set.contains("Delta"))
        assertFalse(set.contains("Gamma"))
        set.add("Alpha")
        assertEquals(1, set.size)
    }

    @Test
    @Tag("Example")
    fun maxCapacity() {

        var set = OpenAddressingSet<String>(3)

        for (i in 1..8) {
            set.add("$i")
        }
        assertEquals(8, set.size)
        assertThrows<IllegalStateException> { set.add("more") }

        set = OpenAddressingSet<String>(4)

        for (i in 1..16) {
            set.add("$i")
        }
        assertEquals(16, set.size)
        assertThrows<IllegalStateException> { set.add("more") }
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