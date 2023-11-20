package io.jenetics.lattices.array.map;

import java.io.IOException;
import java.util.NoSuchElementException;

public abstract class AbstractMutableDoubleValuesMap //extends AbstractDoubleIterable implements MutableDoubleValuesMap
{
//    protected abstract int getOccupiedWithData();
//
//    protected abstract SentinelValues getSentinel();
//
//    protected abstract void setSentinelValuesNull();
//
//    protected abstract double getEmptyValue();
//
//    protected abstract double getValueAtIndex(int index);
//
//    protected abstract int getTableSize();
//
//    protected abstract boolean isNonSentinelAtIndex(int index);
//
//    protected void addEmptyKeyValue(double value)
//    {
//        this.getSentinel().containsZeroKey = true;
//        this.getSentinel().zeroValue = value;
//    }
//
//    protected void removeEmptyKey()
//    {
//        if (this.getSentinel().containsOneKey)
//        {
//            this.getSentinel().containsZeroKey = false;
//            this.getSentinel().zeroValue = this.getEmptyValue();
//        }
//        else
//        {
//            this.setSentinelValuesNull();
//        }
//    }
//
//    protected void addRemovedKeyValue(double value)
//    {
//        this.getSentinel().containsOneKey = true;
//        this.getSentinel().oneValue = value;
//    }
//
//    protected void removeRemovedKey()
//    {
//        if (this.getSentinel().containsZeroKey)
//        {
//            this.getSentinel().containsOneKey = false;
//            this.getSentinel().oneValue = this.getEmptyValue();
//        }
//        else
//        {
//            this.setSentinelValuesNull();
//        }
//    }
//
//    @Override
//    public boolean contains(double value)
//    {
//        return this.containsValue(value);
//    }
//
//    @Override
//    public boolean containsAll(DoubleIterable source)
//    {
//        return source.allSatisfy((double value) -> AbstractMutableDoubleValuesMap.this.contains(value));
//    }
//
//    @Override
//    public double max()
//    {
//        if (this.isEmpty())
//        {
//            throw new NoSuchElementException();
//        }
//        DoubleIterator iterator = this.doubleIterator();
//        double max = iterator.next();
//        while (iterator.hasNext())
//        {
//            double value = iterator.next();
//            if (Double.compare(max, value) < 0)
//            {
//                max = value;
//            }
//        }
//        return max;
//    }
//
//    @Override
//    public double min()
//    {
//        if (this.isEmpty())
//        {
//            throw new NoSuchElementException();
//        }
//        DoubleIterator iterator = this.doubleIterator();
//        double min = iterator.next();
//        while (iterator.hasNext())
//        {
//            double value = iterator.next();
//            if (Double.compare(value, min) < 0)
//            {
//                min = value;
//            }
//        }
//        return min;
//    }
//
//    @Override
//    public int size()
//    {
//        return this.getOccupiedWithData() + (this.getSentinel() == null ? 0 : this.getSentinel().size());
//    }
//
//    @Override
//    public boolean isEmpty()
//    {
//        return this.getOccupiedWithData() == 0 && (this.getSentinel() == null || this.getSentinel().size() == 0);
//    }
//
//    @Override
//    public boolean notEmpty()
//    {
//        return this.getOccupiedWithData() != 0 || (this.getSentinel() != null && this.getSentinel().size() != 0);
//    }
//
//    /**
//     * @since 7.0.
//     */
//    @Override
//    public void each(DoubleProcedure procedure)
//    {
//        this.forEachValue(procedure);
//    }
//
//    @Override
//    public void appendString(Appendable appendable, String start, String separator, String end)
//    {
//        try
//        {
//            appendable.append(start);
//
//            boolean first = true;
//
//            if (this.getSentinel() != null)
//            {
//                if (this.getSentinel().containsZeroKey)
//                {
//                    appendable.append(String.valueOf(this.getSentinel().zeroValue));
//                    first = false;
//                }
//                if (this.getSentinel().containsOneKey)
//                {
//                    if (!first)
//                    {
//                        appendable.append(separator);
//                    }
//                    appendable.append(String.valueOf(this.getSentinel().oneValue));
//                    first = false;
//                }
//            }
//            for (int i = 0; i < this.getTableSize(); i++)
//            {
//                if (this.isNonSentinelAtIndex(i))
//                {
//                    if (!first)
//                    {
//                        appendable.append(separator);
//                    }
//                    appendable.append(String.valueOf(this.getValueAtIndex(i)));
//                    first = false;
//                }
//            }
//            appendable.append(end);
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public double[] toArray()
//    {
//        double[] array = new double[this.size()];
//        int index = 0;
//
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey)
//            {
//                array[index] = this.getSentinel().zeroValue;
//                index++;
//            }
//            if (this.getSentinel().containsOneKey)
//            {
//                array[index] = this.getSentinel().oneValue;
//                index++;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i))
//            {
//                array[index] = this.getValueAtIndex(i);
//                index++;
//            }
//        }
//
//        return array;
//    }
//
//    @Override
//    public double[] toArray(double[] target)
//    {
//        if (target.length < this.size())
//        {
//            target = new double[this.size()];
//        }
//        int index = 0;
//
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey)
//            {
//                target[index] = this.getSentinel().zeroValue;
//                index++;
//            }
//            if (this.getSentinel().containsOneKey)
//            {
//                target[index] = this.getSentinel().oneValue;
//                index++;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i))
//            {
//                target[index] = this.getValueAtIndex(i);
//                index++;
//            }
//        }
//
//        return target;
//    }
//
//    @Override
//    public MutableDoubleBag select(DoublePredicate predicate)
//    {
//        return this.select(predicate, new DoubleHashBag());
//    }
//
//    @Override
//    public MutableDoubleBag reject(DoublePredicate predicate)
//    {
//        return this.reject(predicate, new DoubleHashBag());
//    }
//
//    @Override
//    public <V> MutableBag<V> collect(DoubleToObjectFunction<? extends V> function)
//    {
//        return this.collect(function, Bags.mutable.withInitialCapacity(this.size()));
//    }
//
//    @Override
//    public double detectIfNone(DoublePredicate predicate, double value)
//    {
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey && predicate.accept(this.getSentinel().zeroValue))
//            {
//                return this.getSentinel().zeroValue;
//            }
//            if (this.getSentinel().containsOneKey && predicate.accept(this.getSentinel().oneValue))
//            {
//                return this.getSentinel().oneValue;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
//            {
//                return this.getValueAtIndex(i);
//            }
//        }
//        return value;
//    }
//
//    @Override
//    public int count(DoublePredicate predicate)
//    {
//        int count = 0;
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey && predicate.accept(this.getSentinel().zeroValue))
//            {
//                count++;
//            }
//            if (this.getSentinel().containsOneKey && predicate.accept(this.getSentinel().oneValue))
//            {
//                count++;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
//            {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    @Override
//    public boolean anySatisfy(DoublePredicate predicate)
//    {
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey && predicate.accept(this.getSentinel().zeroValue))
//            {
//                return true;
//            }
//            if (this.getSentinel().containsOneKey && predicate.accept(this.getSentinel().oneValue))
//            {
//                return true;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean allSatisfy(DoublePredicate predicate)
//    {
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey && !predicate.accept(this.getSentinel().zeroValue))
//            {
//                return false;
//            }
//            if (this.getSentinel().containsOneKey && !predicate.accept(this.getSentinel().oneValue))
//            {
//                return false;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i) && !predicate.accept(this.getValueAtIndex(i)))
//            {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public RichIterable<DoubleIterable> chunk(int size)
//    {
//        if (size <= 0)
//        {
//            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
//        }
//        MutableList<DoubleIterable> result = Lists.mutable.empty();
//        if (this.notEmpty())
//        {
//            if (this.size() <= size)
//            {
//                result.add(DoubleBags.mutable.withAll(this));
//            }
//            else
//            {
//                DoubleIterator iterator = this.doubleIterator();
//                while (iterator.hasNext())
//                {
//                    MutableDoubleBag batch = DoubleBags.mutable.empty();
//                    for (int i = 0; i < size && iterator.hasNext(); i++)
//                    {
//                        batch.add(iterator.next());
//                    }
//                    result.add(batch);
//                }
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public double sum()
//    {
//        double result = 0.0;
//        double compensation = 0.0;
//
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey)
//            {
//                double adjustedValue = this.getSentinel().zeroValue - compensation;
//                double nextSum = result + adjustedValue;
//                compensation = nextSum - result - adjustedValue;
//                result = nextSum;
//            }
//            if (this.getSentinel().containsOneKey)
//            {
//                double adjustedValue = this.getSentinel().oneValue - compensation;
//                double nextSum = result + adjustedValue;
//                compensation = nextSum - result - adjustedValue;
//                result = nextSum;
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i))
//            {
//                double adjustedValue = this.getValueAtIndex(i) - compensation;
//                double nextSum = result + adjustedValue;
//                compensation = nextSum - result - adjustedValue;
//                result = nextSum;
//            }
//        }
//
//        return result;
//    }
//
//    @Override
//    public boolean containsValue(double value)
//    {
//        if (this.getSentinel() != null && this.getSentinel().containsValue(value))
//        {
//            return true;
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i) && Double.compare(this.getValueAtIndex(i), value) == 0)
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void forEachValue(DoubleProcedure procedure)
//    {
//        if (this.getSentinel() != null)
//        {
//            if (this.getSentinel().containsZeroKey)
//            {
//                procedure.value(this.getSentinel().zeroValue);
//            }
//            if (this.getSentinel().containsOneKey)
//            {
//                procedure.value(this.getSentinel().oneValue);
//            }
//        }
//        for (int i = 0; i < this.getTableSize(); i++)
//        {
//            if (this.isNonSentinelAtIndex(i))
//            {
//                procedure.value(this.getValueAtIndex(i));
//            }
//        }
//    }
//
//    protected static class SentinelValues extends AbstractSentinelValues
//    {
//        protected double zeroValue;
//        protected double oneValue;
//
//        public boolean containsValue(double value)
//        {
//            boolean valueEqualsZeroValue = this.containsZeroKey && Double.compare(this.zeroValue, value) == 0;
//            boolean valueEqualsOneValue = this.containsOneKey && Double.compare(this.oneValue, value) == 0;
//            return valueEqualsZeroValue || valueEqualsOneValue;
//        }
//
//        public SentinelValues copy()
//        {
//            SentinelValues sentinelValues = new SentinelValues();
//            sentinelValues.zeroValue = this.zeroValue;
//            sentinelValues.oneValue = this.oneValue;
//            sentinelValues.containsOneKey = this.containsOneKey;
//            sentinelValues.containsZeroKey = this.containsZeroKey;
//            return sentinelValues;
//        }
//    }
//
//    protected abstract class AbstractDoubleValuesCollection implements MutableDoubleCollection
//    {
//        @Override
//        public void clear()
//        {
//            AbstractMutableDoubleValuesMap.this.clear();
//        }
//
//        @Override
//        public MutableDoubleCollection select(DoublePredicate predicate)
//        {
//            return AbstractMutableDoubleValuesMap.this.select(predicate);
//        }
//
//        @Override
//        public MutableDoubleCollection reject(DoublePredicate predicate)
//        {
//            return AbstractMutableDoubleValuesMap.this.reject(predicate);
//        }
//
//        @Override
//        public double detectIfNone(DoublePredicate predicate, double ifNone)
//        {
//            return AbstractMutableDoubleValuesMap.this.detectIfNone(predicate, ifNone);
//        }
//
//        @Override
//        public <V> MutableCollection<V> collect(DoubleToObjectFunction<? extends V> function)
//        {
//            return AbstractMutableDoubleValuesMap.this.collect(function);
//        }
//
//        @Override
//        public <T> T injectInto(T injectedValue, ObjectDoubleToObjectFunction<? super T, ? extends T> function)
//        {
//            return AbstractMutableDoubleValuesMap.this.injectInto(injectedValue, function);
//        }
//
//        @Override
//        public RichIterable<DoubleIterable> chunk(int size)
//        {
//            return AbstractMutableDoubleValuesMap.this.chunk(size);
//        }
//
//        @Override
//        public double sum()
//        {
//            return AbstractMutableDoubleValuesMap.this.sum();
//        }
//
//        @Override
//        public double max()
//        {
//            return AbstractMutableDoubleValuesMap.this.max();
//        }
//
//        @Override
//        public double maxIfEmpty(double defaultValue)
//        {
//            return AbstractMutableDoubleValuesMap.this.maxIfEmpty(defaultValue);
//        }
//
//        @Override
//        public double min()
//        {
//            return AbstractMutableDoubleValuesMap.this.min();
//        }
//
//        @Override
//        public double minIfEmpty(double defaultValue)
//        {
//            return AbstractMutableDoubleValuesMap.this.minIfEmpty(defaultValue);
//        }
//
//        @Override
//        public double average()
//        {
//            return AbstractMutableDoubleValuesMap.this.average();
//        }
//
//        @Override
//        public double median()
//        {
//            return AbstractMutableDoubleValuesMap.this.median();
//        }
//
//        @Override
//        public double[] toSortedArray()
//        {
//            return AbstractMutableDoubleValuesMap.this.toSortedArray();
//        }
//
//        @Override
//        public MutableDoubleList toSortedList()
//        {
//            return AbstractMutableDoubleValuesMap.this.toSortedList();
//        }
//
//        @Override
//        public MutableDoubleCollection with(double element)
//        {
//            throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public MutableDoubleCollection without(double element)
//        {
//            throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public MutableDoubleCollection withAll(DoubleIterable elements)
//        {
//            throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public MutableDoubleCollection withoutAll(DoubleIterable elements)
//        {
//            throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public MutableDoubleCollection asUnmodifiable()
//        {
//            return UnmodifiableDoubleCollection.of(this);
//        }
//
//        @Override
//        public MutableDoubleCollection asSynchronized()
//        {
//            return SynchronizedDoubleCollection.of(this);
//        }
//
//        @Override
//        public ImmutableDoubleCollection toImmutable()
//        {
//            return DoubleLists.immutable.withAll(this);
//        }
//
//        @Override
//        public boolean contains(double value)
//        {
//            return AbstractMutableDoubleValuesMap.this.containsValue(value);
//        }
//
//        @Override
//        public boolean containsAll(double... source)
//        {
//            return AbstractMutableDoubleValuesMap.this.containsAll(source);
//        }
//
//        @Override
//        public boolean containsAll(DoubleIterable source)
//        {
//            return AbstractMutableDoubleValuesMap.this.containsAll(source);
//        }
//
//        @Override
//        public MutableDoubleList toList()
//        {
//            return AbstractMutableDoubleValuesMap.this.toList();
//        }
//
//        @Override
//        public MutableDoubleSet toSet()
//        {
//            return AbstractMutableDoubleValuesMap.this.toSet();
//        }
//
//        @Override
//        public MutableDoubleBag toBag()
//        {
//            return AbstractMutableDoubleValuesMap.this.toBag();
//        }
//
//        @Override
//        public LazyDoubleIterable asLazy()
//        {
//            return new LazyDoubleIterableAdapter(this);
//        }
//
//        @Override
//        public boolean isEmpty()
//        {
//            return AbstractMutableDoubleValuesMap.this.isEmpty();
//        }
//
//        @Override
//        public boolean notEmpty()
//        {
//            return AbstractMutableDoubleValuesMap.this.notEmpty();
//        }
//
//        @Override
//        public String makeString()
//        {
//            return AbstractMutableDoubleValuesMap.this.makeString();
//        }
//
//        @Override
//        public String makeString(String separator)
//        {
//            return AbstractMutableDoubleValuesMap.this.makeString(separator);
//        }
//
//        @Override
//        public String makeString(String start, String separator, String end)
//        {
//            return AbstractMutableDoubleValuesMap.this.makeString(start, separator, end);
//        }
//
//        @Override
//        public void appendString(Appendable appendable)
//        {
//            AbstractMutableDoubleValuesMap.this.appendString(appendable);
//        }
//
//        @Override
//        public void appendString(Appendable appendable, String separator)
//        {
//            AbstractMutableDoubleValuesMap.this.appendString(appendable, separator);
//        }
//
//        @Override
//        public void appendString(Appendable appendable, String start, String separator, String end)
//        {
//            AbstractMutableDoubleValuesMap.this.appendString(appendable, start, separator, end);
//        }
//
//        @Override
//        public void each(DoubleProcedure procedure)
//        {
//            AbstractMutableDoubleValuesMap.this.each(procedure);
//        }
//
//        @Override
//        public int count(DoublePredicate predicate)
//        {
//            return AbstractMutableDoubleValuesMap.this.count(predicate);
//        }
//
//        @Override
//        public boolean anySatisfy(DoublePredicate predicate)
//        {
//            return AbstractMutableDoubleValuesMap.this.anySatisfy(predicate);
//        }
//
//        @Override
//        public boolean allSatisfy(DoublePredicate predicate)
//        {
//            return AbstractMutableDoubleValuesMap.this.allSatisfy(predicate);
//        }
//
//        @Override
//        public boolean noneSatisfy(DoublePredicate predicate)
//        {
//            return AbstractMutableDoubleValuesMap.this.noneSatisfy(predicate);
//        }
//
//        @Override
//        public boolean add(double element)
//        {
//            throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public boolean addAll(double... source)
//        {
//            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public boolean addAll(DoubleIterable source)
//        {
//            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
//        }
//
//        @Override
//        public boolean removeAll(DoubleIterable source)
//        {
//            int oldSize = AbstractMutableDoubleValuesMap.this.size();
//
//            DoubleIterator iterator = source.doubleIterator();
//            while (iterator.hasNext())
//            {
//                this.remove(iterator.next());
//            }
//            return oldSize != AbstractMutableDoubleValuesMap.this.size();
//        }
//
//        @Override
//        public boolean removeAll(double... source)
//        {
//            int oldSize = AbstractMutableDoubleValuesMap.this.size();
//
//            for (double item : source)
//            {
//                this.remove(item);
//            }
//            return oldSize != AbstractMutableDoubleValuesMap.this.size();
//        }
//
//        @Override
//        public boolean retainAll(double... source)
//        {
//            return this.retainAll(DoubleHashSet.newSetWith(source));
//        }
//
//        @Override
//        public int size()
//        {
//            return AbstractMutableDoubleValuesMap.this.size();
//        }
//
//        @Override
//        public double[] toArray()
//        {
//            return AbstractMutableDoubleValuesMap.this.toArray();
//        }
//
//        @Override
//        public double[] toArray(double[] target)
//        {
//            return AbstractMutableDoubleValuesMap.this.toArray(target);
//        }
//    }
}
