package com.orderbird;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class VClockModelTest {
    @Test
    public void basic_increment() throws Exception {
        VClockArray a = new VClockArray();
        VClockArray b = a.increment(0);
        VClockArray c = b.increment(3);
        assertEquals(a.vector.size(), 0);
        assertEquals(b.vector.size(), 1);
        assertEquals((int)b.vector.get(0), 1);
        assertEquals(c.vector.size(), 4);
        assertEquals((int)c.vector.get(3), 1);
        assertEquals((int)c.vector.get(2), 0);
    }

    @Test
    public void basic_compare() throws Exception {
        VClockArray a = new VClockArray();
        VClockArray b = a.increment(0);
        VClockArray c = b.increment(3);
        VClockArray d = b.increment(2);
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
        VClockArray a = new VClockArray().increment(2);
        VClockArray b = a.increment(1);
        VClockArray c = a.increment(2);
        VClockArray merged = b.merge(c, 0);
        VClockArray merged2 = b.merge(c, 1);
        assertTrue(b.concurrent(c));
        // check some values
        assertEquals(merged.vector.size(), 3);
        assertEquals(merged2.vector.size(), 3);
        assertEquals((int)merged.vector.get(2), 2);
        assertEquals((int)merged.vector.get(0), 1);
        assertEquals((int)merged.vector.get(1), 1);
        assertEquals((int)merged2.vector.get(0), 0);
        assertEquals((int)merged2.vector.get(1), 2);
        // make sure the compares work as well
        assertTrue(merged.after(b));
        assertTrue(merged.after(c));
        assertTrue(merged2.after(b));
        assertTrue(merged2.after(c));
        assertTrue(merged2.concurrent(merged));
    }

}