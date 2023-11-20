package io.jenetics.lattices.array.map;

import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import io.jenetics.lattices.function.IntDoubleConsumer;

public class IntDoubleHashMap extends IntPrimitiveMap {

    static final class DoubleSentinel extends Sentinel {
        double emptyKeyValue;
        double removedKeyValue;

        boolean contains(double value) {
            return
                (hasEmptyKey && Double.compare(emptyKeyValue, value) == 0) ||
                (hasRemovedKey && Double.compare(removedKeyValue, value) == 0);
        }
    }

    private static final double EMPTY_VALUE = 0.0;

    private final DoubleSentinel sentinel = new DoubleSentinel();

    private double[] values;

    public IntDoubleHashMap() {
        allocate(DEFAULT_INITIAL_CAPACITY << 1);
    }

    public IntDoubleHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be less than 0.");
        }

        final int capacity = smallestPowerOfTwoGreaterThan(initialCapacity << 1);
        allocate(capacity);
    }

    @Override
    DoubleSentinel sentinel() {
        return sentinel;
    }

    public void put(int key, double value) {
        if (key == EMPTY_KEY) {
            sentinel.hasEmptyKey = true;
            sentinel.emptyKeyValue = value;
        } else if (key == REMOVED_KEY) {
            sentinel.hasRemovedKey = true;
            sentinel.removedKeyValue = value;
        } else {
            final int index = probe(key);
            final int keyAtIndex = keys[index];

            if (keyAtIndex == key) {
                values[index] = value;
            } else {
                addKeyValueAtIndex(key, value, index);
            }
        }
    }

    public void remove(int key) {
        if (key == EMPTY_KEY) {
            sentinel.hasEmptyKey = false;
        } else if (key == REMOVED_KEY) {
            sentinel.hasRemovedKey = false;
        } else {
            int index = probe(key);
            if (keys[index] == key) {
                removeKeyAtIndex(index);
            }
        }
    }

    private void addKeyValueAtIndex(int key, double value, int index) {
        if (keys[index] == REMOVED_KEY) {
            --occupiedWithSentinels;
        }

        keys[index] = key;
        values[index] = value;
        ++occupiedWithData;
        if (occupiedWithData + occupiedWithSentinels > maxOccupiedWithData()) {
            rehashAndGrow();
        }
    }

    private void removeKeyAtIndex(int index) {
        keys[index] = REMOVED_KEY;
        values[index] = EMPTY_VALUE;
        --occupiedWithData;
        ++occupiedWithSentinels;
    }

    public double get(int key) {
        return getOrDefault(key, EMPTY_VALUE);
    }

    public double getOrDefault(int key, double defaultValue) {
        if (key == EMPTY_KEY && sentinel.hasEmptyKey) {
            return sentinel.emptyKeyValue;
        } else if (key == REMOVED_KEY && sentinel.hasRemovedKey) {
            return sentinel.removedKeyValue;
        } else if (occupiedWithSentinels == 0) {
            return fastGetOrDefault(key, defaultValue);
        } else {
            return slowGetOrDefault(key, defaultValue);
        }
    }

    private double slowGetOrDefault(int key, double defaultValue) {
        final int index = probe(key);
        if (keys[index] == key) {
            return values[index];
        } else {
            return defaultValue;
        }
    }

    private double fastGetOrDefault(int key, double defaultValue) {
        int index = mask(key);

        for (int i = 0; i < INITIAL_LINEAR_PROBE; ++i) {
            final int keyAtIndex = keys[index];
            if (keyAtIndex == key) {
                return values[index];
            } else if (keyAtIndex == EMPTY_KEY) {
                return defaultValue;
            } else {
                index = (index + 1) & (keys.length - 1);
            }
        }
        return slowGetOrDefault2(key, defaultValue);
    }

    private double slowGetOrDefault2(int key, double defaultValue) {
        int index = probeTwo(key, -1);
        if (keys[index] == key) {
            return values[index];
        }
        return defaultValue;
    }

    public boolean containsKey(int key) {
        if (key == EMPTY_KEY) {
            return sentinel.hasEmptyKey;
        } else if (key == REMOVED_KEY) {
            return sentinel.hasRemovedKey;
        } else {
            return keys[probe(key)] == key;
        }
    }

    public boolean containsValue(double value) {
        if (sentinel.contains(value)) {
            return true;
        }
        for (int i = 0; i < values.length; ++i) {
            if (keys[i] != EMPTY_KEY &&
                keys[i] != REMOVED_KEY &&
                Double.compare(values[i], value) == 0)
            {
                return true;
            }
        }

        return false;
    }

    public void forEach(IntDoubleConsumer consumer) {
        if (sentinel.hasEmptyKey) {
            consumer.accept(EMPTY_KEY, sentinel.emptyKeyValue);
        }
        if (sentinel.hasRemovedKey) {
            consumer.accept(REMOVED_KEY, sentinel.removedKeyValue);
        }
        for (int i = 0; i < this.keys.length; i++) {
            if (keys[i] != EMPTY_KEY && keys[i] != REMOVED_KEY) {
                consumer.accept(keys[i], values[i]);
            }
        }
    }

    public void forEachValue(DoubleConsumer consumer) {
        if (sentinel.hasEmptyKey) {
            consumer.accept(sentinel.emptyKeyValue);
        }
        if (sentinel.hasRemovedKey) {
            consumer.accept(sentinel.removedKeyValue);
        }
        for (int i = 0; i < this.keys.length; i++) {
            if (keys[i] != EMPTY_KEY && keys[i] != REMOVED_KEY) {
                consumer.accept(values[i]);
            }
        }
    }

    public DoubleStream values() {
        final var builder = DoubleStream.builder();
        if (sentinel.hasEmptyKey) {
            builder.accept(sentinel.emptyKeyValue);
        }
        if (sentinel.hasRemovedKey) {
            builder.accept(sentinel.removedKeyValue);
        }

        return DoubleStream.concat(
            builder.build(),
            IntStream.range(0, size())
                .filter(i -> keys[i] != EMPTY_KEY && keys[i] != REMOVED_KEY)
                .mapToDouble(i -> values[i])
        );
    }

    public boolean trimToSize() {
        final int newCapacity = smallestPowerOfTwoGreaterThan(size());
        if (keys.length > newCapacity) {
            rehash(newCapacity);
            return true;
        }
        return false;
    }

    private void rehashAndGrow() {
        int max = maxOccupiedWithData();
        int newCapacity = Math.max(max, smallestPowerOfTwoGreaterThan((occupiedWithData + 1) << 1));
        if (occupiedWithSentinels > 0 && (max >> 1) + (max >> 2) < occupiedWithData) {
            newCapacity <<= 1;
        }
        this.rehash(newCapacity);
    }

    private void rehash(int newCapacity) {
        final var oldKeys = keys;
        final var oldValues = values;

        allocate(newCapacity);
        occupiedWithData = 0;
        occupiedWithSentinels = 0;

        for (int i = 0; i < oldKeys.length; ++i) {
            if (oldKeys[i] != EMPTY_KEY && oldKeys[i] != REMOVED_KEY) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }

    int probe(int element) {
        int index = mask(element);
        int keyAtIndex = keys[index];

        if (keyAtIndex == element || keyAtIndex == EMPTY_KEY) {
            return index;
        }

        int removedIndex = keyAtIndex == REMOVED_KEY ? index : -1;
        for (int i = 1; i < INITIAL_LINEAR_PROBE; i++) {
            int nextIndex = (index + i) & (keys.length - 1);
            keyAtIndex = keys[nextIndex];
            if (keyAtIndex == element) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }

        return probeTwo(element, removedIndex);
    }

    int probeTwo(int element, int removedIndex) {
        int index = spreadTwoAndMask(element);
        for (int i = 0; i < INITIAL_LINEAR_PROBE; ++i) {
            int nextIndex = (index + i) & (keys.length - 1);
            int keyAtIndex = keys[nextIndex];
            if (keyAtIndex == element) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }

        return probeThree(element, removedIndex);
    }

    int probeThree(int element, int removedIndex) {
        int nextIndex = SpreadFunctions.intSpreadOne(element);
        int spreadTwo = Integer.reverse(SpreadFunctions.intSpreadTwo(element)) | 1;

        while (true) {
            nextIndex = mask(nextIndex + spreadTwo);
            int keyAtIndex = keys[nextIndex];
            if (keyAtIndex == element) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }
    }

    int spreadAndMask(int element) {
        int code = SpreadFunctions.intSpreadOne(element);
        return this.mask(code);
    }

    int spreadTwoAndMask(int element) {
        int code = SpreadFunctions.intSpreadTwo(element);
        return this.mask(code);
    }

    private int mask(int spread) {
        return spread & (keys.length - 1);
    }

    @Override
    void allocate(int capacity) {
        super.allocate(capacity);
        values = new double[capacity];
    }

    private int maxOccupiedWithData() {
        return keys.length >> 1;
    }

}
