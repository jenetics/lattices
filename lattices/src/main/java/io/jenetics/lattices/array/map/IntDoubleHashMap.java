package io.jenetics.lattices.array.map;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class IntDoubleHashMap //extends AbstractMutableDoubleValuesMap //implements MutableIntDoubleMap, Externalizable, MutableIntKeysMap
{
    private static final double EMPTY_VALUE = 0.0;
    private static final int EMPTY_KEY = 0;
    private static final int REMOVED_KEY = 1;
    private static final int CACHE_LINE_SIZE = 64;
    private static final int KEY_SIZE = 4;
    private static final int INITIAL_LINEAR_PROBE = CACHE_LINE_SIZE / KEY_SIZE / 2; /* half a cache line */

    private static final int DEFAULT_INITIAL_CAPACITY = 8;

    private final Sentinel sentinel = new Sentinel();

    private int[] keys;
    private double[] values;
    private int occupiedWithData;
    private int occupiedWithSentinels;

    private boolean copyKeysOnWrite;

    public IntDoubleHashMap() {
        allocateTable(DEFAULT_INITIAL_CAPACITY << 1);
    }

    public IntDoubleHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be less than 0.");
        }

        final int capacity = smallestPowerOfTwoGreaterThan(initialCapacity << 1);
        allocateTable(capacity);
    }

    public int size() {
        return occupiedWithData + sentinel.size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public void put(int key, double value) {
        if (key == EMPTY_KEY) {
            sentinel.hasZeroKey = true;
            sentinel.zeroValue = value;
        } else if (key == REMOVED_KEY) {
            sentinel.hasOneKey = true;
            sentinel.oneValue = value;
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

    private static boolean isEmptyKey(int key) {
        return key == EMPTY_KEY;
    }

    private static boolean isRemovedKey(int key) {
        return key == REMOVED_KEY;
    }

    private static boolean isNonSentinel(int key) {
        return !isEmptyKey(key) && !isRemovedKey(key);
    }












    private int smallestPowerOfTwoGreaterThan(int n) {
        return n > 1 ? Integer.highestOneBit(n - 1) << 1 : 1;
    }

//    @Override
//    protected double getEmptyValue() {
//        return EMPTY_VALUE;
//    }
//
//    @Override
//    protected int getTableSize() {
//        return this.values.length;
//    }
//
//    @Override
//    protected double getValueAtIndex(int index) {
//        return this.values[index];
//    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if (!(obj instanceof IntDoubleMap)) {
//            return false;
//        }
//
//        IntDoubleMap other = (IntDoubleMap) obj;
//
//        if (this.size() != other.size()) {
//            return false;
//        }
//
//        if (this.sentinel == null) {
//            if (other.containsKey(EMPTY_KEY) || other.containsKey(REMOVED_KEY)) {
//                return false;
//            }
//        } else {
//            if (this.sentinel.containsZeroKey && (!other.containsKey(EMPTY_KEY) || Double.compare(this.sentinel.zeroValue, other.getOrThrow(EMPTY_KEY)) != 0)) {
//                return false;
//            }
//
//            if (this.sentinel.containsOneKey && (!other.containsKey(REMOVED_KEY) || Double.compare(this.sentinel.oneValue, other.getOrThrow(REMOVED_KEY)) != 0)) {
//                return false;
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            int key = this.keys[i];
//            if (isNonSentinel(key) && (!other.containsKey(key) || Double.compare(this.values[i], other.getOrThrow(key)) != 0)) {
//                return false;
//            }
//        }
//        return true;
//    }

//    @Override
//    public int hashCode() {
//        int result = 0;
//
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                result += EMPTY_KEY ^ (int) (Double.doubleToLongBits(this.sentinel.zeroValue) ^ Double.doubleToLongBits(this.sentinel.zeroValue) >>> 32);
//            }
//            if (this.sentinel.containsOneKey) {
//                result += REMOVED_KEY ^ (int) (Double.doubleToLongBits(this.sentinel.oneValue) ^ Double.doubleToLongBits(this.sentinel.oneValue) >>> 32);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i])) {
//                result += this.keys[i] ^ (int) (Double.doubleToLongBits(this.values[i]) ^ Double.doubleToLongBits(this.values[i]) >>> 32);
//            }
//        }
//
//        return result;
//    }

//    @Override
//    public String toString() {
//        StringBuilder appendable = new StringBuilder();
//
//        appendable.append("{");
//
//        boolean first = true;
//
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                appendable.append(EMPTY_KEY).append("=").append(this.sentinel.zeroValue);
//                first = false;
//            }
//            if (this.sentinel.containsOneKey) {
//                if (!first) {
//                    appendable.append(", ");
//                }
//                appendable.append(REMOVED_KEY).append("=").append(this.sentinel.oneValue);
//                first = false;
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            int key = this.keys[i];
//            if (isNonSentinel(key)) {
//                if (!first) {
//                    appendable.append(", ");
//                }
//                appendable.append(key).append("=").append(this.values[i]);
//                first = false;
//            }
//        }
//        appendable.append("}");
//
//        return appendable.toString();
//    }

//    @Override
//    public MutableDoubleIterator doubleIterator() {
//        return new InternalDoubleIterator();
//    }
//
//    @Override
//    public <V> V injectInto(V injectedValue, ObjectDoubleToObjectFunction<? super V, ? extends V> function) {
//        V result = injectedValue;
//
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                result = function.valueOf(result, this.sentinel.zeroValue);
//            }
//            if (this.sentinel.containsOneKey) {
//                result = function.valueOf(result, this.sentinel.oneValue);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i])) {
//                result = function.valueOf(result, this.values[i]);
//            }
//        }
//
//        return result;
//    }

    public void clear() {
        sentinel.clear();
        occupiedWithData = 0;
        occupiedWithSentinels = 0;
        if (copyKeysOnWrite) {
            copyKeys();
        }
        Arrays.fill(this.keys, EMPTY_KEY);
        Arrays.fill(this.values, EMPTY_VALUE);
    }

//    @Override
//    public void putAll(IntDoubleMap map) {
//        map.forEachKeyValue(this::put);
//    }

//    @Override
//    public void updateValues(IntDoubleToDoubleFunction function) {
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                this.sentinel.zeroValue = function.valueOf(EMPTY_KEY, this.sentinel.zeroValue);
//            }
//            if (this.sentinel.containsOneKey) {
//                this.sentinel.oneValue = function.valueOf(REMOVED_KEY, this.sentinel.oneValue);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i])) {
//                this.values[i] = function.valueOf(this.keys[i], this.values[i]);
//            }
//        }
//    }

    public void remove(int key) {
        if (key == EMPTY_KEY) {
            sentinel.hasZeroKey = false;
        } else if (key == REMOVED_KEY) {
            sentinel.hasOneKey = false;
        } else {
            int index = probe(key);
            if (keys[index] == key) {
                removeKeyAtIndex(index);
            }
        }
    }

//    @Override
//    public double removeKeyIfAbsent(int key, double value) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null || !this.sentinel.containsZeroKey) {
//                return value;
//            }
//            double oldValue = this.sentinel.zeroValue;
//            this.removeEmptyKey();
//            return oldValue;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null || !this.sentinel.containsOneKey) {
//                return value;
//            }
//            double oldValue = this.sentinel.oneValue;
//            this.removeRemovedKey();
//            return oldValue;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            double oldValue = this.values[index];
//            this.removeKeyAtIndex(index);
//            return oldValue;
//        }
//        return value;
//    }

//    @Override
//    public double getIfAbsentPut(int key, double value) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//                this.addEmptyKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsZeroKey) {
//                return this.sentinel.zeroValue;
//            }
//            this.addEmptyKeyValue(value);
//            return value;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//                this.addRemovedKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsOneKey) {
//                return this.sentinel.oneValue;
//            }
//            this.addRemovedKeyValue(value);
//            return value;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            return this.values[index];
//        }
//        this.addKeyValueAtIndex(key, value, index);
//        return value;
//    }

//    @Override
//    public double getAndPut(int key, double putValue, double defaultValue) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//            } else if (this.sentinel.containsZeroKey) {
//                double existingValue = this.sentinel.zeroValue;
//                this.sentinel.zeroValue = putValue;
//                return existingValue;
//            }
//            this.addEmptyKeyValue(putValue);
//            return defaultValue;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//            } else if (this.sentinel.containsOneKey) {
//                double existingValue = this.sentinel.oneValue;
//                this.sentinel.oneValue = putValue;
//                return existingValue;
//            }
//            this.addRemovedKeyValue(putValue);
//            return defaultValue;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            double existingValue = this.values[index];
//            this.values[index] = putValue;
//            return existingValue;
//        }
//        this.addKeyValueAtIndex(key, putValue, index);
//        return defaultValue;
//    }

//    @Override
//    public double getIfAbsentPut(int key, DoubleFunction0 function) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                double value = function.value();
//                this.sentinel = new SentinelValues();
//                this.addEmptyKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsZeroKey) {
//                return this.sentinel.zeroValue;
//            }
//            double value = function.value();
//            this.addEmptyKeyValue(value);
//            return value;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                double value = function.value();
//                this.sentinel = new SentinelValues();
//                this.addRemovedKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsOneKey) {
//                return this.sentinel.oneValue;
//            }
//            double value = function.value();
//            this.addRemovedKeyValue(value);
//            return value;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            return this.values[index];
//        }
//        double value = function.value();
//        this.addKeyValueAtIndex(key, value, index);
//        return value;
//    }

//    @Override
//    public <P> double getIfAbsentPutWith(int key, DoubleFunction<? super P> function, P parameter) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                double value = function.doubleValueOf(parameter);
//                this.sentinel = new SentinelValues();
//                this.addEmptyKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsZeroKey) {
//                return this.sentinel.zeroValue;
//            }
//            double value = function.doubleValueOf(parameter);
//            this.addEmptyKeyValue(value);
//            return value;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                double value = function.doubleValueOf(parameter);
//                this.sentinel = new SentinelValues();
//                this.addRemovedKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsOneKey) {
//                return this.sentinel.oneValue;
//            }
//            double value = function.doubleValueOf(parameter);
//            this.addRemovedKeyValue(value);
//            return value;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            return this.values[index];
//        }
//        double value = function.doubleValueOf(parameter);
//        this.addKeyValueAtIndex(key, value, index);
//        return value;
//    }

//    @Override
//    public double getIfAbsentPutWithKey(int key, IntToDoubleFunction function) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                double value = function.valueOf(key);
//                this.sentinel = new SentinelValues();
//                this.addEmptyKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsZeroKey) {
//                return this.sentinel.zeroValue;
//            }
//            double value = function.valueOf(key);
//            this.addEmptyKeyValue(value);
//            return value;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                double value = function.valueOf(key);
//                this.sentinel = new SentinelValues();
//                this.addRemovedKeyValue(value);
//                return value;
//            }
//            if (this.sentinel.containsOneKey) {
//                return this.sentinel.oneValue;
//            }
//            double value = function.valueOf(key);
//            this.addRemovedKeyValue(value);
//            return value;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            return this.values[index];
//        }
//        double value = function.valueOf(key);
//        this.addKeyValueAtIndex(key, value, index);
//        return value;
//    }

//    @Override
//    public double addToValue(int key, double toBeAdded) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//                this.addEmptyKeyValue(toBeAdded);
//            } else if (this.sentinel.containsZeroKey) {
//                this.sentinel.zeroValue += toBeAdded;
//            } else {
//                this.addEmptyKeyValue(toBeAdded);
//            }
//            return this.sentinel.zeroValue;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//                this.addRemovedKeyValue(toBeAdded);
//            } else if (this.sentinel.containsOneKey) {
//                this.sentinel.oneValue += toBeAdded;
//            } else {
//                this.addRemovedKeyValue(toBeAdded);
//            }
//            return this.sentinel.oneValue;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            this.values[index] += toBeAdded;
//            return this.values[index];
//        }
//        this.addKeyValueAtIndex(key, toBeAdded, index);
//        return toBeAdded;
//    }

    private void addKeyValueAtIndex(int key, double value, int index) {
        if (keys[index] == REMOVED_KEY) {
            occupiedWithSentinels--;
        }
        if (copyKeysOnWrite) {
            copyKeys();
        }
        keys[index] = key;
        values[index] = value;
        occupiedWithData++;
        if (occupiedWithData + occupiedWithSentinels > maxOccupiedWithData()) {
            rehashAndGrow();
        }
    }

    private void removeKeyAtIndex(int index) {
        if (copyKeysOnWrite) {
            copyKeys();
        }
        keys[index] = REMOVED_KEY;
        values[index] = EMPTY_VALUE;
        occupiedWithData--;
        occupiedWithSentinels++;
    }

    private void copyKeys() {
        int[] copy = new int[this.keys.length];
        System.arraycopy(this.keys, 0, copy, 0, this.keys.length);
        this.keys = copy;
        this.copyKeysOnWrite = false;
    }

//    @Override
//    public double updateValue(int key, double initialValueIfAbsent, DoubleToDoubleFunction function) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//                this.addEmptyKeyValue(function.valueOf(initialValueIfAbsent));
//            } else if (this.sentinel.containsZeroKey) {
//                this.sentinel.zeroValue = function.valueOf(this.sentinel.zeroValue);
//            } else {
//                this.addEmptyKeyValue(function.valueOf(initialValueIfAbsent));
//            }
//            return this.sentinel.zeroValue;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null) {
//                this.sentinel = new SentinelValues();
//                this.addRemovedKeyValue(function.valueOf(initialValueIfAbsent));
//            } else if (this.sentinel.containsOneKey) {
//                this.sentinel.oneValue = function.valueOf(this.sentinel.oneValue);
//            } else {
//                this.addRemovedKeyValue(function.valueOf(initialValueIfAbsent));
//            }
//            return this.sentinel.oneValue;
//        }
//        int index = this.probe(key);
//        if (this.keys[index] == key) {
//            this.values[index] = function.valueOf(this.values[index]);
//            return this.values[index];
//        }
//        double value = function.valueOf(initialValueIfAbsent);
//        this.addKeyValueAtIndex(key, value, index);
//        return value;
//    }


    public double get(int key) {
        return getOrDefault(key, EMPTY_VALUE);
    }

    public double getOrDefault(int key, double defaultValue) {
        if (key == EMPTY_KEY || key == REMOVED_KEY) {
            return getForSentinel(key, defaultValue);
        }
        if (occupiedWithSentinels == 0) {
            return fastGetIfAbsent(key, defaultValue);
        }
        return slowGetIfAbsent(key, defaultValue);
    }

    private double getForSentinel(int key, double ifAbsent) {
        if (key == EMPTY_KEY) {
            if (!sentinel.hasZeroKey) {
                return ifAbsent;
            }
            return this.sentinel.zeroValue;
        }
        if (!sentinel.hasOneKey) {
            return ifAbsent;
        }
        return this.sentinel.oneValue;
    }

    private double slowGetIfAbsent(int key, double ifAbsent) {
        int index = probe(key);
        if (keys[index] == key) {
            return values[index];
        }
        return ifAbsent;
    }

    private double fastGetIfAbsent(int key, double ifAbsent) {
        int index = mask(key);

        for (int i = 0; i < INITIAL_LINEAR_PROBE; i++) {
            int keyAtIndex = keys[index];
            if (keyAtIndex == key) {
                return values[index];
            }
            if (keyAtIndex == EMPTY_KEY) {
                return ifAbsent;
            }
            index = (index + 1) & (keys.length - 1);
        }
        return slowGetIfAbsentTwo(key, ifAbsent);
    }

    private double slowGetIfAbsentTwo(int key, double ifAbsent) {
        int index = probeTwo(key, -1);
        if (keys[index] == key) {
            return values[index];
        }
        return ifAbsent;
    }

//    public double getOrThrow(int key) {
//        if (isEmptyKey(key)) {
//            if (this.sentinel == null || !this.sentinel.containsZeroKey) {
//                throw new IllegalStateException("Key " + key + " not present.");
//            }
//            return this.sentinel.zeroValue;
//        }
//        if (isRemovedKey(key)) {
//            if (this.sentinel == null || !this.sentinel.containsOneKey) {
//                throw new IllegalStateException("Key " + key + " not present.");
//            }
//            return this.sentinel.oneValue;
//        }
//        int index = this.probe(key);
//        if (isNonSentinel(this.keys[index])) {
//            return this.values[index];
//        }
//        throw new IllegalStateException("Key " + key + " not present.");
//    }

    public boolean containsKey(int key) {
        if (key == EMPTY_KEY) {
            return sentinel.hasZeroKey;
        }
        if (key == REMOVED_KEY) {
            return sentinel.hasOneKey;
        }
        return keys[probe(key)] == key;
    }

//    @Override
//    public void forEachKey(IntProcedure procedure) {
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                procedure.value(EMPTY_KEY);
//            }
//            if (this.sentinel.containsOneKey) {
//                procedure.value(REMOVED_KEY);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i])) {
//                procedure.value(this.keys[i]);
//            }
//        }
//    }

//    @Override
//    public void forEachKeyValue(IntDoubleProcedure procedure) {
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                procedure.value(EMPTY_KEY, this.sentinel.zeroValue);
//            }
//            if (this.sentinel.containsOneKey) {
//                procedure.value(REMOVED_KEY, this.sentinel.oneValue);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i])) {
//                procedure.value(this.keys[i], this.values[i]);
//            }
//        }
//    }

//    @Override
//    public LazyIntIterable keysView() {
//        return new KeysView();
//    }

//    @Override
//    public RichIterable<IntDoublePair> keyValuesView() {
//        return new KeyValuesView();
//    }

//    @Override
//    public MutableDoubleIntMap flipUniqueValues() {
//        MutableDoubleIntMap result = DoubleIntMaps.mutable.empty();
//        this.forEachKeyValue((key, value) -> {
//            if (result.containsKey(value)) {
//                throw new IllegalStateException("Duplicate value: " + value + " found at key: " + result.get(value) + " and key: " + key);
//            }
//            result.put(value, key);
//        });
//        return result;
//    }

//    @Override
//    public IntDoubleHashMap select(IntDoublePredicate predicate) {
//        IntDoubleHashMap result = new IntDoubleHashMap();
//
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey && predicate.accept(EMPTY_KEY, this.sentinel.zeroValue)) {
//                result.put(EMPTY_KEY, this.sentinel.zeroValue);
//            }
//            if (this.sentinel.containsOneKey && predicate.accept(REMOVED_KEY, this.sentinel.oneValue)) {
//                result.put(REMOVED_KEY, this.sentinel.oneValue);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i]) && predicate.accept(this.keys[i], this.values[i])) {
//                result.put(this.keys[i], this.values[i]);
//            }
//        }
//
//        return result;
//    }

//    @Override
//    public IntDoubleHashMap reject(IntDoublePredicate predicate) {
//        IntDoubleHashMap result = new IntDoubleHashMap();
//
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey && !predicate.accept(EMPTY_KEY, this.sentinel.zeroValue)) {
//                result.put(EMPTY_KEY, this.sentinel.zeroValue);
//            }
//            if (this.sentinel.containsOneKey && !predicate.accept(REMOVED_KEY, this.sentinel.oneValue)) {
//                result.put(REMOVED_KEY, this.sentinel.oneValue);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i]) && !predicate.accept(this.keys[i], this.values[i])) {
//                result.put(this.keys[i], this.values[i]);
//            }
//        }
//        return result;
//    }

//    @Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        out.writeInt(this.size());
//        if (this.sentinel != null) {
//            if (this.sentinel.containsZeroKey) {
//                out.writeInt(EMPTY_KEY);
//                out.writeDouble(this.sentinel.zeroValue);
//            }
//            if (this.sentinel.containsOneKey) {
//                out.writeInt(REMOVED_KEY);
//                out.writeDouble(this.sentinel.oneValue);
//            }
//        }
//        for (int i = 0; i < this.keys.length; i++) {
//            if (isNonSentinel(this.keys[i])) {
//                out.writeInt(this.keys[i]);
//                out.writeDouble(this.values[i]);
//            }
//        }
//    }

    /**
     * @since 12.0
     */
    public boolean trimToSize() {
        final int newCapacity = smallestPowerOfTwoGreaterThan(size());
        if (keys.length > newCapacity) {
            rehash(newCapacity);
            return true;
        }
        return false;
    }

//    /**
//     * Rehashes every element in the set into a new backing table of the
//     * smallest possible size and eliminating removed sentinels.
//     *
//     * @deprecated since 12.0 - Use {@link #trimToSize()} instead
//     */
//    @Deprecated
//    public void compact() {
//        this.rehash(this.smallestPowerOfTwoGreaterThan(this.size()));
//    }

    private void rehashAndGrow() {
        int max = this.maxOccupiedWithData();
        int newCapacity = Math.max(max, smallestPowerOfTwoGreaterThan((this.occupiedWithData + 1) << 1));
        if (this.occupiedWithSentinels > 0 && (max >> 1) + (max >> 2) < this.occupiedWithData) {
            newCapacity <<= 1;
        }
        this.rehash(newCapacity);
    }

    private void rehash(int newCapacity) {
        int oldLength = this.keys.length;
        int[] old = this.keys;
        double[] oldValues = this.values;
        this.allocateTable(newCapacity);
        this.occupiedWithData = 0;
        this.occupiedWithSentinels = 0;

        for (int i = 0; i < oldLength; i++) {
            if (isNonSentinel(old[i])) {
                this.put(old[i], oldValues[i]);
            }
        }
    }

    // exposed for testing
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
        int index = this.spreadTwoAndMask(element);
        for (int i = 0; i < INITIAL_LINEAR_PROBE; ++i) {
            int nextIndex = (index + i) & (this.keys.length - 1);
            int keyAtIndex = this.keys[nextIndex];
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
            nextIndex = this.mask(nextIndex + spreadTwo);
            int keyAtIndex = this.keys[nextIndex];
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

    // exposed for testing
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

    protected void allocateTable(int sizeToAllocate) {
        keys = new int[sizeToAllocate];
        values = new double[sizeToAllocate];
    }


    private int maxOccupiedWithData() {
        return this.keys.length >> 1;
    }

//    @Override
//    public MutableIntSet keySet() {
//        return new KeySet();
//    }

//    @Override
//    public MutableDoubleCollection values() {
//        return new ValuesCollection();
//    }

//    private class InternalDoubleIterator implements MutableDoubleIterator {
//        private int count;
//        private int position;
//        private int lastKey;
//        private boolean handledZero;
//        private boolean handledOne;
//        private boolean canRemove;
//
//        @Override
//        public boolean hasNext() {
//            return this.count < IntDoubleHashMap.this.size();
//        }
//
//        @Override
//        public double next() {
//            if (!this.hasNext()) {
//                throw new NoSuchElementException("next() called, but the iterator is exhausted");
//            }
//            this.count++;
//            this.canRemove = true;
//
//            if (!this.handledZero) {
//                this.handledZero = true;
//                if (IntDoubleHashMap.this.containsKey(EMPTY_KEY)) {
//                    this.lastKey = EMPTY_KEY;
//                    return IntDoubleHashMap.this.get(EMPTY_KEY);
//                }
//            }
//            if (!this.handledOne) {
//                this.handledOne = true;
//                if (IntDoubleHashMap.this.containsKey(REMOVED_KEY)) {
//                    this.lastKey = REMOVED_KEY;
//                    return IntDoubleHashMap.this.get(REMOVED_KEY);
//                }
//            }
//            int[] keys = IntDoubleHashMap.this.keys;
//            while (!isNonSentinel(keys[this.position])) {
//                this.position++;
//            }
//            this.lastKey = keys[this.position];
//            double result = IntDoubleHashMap.this.values[this.position];
//            this.position++;
//
//            return result;
//        }
//
//        @Override
//        public void remove() {
//            if (!this.canRemove) {
//                throw new IllegalStateException();
//            }
//            IntDoubleHashMap.this.removeKey(this.lastKey);
//            this.count--;
//            this.canRemove = false;
//        }
//    }

//    private class KeysView extends AbstractLazyIntIterable {
//        @Override
//        public IntIterator intIterator() {
//            return new UnmodifiableIntIterator(new KeySetIterator());
//        }
//
//        /**
//         * @since 7.0.
//         */
//        @Override
//        public void each(IntProcedure procedure) {
//            IntDoubleHashMap.this.forEachKey(procedure);
//        }
//    }

//    private class KeySetIterator implements MutableIntIterator {
//        private int count;
//        private int position;
//        private int lastKey;
//        private boolean handledZero;
//        private boolean handledOne;
//        private boolean canRemove;
//
//        @Override
//        public boolean hasNext() {
//            return this.count < IntDoubleHashMap.this.size();
//        }
//
//        @Override
//        public int next() {
//            if (!this.hasNext()) {
//                throw new NoSuchElementException("next() called, but the iterator is exhausted");
//            }
//            this.count++;
//            this.canRemove = true;
//
//            if (!this.handledZero) {
//                this.handledZero = true;
//                if (IntDoubleHashMap.this.containsKey(EMPTY_KEY)) {
//                    this.lastKey = EMPTY_KEY;
//                    return this.lastKey;
//                }
//            }
//            if (!this.handledOne) {
//                this.handledOne = true;
//                if (IntDoubleHashMap.this.containsKey(REMOVED_KEY)) {
//                    this.lastKey = REMOVED_KEY;
//                    return this.lastKey;
//                }
//            }
//
//            int[] keys = IntDoubleHashMap.this.keys;
//            while (!isNonSentinel(keys[this.position])) {
//                this.position++;
//            }
//            this.lastKey = keys[this.position];
//            this.position++;
//
//            return this.lastKey;
//        }
//
//        @Override
//        public void remove() {
//            if (!this.canRemove) {
//                throw new IllegalStateException();
//            }
//            IntDoubleHashMap.this.removeKey(this.lastKey);
//            this.count--;
//            this.canRemove = false;
//        }
//    }

//    private class KeySet extends AbstractMutableIntKeySet {
//        @Override
//        protected MutableIntKeysMap getOuter() {
//            return IntDoubleHashMap.this;
//        }
//
//        @Override
//        protected SentinelValues getSentinelValues() {
//            return IntDoubleHashMap.this.sentinel;
//        }
//
//        @Override
//        protected int getKeyAtIndex(int index) {
//            return IntDoubleHashMap.this.keys[index];
//        }
//
//        @Override
//        protected int getTableSize() {
//            return IntDoubleHashMap.this.keys.length;
//        }
//
//        @Override
//        public MutableIntIterator intIterator() {
//            return new KeySetIterator();
//        }
//
//        @Override
//        public boolean retainAll(IntIterable source) {
//            int oldSize = IntDoubleHashMap.this.size();
//            IntSet sourceSet = source instanceof IntSet ? (IntSet) source : source.toSet();
//            IntDoubleHashMap retained = IntDoubleHashMap.this.select((int key, double value) -> sourceSet.contains(key));
//            if (retained.size() != oldSize) {
//                IntDoubleHashMap.this.keys = retained.keys;
//                IntDoubleHashMap.this.values = retained.values;
//                IntDoubleHashMap.this.sentinel = retained.sentinel;
//                IntDoubleHashMap.this.occupiedWithData = retained.occupiedWithData;
//                IntDoubleHashMap.this.occupiedWithSentinels = retained.occupiedWithSentinels;
//                return true;
//            }
//            return false;
//        }
//
//        @Override
//        public boolean retainAll(int... source) {
//            return this.retainAll(IntHashSet.newSetWith(source));
//        }
//
//        @Override
//        public IntSet freeze() {
//            IntDoubleHashMap.this.copyKeysOnWrite = true;
//            boolean containsZeroKey = false;
//            boolean containsOneKey = false;
//            if (IntDoubleHashMap.this.sentinel != null) {
//                containsZeroKey = IntDoubleHashMap.this.sentinel.containsZeroKey;
//                containsOneKey = IntDoubleHashMap.this.sentinel.containsOneKey;
//            }
//            return new ImmutableIntMapKeySet(IntDoubleHashMap.this.keys, IntDoubleHashMap.this.occupiedWithData, containsZeroKey, containsOneKey);
//        }
//
//        /**
//         * @since 9.2.
//         */
//        @Override
//        public MutableIntSet newEmpty() {
//            return new IntHashSet();
//        }
//    }

//    private class ValuesCollection extends AbstractDoubleValuesCollection {
//        @Override
//        public MutableDoubleIterator doubleIterator() {
//            return IntDoubleHashMap.this.doubleIterator();
//        }
//
//        @Override
//        public boolean remove(double item) {
//            int oldSize = IntDoubleHashMap.this.size();
//
//            if (IntDoubleHashMap.this.sentinel != null) {
//                if (IntDoubleHashMap.this.sentinel.containsZeroKey && Double.compare(item, IntDoubleHashMap.this.sentinel.zeroValue) == 0) {
//                    IntDoubleHashMap.this.removeKey(EMPTY_KEY);
//                }
//            }
//            if (IntDoubleHashMap.this.sentinel != null) {
//                if (IntDoubleHashMap.this.sentinel.containsOneKey && Double.compare(item, IntDoubleHashMap.this.sentinel.oneValue) == 0) {
//                    IntDoubleHashMap.this.removeKey(REMOVED_KEY);
//                }
//            }
//            for (int i = 0; i < IntDoubleHashMap.this.keys.length; i++) {
//                if (isNonSentinel(IntDoubleHashMap.this.keys[i]) && Double.compare(item, IntDoubleHashMap.this.values[i]) == 0) {
//                    IntDoubleHashMap.this.removeKey(IntDoubleHashMap.this.keys[i]);
//                }
//            }
//            return oldSize != IntDoubleHashMap.this.size();
//        }
//
//        @Override
//        public boolean retainAll(DoubleIterable source) {
//            int oldSize = IntDoubleHashMap.this.size();
//            DoubleSet sourceSet = source instanceof DoubleSet ? (DoubleSet) source : source.toSet();
//            IntDoubleHashMap retained = IntDoubleHashMap.this.select((int key, double value) -> sourceSet.contains(value));
//            if (retained.size() != oldSize) {
//                IntDoubleHashMap.this.keys = retained.keys;
//                IntDoubleHashMap.this.values = retained.values;
//                IntDoubleHashMap.this.sentinel = retained.sentinel;
//                IntDoubleHashMap.this.occupiedWithData = retained.occupiedWithData;
//                IntDoubleHashMap.this.occupiedWithSentinels = retained.occupiedWithSentinels;
//                return true;
//            }
//            return false;
//        }
//
//        /**
//         * @since 9.2.
//         */
//        @Override
//        public MutableDoubleCollection newEmpty() {
//            return new DoubleHashBag();
//        }
//    }

//    private class KeyValuesView extends AbstractLazyIterable<IntDoublePair> {
//        @Override
//        public void each(Procedure<? super IntDoublePair> procedure) {
//            if (IntDoubleHashMap.this.sentinel != null) {
//                if (IntDoubleHashMap.this.sentinel.containsZeroKey) {
//                    procedure.value(PrimitiveTuples.pair(EMPTY_KEY, IntDoubleHashMap.this.sentinel.zeroValue));
//                }
//                if (IntDoubleHashMap.this.sentinel.containsOneKey) {
//                    procedure.value(PrimitiveTuples.pair(REMOVED_KEY, IntDoubleHashMap.this.sentinel.oneValue));
//                }
//            }
//            for (int i = 0; i < IntDoubleHashMap.this.keys.length; i++) {
//                if (isNonSentinel(IntDoubleHashMap.this.keys[i])) {
//                    procedure.value(PrimitiveTuples.pair(IntDoubleHashMap.this.keys[i], IntDoubleHashMap.this.values[i]));
//                }
//            }
//        }
//
//        @Override
//        public void forEachWithIndex(ObjectIntProcedure<? super IntDoublePair> objectIntProcedure) {
//            int index = 0;
//            if (IntDoubleHashMap.this.sentinel != null) {
//                if (IntDoubleHashMap.this.sentinel.containsZeroKey) {
//                    objectIntProcedure.value(PrimitiveTuples.pair(EMPTY_KEY, IntDoubleHashMap.this.sentinel.zeroValue), index);
//                    index++;
//                }
//                if (IntDoubleHashMap.this.sentinel.containsOneKey) {
//                    objectIntProcedure.value(PrimitiveTuples.pair(REMOVED_KEY, IntDoubleHashMap.this.sentinel.oneValue), index);
//                    index++;
//                }
//            }
//            for (int i = 0; i < IntDoubleHashMap.this.keys.length; i++) {
//                if (isNonSentinel(IntDoubleHashMap.this.keys[i])) {
//                    objectIntProcedure.value(PrimitiveTuples.pair(IntDoubleHashMap.this.keys[i], IntDoubleHashMap.this.values[i]), index);
//                    index++;
//                }
//            }
//        }
//
//        @Override
//        public <P> void forEachWith(Procedure2<? super IntDoublePair, ? super P> procedure, P parameter) {
//            if (IntDoubleHashMap.this.sentinel != null) {
//                if (IntDoubleHashMap.this.sentinel.containsZeroKey) {
//                    procedure.value(PrimitiveTuples.pair(EMPTY_KEY, IntDoubleHashMap.this.sentinel.zeroValue), parameter);
//                }
//                if (IntDoubleHashMap.this.sentinel.containsOneKey) {
//                    procedure.value(PrimitiveTuples.pair(REMOVED_KEY, IntDoubleHashMap.this.sentinel.oneValue), parameter);
//                }
//            }
//            for (int i = 0; i < IntDoubleHashMap.this.keys.length; i++) {
//                if (isNonSentinel(IntDoubleHashMap.this.keys[i])) {
//                    procedure.value(PrimitiveTuples.pair(IntDoubleHashMap.this.keys[i], IntDoubleHashMap.this.values[i]), parameter);
//                }
//            }
//        }
//
//        @Override
//        public Iterator<IntDoublePair> iterator() {
//            return new InternalKeyValuesIterator();
//        }
//
//        public class InternalKeyValuesIterator implements Iterator<IntDoublePair> {
//            private int count;
//            private int position;
//            private boolean handledZero;
//            private boolean handledOne;
//
//            @Override
//            public IntDoublePair next() {
//                if (!this.hasNext()) {
//                    throw new NoSuchElementException("next() called, but the iterator is exhausted");
//                }
//                this.count++;
//
//                if (!this.handledZero) {
//                    this.handledZero = true;
//                    if (IntDoubleHashMap.this.containsKey(EMPTY_KEY)) {
//                        return PrimitiveTuples.pair(EMPTY_KEY, IntDoubleHashMap.this.sentinel.zeroValue);
//                    }
//                }
//                if (!this.handledOne) {
//                    this.handledOne = true;
//                    if (IntDoubleHashMap.this.containsKey(REMOVED_KEY)) {
//                        return PrimitiveTuples.pair(REMOVED_KEY, IntDoubleHashMap.this.sentinel.oneValue);
//                    }
//                }
//
//                int[] keys = IntDoubleHashMap.this.keys;
//                while (!isNonSentinel(keys[this.position])) {
//                    this.position++;
//                }
//                IntDoublePair result = PrimitiveTuples.pair(keys[this.position], IntDoubleHashMap.this.values[this.position]);
//                this.position++;
//                return result;
//            }
//
//            @Override
//            public void remove() {
//                throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
//            }
//
//            @Override
//            public boolean hasNext() {
//                return this.count != IntDoubleHashMap.this.size();
//            }
//        }
//    }
}
