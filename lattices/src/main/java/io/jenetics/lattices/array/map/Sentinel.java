package io.jenetics.lattices.array.map;

class Sentinel {
    boolean hasZeroKey;
    boolean hasOneKey;
    double zeroValue;
    double oneValue;

    public int size() {
        return (hasZeroKey ? 1 : 0) + (hasOneKey ? 1 : 0);
    }

    boolean contains(double value) {
        boolean valueEqualsZeroValue = hasZeroKey && Double.compare(zeroValue, value) == 0;
        boolean valueEqualsOneValue = hasOneKey && Double.compare(oneValue, value) == 0;
        return valueEqualsZeroValue || valueEqualsOneValue;
    }

    Sentinel copy() {
        Sentinel sentinel = new Sentinel();
        sentinel.zeroValue = this.zeroValue;
        sentinel.oneValue = this.oneValue;
        sentinel.hasOneKey = this.hasOneKey;
        sentinel.hasZeroKey = this.hasZeroKey;
        return sentinel;
    }

    void clear() {
        hasZeroKey = false;
        hasOneKey = false;
    }

}
