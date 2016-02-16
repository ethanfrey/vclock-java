package com.orderbird;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Set;

/**
 * Created by ethan on 16/02/16.
 */
public class VClockDict {
    protected Map<String, Integer> vector;

    public VClockDict() {
        vector = new HashMap<String, Integer>();
    }

    protected void increment_inplace(String idx) {
        if (vector.containsKey(idx)) {
            Integer val = vector.get(idx);
            vector.put(idx, ++val);
        } else {
            vector.put(idx, 1);
        }
    }

    public VClockDict increment(String idx) {
        VClockDict result = clone();
        result.increment_inplace(idx);
        return result;
    }

    public VClockDict merge(VClockDict other, String idx) {
        // update every element to the larger of the two hash maps
        VClockDict combined = clone();
        for (String key: other.vector.keySet()) {
            Integer value = other.vector.get(key);
            if (value.compareTo(combined.vector.get(key)) > 0) {
                combined.vector.put(key, value);
            }
        }
        combined.increment_inplace(idx);
        return combined;
    }

    public boolean after(VClockDict other) {
        if (other.vector.size() > vector.size()) {
            // if it is bigger, I cannot be after it
            return false;
        }
        // when comparing vectors, keep track if they are identical
        boolean equal = (vector.size() == other.vector.size());
        // knowing that other has a subset of my keys, just compare those
        for (String key: other.vector.keySet()) {
            // if it has a key I don't have, I cannot be after it
            if (!vector.containsKey(key)) {
                return false;
            }
            // otherwise, check a compare
            int mine = vector.get(key);
            int theirs = other.vector.get(key);
            if (theirs > mine) {
                return false;
            } else if (mine > theirs) {
                // let's see if there are any places where I am higher than other
                equal = false;
            }
        }

        // all checks pass, we are good to go!
        return !equal;
    }

    public boolean before(VClockDict other) {
        return other.after(this);
    }

    public boolean concurrent(VClockDict other) {
        return !after(other) && !other.after(this);
    }

    @Override
    protected VClockDict clone()  {
        VClockDict clone = new VClockDict();
        clone.vector = new HashMap<String, Integer>(vector);
        return clone;
    }
}
