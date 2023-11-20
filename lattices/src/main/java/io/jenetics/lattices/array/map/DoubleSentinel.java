package io.jenetics.lattices.array.map;

class DoubleSentinel {
    boolean hasEmptyKey;
    boolean hasRemovedKey;
    double emptyKeyValue;
    double removedKeyValue;

    int size() {
        return (hasEmptyKey ? 1 : 0) + (hasRemovedKey ? 1 : 0);
    }

    boolean contains(double value) {
        boolean valueEqualsZeroValue = hasEmptyKey && Double.compare(emptyKeyValue, value) == 0;
        boolean valueEqualsOneValue = hasRemovedKey && Double.compare(removedKeyValue, value) == 0;
        return valueEqualsZeroValue || valueEqualsOneValue;
    }

    DoubleSentinel copy() {
        DoubleSentinel sentinel = new DoubleSentinel();
        sentinel.emptyKeyValue = this.emptyKeyValue;
        sentinel.removedKeyValue = this.removedKeyValue;
        sentinel.hasRemovedKey = this.hasRemovedKey;
        sentinel.hasEmptyKey = this.hasEmptyKey;
        return sentinel;
    }

    void clear() {
        hasEmptyKey = false;
        hasRemovedKey = false;
    }

}
