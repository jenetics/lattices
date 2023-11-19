package io.jenetics.lattices.array;

public class IntDoubleMap {


    //public static int hashCollisions = 0;
    protected static final byte FREE = 0;
    protected static final byte FULL = 1;
    protected static final byte REMOVED = 2;
    /**
     * The hash table keys.
     *
     * @serial
     */
    protected int[] table;
    /**
     * The hash table values.
     *
     * @serial
     */
    protected double[] values;
    /**
     * The state of each hash table entry (FREE, FULL, REMOVED).
     *
     * @serial
     */
    protected byte[] state;
    /**
     * The number of table entries in state==FREE.
     *
     * @serial
     */
    protected int freeEntries;

    /**
     * Constructs an empty map with default capacity and default load factors.
     */
    public IntDoubleMap() {
        this(defaultCapacity);
    }

    /**
     * Constructs an empty map with the specified initial capacity and default
     * load factors.
     *
     * @param initialCapacity the initial capacity of the map.
     * @throws IllegalArgumentException if the initial capacity is less than
     * zero.
     */
    public IntDoubleMap(int initialCapacity) {
        this(initialCapacity, defaultMinLoadFactor, defaultMaxLoadFactor);
    }

    /**
     * Constructs an empty map with the specified initial capacity and the
     * specified minimum and maximum load factor.
     *
     * @param initialCapacity the initial capacity.
     * @param minLoadFactor the minimum load factor.
     * @param maxLoadFactor the maximum load factor.
     * @throws IllegalArgumentException if <tt>initialCapacity < 0 ||
     * (minLoadFactor < 0.0 || minLoadFactor >= 1.0) || (maxLoadFactor <= 0.0 ||
     * maxLoadFactor >= 1.0) || (minLoadFactor >= maxLoadFactor)</tt>.
     */
    public IntDoubleMap(int initialCapacity, double minLoadFactor, double maxLoadFactor) {
        setUp(initialCapacity, minLoadFactor, maxLoadFactor);
    }

    /**
     * Assigns the result of a function to each value; <tt>v[i] =
     * function(v[i])</tt>.
     *
     * @param function a function object taking as argument the current
     * association's value.
     */
    public void assign(cern.colt.function.DoubleFunction function) {
        // specialization for speed
        if (function instanceof cern.jet.math.Mult) { // x[i] = mult*x[i]
            double multiplicator = ((cern.jet.math.Mult) function).multiplicator;
            if (multiplicator == 1) return;
            if (multiplicator == 0) {
                clear();
                return;
            }
            for (int i = table.length; i-- > 0; ) {
                if (state[i] == FULL) values[i] *= multiplicator;
            }
        } else { // the general case x[i] = f(x[i])
            for (int i = table.length; i-- > 0; ) {
                if (state[i] == FULL) values[i] = function.apply(values[i]);
            }
        }
    }

    /**
     * Clears the receiver, then adds all (key,value) pairs of
     * <tt>other</tt>values to it.
     *
     * @param other the other map to be copied into the receiver.
     */
    public void assign(AbstractIntDoubleMap other) {
        if (!(other instanceof OpenIntDoubleHashMap)) {
            super.assign(other);
            return;
        }
        OpenIntDoubleHashMap source = (OpenIntDoubleHashMap) other;
        OpenIntDoubleHashMap copy = (OpenIntDoubleHashMap) source.copy();
        this.values = copy.values;
        this.table = copy.table;
        this.state = copy.state;
        this.freeEntries = copy.freeEntries;
        this.distinct = copy.distinct;
        this.lowWaterMark = copy.lowWaterMark;
        this.highWaterMark = copy.highWaterMark;
        this.minLoadFactor = copy.minLoadFactor;
        this.maxLoadFactor = copy.maxLoadFactor;
    }

    /**
     * Removes all (key,value) associations from the receiver. Implicitly calls
     * <tt>trimToSize()</tt>.
     */
    public void clear() {
        new ByteArrayList(this.state).fillFromToWith(0, this.state.length - 1, FREE);
        //new DoubleArrayList(values).fillFromToWith(0, state.length-1, 0); // delta

	/*
	if (debug) {
		for (int i=table.length; --i >= 0; ) {
		    state[i] = FREE;
		    table[i]= Integer.MAX_VALUE;
		    values[i]= Double.NaN;
		}
	}
	*/

        this.distinct = 0;
        this.freeEntries = table.length; // delta
        trimToSize();
    }

    /**
     * Returns a deep copy of the receiver.
     *
     * @return a deep copy of the receiver.
     */
    public Object clone() {
        OpenIntDoubleHashMap copy = (OpenIntDoubleHashMap) super.clone();
        copy.table = copy.table.clone();
        copy.values = copy.values.clone();
        copy.state = copy.state.clone();
        return copy;
    }

    /**
     * Returns <tt>true</tt> if the receiver contains the specified key.
     *
     * @return <tt>true</tt> if the receiver contains the specified key.
     */
    public boolean containsKey(int key) {
        return indexOfKey(key) >= 0;
    }

    /**
     * Returns <tt>true</tt> if the receiver contains the specified value.
     *
     * @return <tt>true</tt> if the receiver contains the specified value.
     */
    public boolean containsValue(double value) {
        return indexOfValue(value) >= 0;
    }

    /**
     * Ensures that the receiver can hold at least the specified number of
     * associations without needing to allocate new internal memory. If
     * necessary, allocates new internal memory and increases the capacity of
     * the receiver.
     * <p>
     * This method never need be called; it is for performance tuning only.
     * Calling this method before <tt>put()</tt>ing a large number of
     * associations boosts performance, because the receiver will grow only once
     * instead of potentially many times and hash collisions get less probable.
     *
     * @param minCapacity the desired minimum capacity.
     */
    public void ensureCapacity(int minCapacity) {
        if (table.length < minCapacity) {
            int newCapacity = nextPrime(minCapacity);
            rehash(newCapacity);
        }
    }

    /**
     * Applies a procedure to each key of the receiver, if any. Note: Iterates
     * over the keys in no particular order. Subclasses can define a particular
     * order, for example, "sorted by key". All methods which <i>can</i> be
     * expressed in terms of this method (most methods can) <i>must
     * guarantee</i> to use the <i>same</i> order defined by this method, even
     * if it is no particular order. This is necessary so that, for example,
     * methods <tt>keys</tt> and <tt>values</tt> will yield association pairs,
     * not two uncorrelated lists.
     *
     * @param procedure the procedure to be applied. Stops iteration if the
     * procedure returns <tt>false</tt>, otherwise continues.
     * @return <tt>false</tt> if the procedure stopped before all keys where
     * iterated over, <tt>true</tt> otherwise.
     */
    public boolean forEachKey(IntProcedure procedure) {
        for (int i = table.length; i-- > 0; ) {
            if (state[i] == FULL) if (!procedure.apply(table[i])) return false;
        }
        return true;
    }

    /**
     * Applies a procedure to each (key,value) pair of the receiver, if any.
     * Iteration order is guaranteed to be <i>identical</i> to the order used by
     * method {@link #forEachKey(IntProcedure)}.
     *
     * @param procedure the procedure to be applied. Stops iteration if the
     * procedure returns <tt>false</tt>, otherwise continues.
     * @return <tt>false</tt> if the procedure stopped before all keys where
     * iterated over, <tt>true</tt> otherwise.
     */
    public boolean forEachPair(final IntDoubleProcedure procedure) {
        for (int i = table.length; i-- > 0; ) {
            if (state[i] == FULL)
                if (!procedure.apply(table[i], values[i])) return false;
        }
        return true;
    }

    /**
     * Returns the value associated with the specified key. It is often a good
     * idea to first check with {@link #containsKey(int)} whether the given key
     * has a value associated or not, i.e. whether there exists an association
     * for the given key or not.
     *
     * @param key the key to be searched for.
     * @return the value associated with the specified key; <tt>0</tt> if no
     * such key is present.
     */
    public double get(int key) {
        int i = indexOfKey(key);
        if (i < 0) return 0; //not contained
        return values[i];
    }

    /**
     * @param key the key to be added to the receiver.
     * @return the index where the key would need to be inserted, if it is not
     * already contained. Returns -index-1 if the key is already contained at
     * slot index. Therefore, if the returned index < 0, then it is already
     * contained at slot -index-1. If the returned index >= 0, then it is NOT
     * already contained and should be inserted at slot index.
     */
    protected int indexOfInsertion(int key) {
        final int[] tab = table;
        final byte[] stat = state;
        final int length = tab.length;

        final int hash = HashFunctions.hash(key) & 0x7FFFFFFF;
        int i = hash % length;
        int decrement = hash % (length - 2); // double hashing, see http://www.eece.unm.edu/faculty/heileman/hash/node4.html
        //int decrement = (hash / length) % length;
        if (decrement == 0) decrement = 1;

        // stop if we find a removed or free slot, or if we find the key itself
        // do NOT skip over removed slots (yes, open addressing is like that...)
        while (stat[i] == FULL && tab[i] != key) {
            i -= decrement;
            //hashCollisions++;
            if (i < 0) i += length;
        }

        if (stat[i] == REMOVED) {
            // stop if we find a free slot, or if we find the key itself.
            // do skip over removed slots (yes, open addressing is like that...)
            // assertion: there is at least one FREE slot.
            int j = i;
            while (stat[i] != FREE && (stat[i] == REMOVED || tab[i] != key)) {
                i -= decrement;
                //hashCollisions++;
                if (i < 0) i += length;
            }
            if (stat[i] == FREE) i = j;
        }


        if (stat[i] == FULL) {
            // key already contained at slot i.
            // return a negative number identifying the slot.
            return -i - 1;
        }
        // not already contained, should be inserted at slot i.
        // return a number >= 0 identifying the slot.
        return i;
    }

    /**
     * @param key the key to be searched in the receiver.
     * @return the index where the key is contained in the receiver, else
     * returns -1.
     */
    protected int indexOfKey(int key) {
        final int[] tab = table;
        final byte[] stat = state;
        final int length = tab.length;

        final int hash = HashFunctions.hash(key) & 0x7FFFFFFF;
        int i = hash % length;
        int decrement = hash % (length - 2); // double hashing, see http://www.eece.unm.edu/faculty/heileman/hash/node4.html
        //int decrement = (hash / length) % length;
        if (decrement == 0) decrement = 1;

        // stop if we find a free slot, or if we find the key itself.
        // do skip over removed slots (yes, open addressing is like that...)
        // assertion: there is at least one FREE slot.
        while (stat[i] != FREE && (stat[i] == REMOVED || tab[i] != key)) {
            i -= decrement;
            //hashCollisions++;
            if (i < 0) i += length;
        }

        if (stat[i] == FREE) return -1; // not found
        return i; //found, return index where key is contained
    }

    /**
     * @param value the value to be searched in the receiver.
     * @return the index where the value is contained in the receiver, returns
     * -1 if the value was not found.
     */
    protected int indexOfValue(double value) {
        final double[] val = values;
        final byte[] stat = state;

        for (int i = stat.length; --i >= 0; ) {
            if (stat[i] == FULL && val[i] == value) return i;
        }

        return -1; // not found
    }

    /**
     * Returns the first key the given value is associated with. It is often a
     * good idea to first check with {@link #containsValue(double)} whether
     * there exists an association from a key to this value. Search order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     *
     * @param value the value to search for.
     * @return the first key for which holds <tt>get(key) == value</tt>; returns
     * <tt>Integer.MIN_VALUE</tt> if no such key exists.
     */
    public int keyOf(double value) {
        //returns the first key found; there may be more matching keys, however.
        int i = indexOfValue(value);
        if (i < 0) return Integer.MIN_VALUE;
        return table[i];
    }

    /**
     * Fills all keys contained in the receiver into the specified list. Fills
     * the list, starting at index 0. After this call returns the specified list
     * has a new size that equals <tt>this.size()</tt>. Iteration order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     * <p>
     * This method can be used to iterate over the keys of the receiver.
     *
     * @param list the list to be filled, can have any size.
     */
    public void keys(IntArrayList list) {
        list.setSize(distinct);
        int[] elements = list.elements();

        int[] tab = table;
        byte[] stat = state;

        int j = 0;
        for (int i = tab.length; i-- > 0; ) {
            if (stat[i] == FULL) elements[j++] = tab[i];
        }
    }

    /**
     * Fills all pairs satisfying a given condition into the specified lists.
     * Fills into the lists, starting at index 0. After this call returns the
     * specified lists both have a new size, the number of pairs satisfying the
     * condition. Iteration order is guaranteed to be <i>identical</i> to the
     * order used by method {@link #forEachKey(IntProcedure)}.
     * <p>
     * <b>Example:</b>
     * <br>
     * <pre>
     * IntDoubleProcedure condition = new IntDoubleProcedure() { // match even keys only
     * public boolean apply(int key, double value) { return key%2==0; }
     * }
     * keys = (8,7,6), values = (1,2,2) --> keyList = (6,8), valueList = (2,1)</tt>
     * </pre>
     *
     * @param condition the condition to be matched. Takes the current key as
     * first and the current value as second argument.
     * @param keyList the list to be filled with keys, can have any size.
     * @param valueList the list to be filled with values, can have any size.
     */
    public void pairsMatching(final IntDoubleProcedure condition, final IntArrayList keyList, final DoubleArrayList valueList) {
        keyList.clear();
        valueList.clear();

        for (int i = table.length; i-- > 0; ) {
            if (state[i] == FULL && condition.apply(table[i], values[i])) {
                keyList.add(table[i]);
                valueList.add(values[i]);
            }
        }
    }

    /**
     * Associates the given key with the given value. Replaces any old
     * <tt>(key,someOtherValue)</tt> association, if existing.
     *
     * @param key the key the value shall be associated with.
     * @param value the value to be associated.
     * @return <tt>true</tt> if the receiver did not already contain such a key;
     * <tt>false</tt> if the receiver did already contain such a key - the new
     * value has now replaced the formerly associated value.
     */
    public boolean put(int key, double value) {
        int i = indexOfInsertion(key);
        if (i < 0) { //already contained
            i = -i - 1;
            //if (debug) if (this.state[i] != FULL) throw new InternalError();
            //if (debug) if (this.table[i] != key) throw new InternalError();
            this.values[i] = value;
            return false;
        }

        if (this.distinct > this.highWaterMark) {
            int newCapacity = chooseGrowCapacity(this.distinct + 1, this.minLoadFactor, this.maxLoadFactor);
		/*
		System.out.print("grow rehashing ");
		System.out.println("at distinct="+distinct+", capacity="+table.length+" to newCapacity="+newCapacity+" ...");
		*/
            rehash(newCapacity);
            return put(key, value);
        }

        this.table[i] = key;
        this.values[i] = value;
        if (this.state[i] == FREE) this.freeEntries--;
        this.state[i] = FULL;
        this.distinct++;

        if (this.freeEntries < 1) { //delta
            int newCapacity = chooseGrowCapacity(this.distinct + 1, this.minLoadFactor, this.maxLoadFactor);
            rehash(newCapacity);
        }

        return true;
    }

    /**
     * Rehashes the contents of the receiver into a new table with a smaller or
     * larger capacity. This method is called automatically when the number of
     * keys in the receiver exceeds the high water mark or falls below the low
     * water mark.
     */
    protected void rehash(int newCapacity) {
        int oldCapacity = table.length;
        //if (oldCapacity == newCapacity) return;

        if (newCapacity <= this.distinct) throw new InternalError();
        //if (debug) check();

        int[] oldTable = table;
        double[] oldValues = values;
        byte[] oldState = state;

        int[] newTable = new int[newCapacity];
        double[] newValues = new double[newCapacity];
        byte[] newState = new byte[newCapacity];

        this.lowWaterMark = chooseLowWaterMark(newCapacity, this.minLoadFactor);
        this.highWaterMark = chooseHighWaterMark(newCapacity, this.maxLoadFactor);

        this.table = newTable;
        this.values = newValues;
        this.state = newState;
        this.freeEntries = newCapacity - this.distinct; // delta

        for (int i = oldCapacity; i-- > 0; ) {
            if (oldState[i] == FULL) {
                int element = oldTable[i];
                int index = indexOfInsertion(element);
                newTable[index] = element;
                newValues[index] = oldValues[i];
                newState[index] = FULL;

            }
        }

        //if (debug) check();
    }

    /**
     * Removes the given key with its associated element from the receiver, if
     * present.
     *
     * @param key the key to be removed from the receiver.
     * @return <tt>true</tt> if the receiver contained the specified key,
     * <tt>false</tt> otherwise.
     */
    public boolean removeKey(int key) {
        int i = indexOfKey(key);
        if (i < 0) return false; // key not contained

        //if (debug) if (this.state[i] == FREE) throw new InternalError();
        //if (debug) if (this.state[i] == REMOVED) throw new InternalError();
        this.state[i] = REMOVED;
        //this.values[i]=0; // delta

        //if (debug) this.table[i]=Integer.MAX_VALUE; // delta
        //if (debug) this.values[i]=Double.NaN; // delta
        this.distinct--;

        if (this.distinct < this.lowWaterMark) {
            int newCapacity = chooseShrinkCapacity(this.distinct, this.minLoadFactor, this.maxLoadFactor);
		/*
		if (table.length != newCapacity) {
			System.out.print("shrink rehashing ");
			System.out.println("at distinct="+distinct+", capacity="+table.length+" to newCapacity="+newCapacity+" ...");
		}
		*/
            rehash(newCapacity);
        }

        return true;
    }

    /**
     * Initializes the receiver.
     *
     * @param initialCapacity the initial capacity of the receiver.
     * @param minLoadFactor the minLoadFactor of the receiver.
     * @param maxLoadFactor the maxLoadFactor of the receiver.
     * @throws IllegalArgumentException if <tt>initialCapacity < 0 ||
     * (minLoadFactor < 0.0 || minLoadFactor >= 1.0) || (maxLoadFactor <= 0.0 ||
     * maxLoadFactor >= 1.0) || (minLoadFactor >= maxLoadFactor)</tt>.
     */
    protected void setUp(int initialCapacity, double minLoadFactor, double maxLoadFactor) {
        int capacity = initialCapacity;
        super.setUp(capacity, minLoadFactor, maxLoadFactor);
        capacity = nextPrime(capacity);
        if (capacity == 0)
            capacity = 1; // open addressing needs at least one FREE slot at any time.

        this.table = new int[capacity];
        this.values = new double[capacity];
        this.state = new byte[capacity];

        // memory will be exhausted long before this pathological case happens, anyway.
        this.minLoadFactor = minLoadFactor;
        if (capacity == PrimeFinder.largestPrime) this.maxLoadFactor = 1.0;
        else this.maxLoadFactor = maxLoadFactor;

        this.distinct = 0;
        this.freeEntries = capacity; // delta

        // lowWaterMark will be established upon first expansion.
        // establishing it now (upon instance construction) would immediately make the table shrink upon first put(...).
        // After all the idea of an "initialCapacity" implies violating lowWaterMarks when an object is young.
        // See ensureCapacity(...)
        this.lowWaterMark = 0;
        this.highWaterMark = chooseHighWaterMark(capacity, this.maxLoadFactor);
    }

    /**
     * Trims the capacity of the receiver to be the receiver's current size.
     * Releases any superfluous internal memory. An application can use this
     * operation to minimize the storage of the receiver.
     */
    public void trimToSize() {
        // * 1.2 because open addressing's performance exponentially degrades beyond that point
        // so that even rehashing the table can take very long
        int newCapacity = nextPrime((int) (1 + 1.2 * size()));
        if (table.length > newCapacity) {
            rehash(newCapacity);
        }
    }

    /**
     * Fills all values contained in the receiver into the specified list. Fills
     * the list, starting at index 0. After this call returns the specified list
     * has a new size that equals <tt>this.size()</tt>. Iteration order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     * <p>
     * This method can be used to iterate over the values of the receiver.
     *
     * @param list the list to be filled, can have any size.
     */
    public void values(DoubleArrayList list) {
        list.setSize(distinct);
        double[] elements = list.elements();

        double[] val = values;
        byte[] stat = state;

        int j = 0;
        for (int i = stat.length; i-- > 0; ) {
            if (stat[i] == FULL) elements[j++] = val[i];
        }
    }


    /**
     * Assigns the result of a function to each value; <tt>v[i] =
     * function(v[i])</tt>.
     *
     * @param function a function object taking as argument the current
     * association's value.
     */
    public void assign(final cern.colt.function.DoubleFunction function) {
        copy().forEachPair(new cern.colt.function.IntDoubleProcedure() {
            public boolean apply(int key, double value) {
                put(key, function.apply(value));
                return true;
            }
        });
    }

    /**
     * Clears the receiver, then adds all (key,value) pairs of
     * <tt>other</tt>values to it.
     *
     * @param other the other map to be copied into the receiver.
     */
    public void assign(AbstractIntDoubleMap other) {
        clear();
        other.forEachPair(new IntDoubleProcedure() {
            public boolean apply(int key, double value) {
                put(key, value);
                return true;
            }
        });
    }

    /**
     * Returns <tt>true</tt> if the receiver contains the specified key.
     *
     * @return <tt>true</tt> if the receiver contains the specified key.
     */
    public boolean containsKey(final int key) {
        return !forEachKey(new IntProcedure() {
            public boolean apply(int iterKey) {
                return (key != iterKey);
            }
        });
    }

    /**
     * Returns <tt>true</tt> if the receiver contains the specified value.
     *
     * @return <tt>true</tt> if the receiver contains the specified value.
     */
    public boolean containsValue(final double value) {
        return !forEachPair(new IntDoubleProcedure() {
            public boolean apply(int iterKey, double iterValue) {
                return (value != iterValue);
            }
        });
    }

    /**
     * Returns a deep copy of the receiver; uses <code>clone()</code> and casts
     * the result.
     *
     * @return a deep copy of the receiver.
     */
    public AbstractIntDoubleMap copy() {
        return (AbstractIntDoubleMap) clone();
    }

    /**
     * Compares the specified object with this map for equality.  Returns
     * <tt>true</tt> if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps <tt>m1</tt> and
     * <tt>m2</tt> represent the same mappings iff
     * <pre>
     * m1.forEachPair(
     * 		new IntDoubleProcedure() {
     * 			public boolean apply(int key, double value) {
     * 				return m2.containsKey(key) && m2.get(key) == value;
     *            }
     *        }
     * 	)
     * &&
     * m2.forEachPair(
     * 		new IntDoubleProcedure() {
     * 			public boolean apply(int key, double value) {
     * 				return m1.containsKey(key) && m1.get(key) == value;
     *            }
     *        }
     * 	);
     * </pre>
     * <p>
     * This implementation first checks if the specified object is this map; if
     * so it returns <tt>true</tt>.  Then, it checks if the specified object is
     * a map whose size is identical to the size of this set; if not, it it
     * returns <tt>false</tt>.  If so, it applies the iteration as described
     * above.
     *
     * @param obj object to be compared for equality with this map.
     * @return <tt>true</tt> if the specified object is equal to this map.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof AbstractIntDoubleMap)) return false;
        final AbstractIntDoubleMap other = (AbstractIntDoubleMap) obj;
        if (other.size() != size()) return false;

        return forEachPair(new IntDoubleProcedure() {
            public boolean apply(int key, double value) {
                return other.containsKey(key) && other.get(key) == value;
            }
        }) && other.forEachPair(new IntDoubleProcedure() {
            public boolean apply(int key, double value) {
                return containsKey(key) && get(key) == value;
            }
        });
    }

    /**
     * Applies a procedure to each key of the receiver, if any. Note: Iterates
     * over the keys in no particular order. Subclasses can define a particular
     * order, for example, "sorted by key". All methods which <i>can</i> be
     * expressed in terms of this method (most methods can) <i>must
     * guarantee</i> to use the <i>same</i> order defined by this method, even
     * if it is no particular order. This is necessary so that, for example,
     * methods <tt>keys</tt> and <tt>values</tt> will yield association pairs,
     * not two uncorrelated lists.
     *
     * @param procedure the procedure to be applied. Stops iteration if the
     * procedure returns <tt>false</tt>, otherwise continues.
     * @return <tt>false</tt> if the procedure stopped before all keys where
     * iterated over, <tt>true</tt> otherwise.
     */
    public abstract boolean forEachKey(IntProcedure procedure);

    /**
     * Applies a procedure to each (key,value) pair of the receiver, if any.
     * Iteration order is guaranteed to be <i>identical</i> to the order used by
     * method {@link #forEachKey(IntProcedure)}.
     *
     * @param procedure the procedure to be applied. Stops iteration if the
     * procedure returns <tt>false</tt>, otherwise continues.
     * @return <tt>false</tt> if the procedure stopped before all keys where
     * iterated over, <tt>true</tt> otherwise.
     */
    public boolean forEachPair(final IntDoubleProcedure procedure) {
        return forEachKey(new IntProcedure() {
            public boolean apply(int key) {
                return procedure.apply(key, get(key));
            }
        });
    }

    /**
     * Returns the value associated with the specified key. It is often a good
     * idea to first check with {@link #containsKey(int)} whether the given key
     * has a value associated or not, i.e. whether there exists an association
     * for the given key or not.
     *
     * @param key the key to be searched for.
     * @return the value associated with the specified key; <tt>0</tt> if no
     * such key is present.
     */
    public abstract double get(int key);

    /**
     * Returns the first key the given value is associated with. It is often a
     * good idea to first check with {@link #containsValue(double)} whether
     * there exists an association from a key to this value. Search order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     *
     * @param value the value to search for.
     * @return the first key for which holds <tt>get(key) == value</tt>; returns
     * <tt>Integer.MIN_VALUE</tt> if no such key exists.
     */
    public int keyOf(final double value) {
        final int[] foundKey = new int[1];
        boolean notFound = forEachPair(new IntDoubleProcedure() {
            public boolean apply(int iterKey, double iterValue) {
                boolean found = value == iterValue;
                if (found) foundKey[0] = iterKey;
                return !found;
            }
        });
        if (notFound) return Integer.MIN_VALUE;
        return foundKey[0];
    }

    /**
     * Returns a list filled with all keys contained in the receiver. The
     * returned list has a size that equals <tt>this.size()</tt>. Iteration
     * order is guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     * <p>
     * This method can be used to iterate over the keys of the receiver.
     *
     * @return the keys.
     */
    public IntArrayList keys() {
        IntArrayList list = new IntArrayList(size());
        keys(list);
        return list;
    }

    /**
     * Fills all keys contained in the receiver into the specified list. Fills
     * the list, starting at index 0. After this call returns the specified list
     * has a new size that equals <tt>this.size()</tt>. Iteration order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     * <p>
     * This method can be used to iterate over the keys of the receiver.
     *
     * @param list the list to be filled, can have any size.
     */
    public void keys(final IntArrayList list) {
        list.clear();
        forEachKey(new IntProcedure() {
            public boolean apply(int key) {
                list.add(key);
                return true;
            }
        });
    }

    /**
     * Fills all keys <i>sorted ascending by their associated value</i> into the
     * specified list. Fills into the list, starting at index 0. After this call
     * returns the specified list has a new size that equals
     * <tt>this.size()</tt>. Primary sort criterium is "value", secondary sort
     * criterium is "key". This means that if any two values are equal, the
     * smaller key comes first.
     * <p>
     * <b>Example:</b>
     * <br>
     * <tt>keys = (8,7,6), values = (1,2,2) --> keyList = (8,6,7)</tt>
     *
     * @param keyList the list to be filled, can have any size.
     */
    public void keysSortedByValue(final IntArrayList keyList) {
        pairsSortedByValue(keyList, new DoubleArrayList(size()));
    }

    /**
     * Fills all pairs satisfying a given condition into the specified lists.
     * Fills into the lists, starting at index 0. After this call returns the
     * specified lists both have a new size, the number of pairs satisfying the
     * condition. Iteration order is guaranteed to be <i>identical</i> to the
     * order used by method {@link #forEachKey(IntProcedure)}.
     * <p>
     * <b>Example:</b>
     * <br>
     * <pre>
     * IntDoubleProcedure condition = new IntDoubleProcedure() { // match even keys only
     * public boolean apply(int key, double value) { return key%2==0; }
     * }
     * keys = (8,7,6), values = (1,2,2) --> keyList = (6,8), valueList = (2,1)</tt>
     * </pre>
     *
     * @param condition the condition to be matched. Takes the current key as
     * first and the current value as second argument.
     * @param keyList the list to be filled with keys, can have any size.
     * @param valueList the list to be filled with values, can have any size.
     */
    public void pairsMatching(final IntDoubleProcedure condition, final IntArrayList keyList, final DoubleArrayList valueList) {
        keyList.clear();
        valueList.clear();

        forEachPair(new IntDoubleProcedure() {
            public boolean apply(int key, double value) {
                if (condition.apply(key, value)) {
                    keyList.add(key);
                    valueList.add(value);
                }
                return true;
            }
        });
    }

    /**
     * Fills all keys and values <i>sorted ascending by key</i> into the
     * specified lists. Fills into the lists, starting at index 0. After this
     * call returns the specified lists both have a new size that equals
     * <tt>this.size()</tt>.
     * <p>
     * <b>Example:</b>
     * <br>
     * <tt>keys = (8,7,6), values = (1,2,2) --> keyList = (6,7,8), valueList =
     * (2,2,1)</tt>
     *
     * @param keyList the list to be filled with keys, can have any size.
     * @param valueList the list to be filled with values, can have any size.
     */
    public void pairsSortedByKey(final IntArrayList keyList, final DoubleArrayList valueList) {
        keys(keyList);
        keyList.sort();
        valueList.setSize(keyList.size());
        for (int i = keyList.size(); --i >= 0; ) {
            valueList.setQuick(i, get(keyList.getQuick(i)));
        }
    }

    /**
     * Fills all keys and values <i>sorted ascending by value</i> into the
     * specified lists. Fills into the lists, starting at index 0. After this
     * call returns the specified lists both have a new size that equals
     * <tt>this.size()</tt>. Primary sort criterium is "value", secondary sort
     * criterium is "key". This means that if any two values are equal, the
     * smaller key comes first.
     * <p>
     * <b>Example:</b>
     * <br>
     * <tt>keys = (8,7,6), values = (1,2,2) --> keyList = (8,6,7), valueList =
     * (1,2,2)</tt>
     *
     * @param keyList the list to be filled with keys, can have any size.
     * @param valueList the list to be filled with values, can have any size.
     */
    public void pairsSortedByValue(final IntArrayList keyList, final DoubleArrayList valueList) {
        keys(keyList);
        values(valueList);

        final int[] k = keyList.elements();
        final double[] v = valueList.elements();
        cern.colt.Swapper swapper = new cern.colt.Swapper() {
            public void swap(int a, int b) {
                int t2;
                double t1;
                t1 = v[a];
                v[a] = v[b];
                v[b] = t1;
                t2 = k[a];
                k[a] = k[b];
                k[b] = t2;
            }
        };

        cern.colt.function.IntComparator comp = new cern.colt.function.IntComparator() {
            public int compare(int a, int b) {
                return v[a] < v[b] ? -1 : v[a] > v[b] ? 1 : (k[a] < k[b] ? -1 : (k[a] == k[b] ? 0 : 1));
            }
        };

        cern.colt.GenericSorting.quickSort(0, keyList.size(), comp, swapper);
    }

    /**
     * Associates the given key with the given value. Replaces any old
     * <tt>(key,someOtherValue)</tt> association, if existing.
     *
     * @param key the key the value shall be associated with.
     * @param value the value to be associated.
     * @return <tt>true</tt> if the receiver did not already contain such a key;
     * <tt>false</tt> if the receiver did already contain such a key - the new
     * value has now replaced the formerly associated value.
     */
    public abstract boolean put(int key, double value);

    /**
     * Removes the given key with its associated element from the receiver, if
     * present.
     *
     * @param key the key to be removed from the receiver.
     * @return <tt>true</tt> if the receiver contained the specified key,
     * <tt>false</tt> otherwise.
     */
    public abstract boolean removeKey(int key);

    /**
     * Returns a string representation of the receiver, containing the String
     * representation of each key-value pair, sorted ascending by key.
     */
    public String toString() {
        IntArrayList theKeys = keys();
        String tmp = theKeys.toString() + "\n";
        theKeys.sort();

        StringBuffer buf = new StringBuffer(tmp);
        //StringBuffer buf = new StringBuffer();
        buf.append("[");
        int maxIndex = theKeys.size() - 1;
        for (int i = 0; i <= maxIndex; i++) {
            int key = theKeys.get(i);
            buf.append(key);
            buf.append("->");
            buf.append(get(key));
            if (i < maxIndex) buf.append(", ");
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the receiver, containing the String
     * representation of each key-value pair, sorted ascending by value.
     */
    public String toStringByValue() {
        IntArrayList theKeys = new IntArrayList();
        keysSortedByValue(theKeys);

        StringBuffer buf = new StringBuffer();
        buf.append("[");
        int maxIndex = theKeys.size() - 1;
        for (int i = 0; i <= maxIndex; i++) {
            int key = theKeys.get(i);
            buf.append(key);
            buf.append("->");
            buf.append(get(key));
            if (i < maxIndex) buf.append(", ");
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a list filled with all values contained in the receiver. The
     * returned list has a size that equals <tt>this.size()</tt>. Iteration
     * order is guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     * <p>
     * This method can be used to iterate over the values of the receiver.
     *
     * @return the values.
     */
    public DoubleArrayList values() {
        DoubleArrayList list = new DoubleArrayList(size());
        values(list);
        return list;
    }

    /**
     * Fills all values contained in the receiver into the specified list. Fills
     * the list, starting at index 0. After this call returns the specified list
     * has a new size that equals <tt>this.size()</tt>. Iteration order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntProcedure)}.
     * <p>
     * This method can be used to iterate over the values of the receiver.
     *
     * @param list the list to be filled, can have any size.
     */
    public void values(final DoubleArrayList list) {
        list.clear();
        forEachKey(new IntProcedure() {
            public boolean apply(int key) {
                list.add(get(key));
                return true;
            }
        });
    }

}
