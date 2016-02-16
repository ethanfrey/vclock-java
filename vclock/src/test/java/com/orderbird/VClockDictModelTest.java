package com.orderbird;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class VClockDictModelTest {
    @Test
    public void basic_increment() throws Exception {
        VClockDict a = new VClockDict();
        VClockDict b = a.increment("AA");
        VClockDict c = b.increment("DD");
        assertEquals(a.vector.size(), 0);
        assertEquals(b.vector.size(), 1);
        assertEquals((int)b.vector.get("AA"), 1);
        assertEquals(c.vector.size(), 2);
        assertEquals((int)c.vector.get("DD"), 1);
    }

    @Test
    public void basic_compare() throws Exception {
        VClockDict a = new VClockDict();
        VClockDict b = a.increment("FO");
        VClockDict c = b.increment("OB");
        VClockDict d = b.increment("AR");
        // everyone is after a
        assertTrue(b.after(a));
        assertTrue(c.after(a));
        assertTrue(d.after(a));
        // two are after b
        assertFalse(a.after(b));
        assertTrue(c.after(b));
        assertTrue(d.after(b));
        // and before also works
        assertTrue(a.before(b));
        assertFalse(c.before(b));
        assertFalse(d.before(b));
        // c and d are not ater one anoter
        assertFalse(d.after(c));
        assertFalse(c.after(d));
        // concurrency is transative
        assertTrue(c.concurrent(d));
        assertTrue(d.concurrent(c));
    }


    @Test
    public void compare_merge() throws Exception {
        VClockDict a = new VClockDict().increment("7s");
        VClockDict b = a.increment("a9");
        VClockDict c = a.increment("7s");
        // this has 7s = 2, a9 = 1, 42 = 1
        VClockDict merged = b.merge(c, "42");
        // this has 7s = 2, a9 = 2
        VClockDict merged2 = b.merge(c, "a9");
        assertTrue(b.concurrent(c));
        // check some values
        assertEquals(c.vector.size(), 1);
        assertEquals(merged.vector.size(), 3);
        assertEquals(merged2.vector.size(), 2);
        assertEquals((int)merged.vector.get("7s"), 2);
        assertEquals((int)merged.vector.get("a9"), 1);
        assertEquals((int)merged.vector.get("42"), 1);
        assertEquals((int)merged2.vector.get("7s"), 2);
        assertEquals((int)merged2.vector.get("a9"), 2);
        // make sure the compares work as well
        assertTrue(merged.after(b));
        assertTrue(merged.after(c));
        assertTrue(merged2.after(b));
        assertTrue(merged2.after(c));
        assertTrue(merged2.concurrent(merged));
    }

}