package io.jenetics.lattices.array;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IntDoubleHashMap {

//    static final class Node {
//        final int hash;
//        final int key;
//        double value;
//        Node next;
//
//        Node(int hash, int key, double value, Node next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        public int hashCode() {
//            return key^Double.hashCode(value);
//        }
//
//        public boolean equals(Object o) {
//            return o instanceof Node node &&
//                key == node.key &&
//                Double.compare(value, node.value) == 0;
//        }
//    }
//
//    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
//    static final int MAXIMUM_CAPACITY = 1 << 30;
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//    static final int TREEIFY_THRESHOLD = 8;
//    static final int UNTREEIFY_THRESHOLD = 6;
//    static final int MIN_TREEIFY_CAPACITY = 64;
//
//    Node[] table;
//
//    int size;
//    int modCount;
//    int threshold;
//
//    final float loadFactor = DEFAULT_LOAD_FACTOR;
//
//    final double putVal(int hash, int key, double value, boolean onlyIfAbsent,
//                   boolean evict)
//    {
//        Node[] tab;
//        Node p; int n, i;
//        if ((tab = table) == null || (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((p = tab[i = (n - 1) & hash]) == null)
//            tab[i] = newNode(hash, key, value, null);
//        else {
//            Node e; int k;
//            if (p.hash == hash &&
//                ((k = p.key) == key || (key == k))) {
//                e = p;
//            } else {
//                for (int binCount = 0; ; ++binCount) {
//                    if ((e = p.next) == null) {
//                        p.next = newNode(hash, key, value, null);
//                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                            treeifyBin(tab, hash);
//                        break;
//                    }
//                    if (e.hash == hash &&
//                        ((k = e.key) == key || (key == k)))
//                        break;
//                    p = e;
//                }
//            }
//            if (e != null) { // existing mapping for key
//                double oldValue = e.value;
//                if (!onlyIfAbsent || oldValue == 0)
//                    e.value = value;
//                return oldValue;
//            }
//        }
//        ++modCount;
//        if (++size > threshold)
//            resize();
//
//        return 0;
//    }
//
//    final void treeifyBin(Node[] tab, int hash) {
//        int n, index; Node e;
//        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
//            resize();
//        else if ((e = tab[index = (n - 1) & hash]) != null) {
//            HashMap.TreeNode<K,V> hd = null, tl = null;
//            do {
//                HashMap.TreeNode<K,V> p = replacementTreeNode(e, null);
//                if (tl == null)
//                    hd = p;
//                else {
//                    p.prev = tl;
//                    tl.next = p;
//                }
//                tl = p;
//            } while ((e = e.next) != null);
//            if ((tab[index] = hd) != null)
//                hd.treeify(tab);
//        }
//    }
//
//    final Node[] resize() {
//        Node[] oldTab = table;
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        int oldThr = threshold;
//        int newCap, newThr = 0;
//        if (oldCap > 0) {
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                threshold = Integer.MAX_VALUE;
//                return oldTab;
//            }
//            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                oldCap >= DEFAULT_INITIAL_CAPACITY)
//                newThr = oldThr << 1; // double threshold
//        }
//        else if (oldThr > 0) // initial capacity was placed in threshold
//            newCap = oldThr;
//        else {               // zero initial threshold signifies using defaults
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//        if (newThr == 0) {
//            float ft = (float)newCap * loadFactor;
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
//                (int)ft : Integer.MAX_VALUE);
//        }
//        threshold = newThr;
//        Node[] newTab = new Node[newCap];
//        table = newTab;
//        if (oldTab != null) {
//            for (int j = 0; j < oldCap; ++j) {
//                Node e;
//                if ((e = oldTab[j]) != null) {
//                    oldTab[j] = null;
//                    if (e.next == null) {
//                        newTab[e.hash & (newCap - 1)] = e;
//                    } else { // preserve order
//                        Node loHead = null, loTail = null;
//                        Node hiHead = null, hiTail = null;
//                        Node next;
//                        do {
//                            next = e.next;
//                            if ((e.hash & oldCap) == 0) {
//                                if (loTail == null)
//                                    loHead = e;
//                                else
//                                    loTail.next = e;
//                                loTail = e;
//                            }
//                            else {
//                                if (hiTail == null)
//                                    hiHead = e;
//                                else
//                                    hiTail.next = e;
//                                hiTail = e;
//                            }
//                        } while ((e = next) != null);
//                        if (loTail != null) {
//                            loTail.next = null;
//                            newTab[j] = loHead;
//                        }
//                        if (hiTail != null) {
//                            hiTail.next = null;
//                            newTab[j + oldCap] = hiHead;
//                        }
//                    }
//                }
//            }
//        }
//        return newTab;
//    }
//
//    Node newNode(int hash, int key, double value, Node next) {
//        return new Node(hash, key, value, next);
//    }

}
