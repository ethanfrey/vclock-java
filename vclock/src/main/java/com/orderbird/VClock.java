package com.orderbird;

import java.util.ArrayList;
import java.util.List;

public class VClock {
    protected List<Integer> vector;

    public VClock() {
        vector = new ArrayList<>();
    }

    protected void extend(int size) {
        for (int i=vector.size(); i<size; i++) {
            vector.add(new Integer(0));
        }
    }

    protected void increment_inplace(int idx) {
        extend(idx + 1);
        Integer val = vector.get(idx);
        vector.set(idx, ++val);
    }

    public VClock increment(int idx) {
        VClock result = clone();
        result.increment_inplace(idx);
        return result;
    }

    public VClock merge(VClock other, int idx) {
        VClock longer, shorter;
        if (vector.size() >= other.vector.size()) {
            longer = clone();
            shorter = other;
        } else {
            longer = other.clone();
            shorter = this;
        }
        for (int i=0; i<shorter.vector.size(); i++) {
            int a = longer.vector.get(i);
            int b = shorter.vector.get(i);
            longer.vector.set(i, Math.max(a, b));
        }
        longer.increment_inplace(idx);
        return longer;
    }

    public boolean after(VClock other) {
        if (other.vector.size() > vector.size()) {
            return false;
        }
        if (other.vector.equals(vector)) {
            return false;
        }
        // I know the other vector is equal or shorter in length than my vecotr
        int end = other.vector.size();
        for (int i=0; i<end; i++) {
            int here = vector.get(i);
            int there = other.vector.get(i);
            if (there > here) {
                return false;
            }
        }
        // all checks pass.  Yay!
        return true;
    }

    public boolean before(VClock other) {
        return other.after(this);
    }

    public boolean concurrent(VClock other) {
        return !after(other) && !other.after(this);
    }

    @Override
    protected VClock clone()  {
        VClock clone = new VClock();
        clone.vector = new ArrayList<>(this.vector);
        return clone;
    }
}
