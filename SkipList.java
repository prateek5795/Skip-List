package sxg175130;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Skip List class Extends the basic functionalities of LinkedList
 * with insertion, searching and deletion operation at O(log N)
 * but the elements are stored in sorted order
 *
 * @author Sivagurunathanvelayutham
 * @author Sai Spandan
 * @author Prateek
 * @see Entry which provides the basic skeletion for Skip List similar
 * to LinkedList Entry Class
 */

public class SkipList<T extends Comparable<? super T>> {
    static final int PossibleLevels = 33;

    /**
     * Entry representing the Linked List contains
     * next[] representing the pointer for max level in the list
     * width[] representing the number of elements between two nodes
     */

    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        int level;
        int[] width;

        public Entry(E x, int lev) {
            element = x;
            next = (Entry[]) Array.newInstance(Entry.class, lev);
            width = new int[lev];
            level = lev;
            // add more code if needed
        }

        public E getElement() {
            return element;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry o = (Entry) obj;
                if (this.element != null && o.element != null)
                    return this.element.equals(o.element);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this);
        }

        @Override
        public String toString() {
            return element == null ? "null" : this.element.toString();
        }
    }


    public class SkipListStack {
        private Entry entry;
        private int totalWidth;

        public SkipListStack(Entry entry, int totalWidth) {
            this.entry = entry;
            this.totalWidth = totalWidth;
        }

        @Override
        public String toString() {
            return entry.toString() + ", " + totalWidth;
        }
    }

    //SkipList Class
    private Entry head, tail, lastNode; // dummy nodes
    private int size, maxLevel;
    private Random random;
    private SkipListStack[] prev;

    public SkipList() {
        head = new Entry<>(null, PossibleLevels + 2);
        tail = new Entry<>(null, PossibleLevels + 2);
        size = 0;
        maxLevel = 1;
        initializeSentinels();
        random = new Random();
    }

    /**
     * Adding element to skip list
     *
     * @param x Add x to list.
     * @return If x already exists, return false else true
     */
    public boolean add(T x) {

        if (x == null)
            return false;

        if (contains(x))
            return false;

        int level = Math.min(chooseLevel(), PossibleLevels);
        maxLevel = Math.max(maxLevel, level);

        Entry newNode = addElementsWithLevel(x, level, prev);

        // TODO update prev
        if (newNode.next[0].element != null) {
            newNode.next[0].prev = newNode;
        }

        if (newNode.next[0].element == null)
            this.lastNode = newNode;

        size++;
        return true;
    }

    private void addElementsWithLevel(T x, int level) {
        find(x);
        addElementsWithLevel(x, level, prev);
    }

    private Entry addElementsWithLevel(T x, int level, SkipListStack[] prev) {
        Entry newNode = new Entry<>(x, level + 1);

        int dist = 0;

        for (int i = 0; i <= PossibleLevels; i++) {
            Entry prevNode = i < prev.length ? prev[i].entry : head;
            if (i <= level) {
                newNode.next[i] = prevNode.next[i];
                prevNode.next[i] = newNode;
                // update distance

                newNode.width[i] = Math.max(prevNode.width[i] - dist, 1);
                prevNode.width[i] = dist + 1;

                dist += prev[i].totalWidth;
            } else {
                prevNode.width[i]++;
            }
        }
        return newNode;
    }

    /**
     * Calculate the distance between two nodes
     *
     * @param a representing the source node
     * @param b representing the destination node
     * @return distance between source and destination node
     */

    private int calcDist(Entry<T> a, Entry<T> b) {
        if (a == b || a == null || b == null) {
            return 0;
        }
        Entry<T> curr = a;
        int dist = 0;
        while (curr != b) {
            for (int i = 0; i < maxLevel; i++) {
                if (!curr.next[i].equals(tail) && b.element.compareTo((T) curr.next[i].element) >= 0) {
                    dist += curr.width[i];
                    curr = curr.next[i];
                    break;
                }
            }
        }
        return dist;
    }

    /**
     * Method to randomly choose the level
     *
     * @return random level as int
     */

    public int chooseLevel() {
        int lev = 1 + Integer.numberOfTrailingZeros(random.nextInt());
        return Math.min(lev, maxLevel + 1);
    }

    /**
     * Find smallest element that is greater or equal to x
     *
     * @param x
     * @return ceiling of the number
     */

    public T ceiling(T x) {
        if (contains(x)) {
            return x;
        }
        Entry next = prev[0].entry;
        if (next == null || next.next[0] == null)
            return null;
        return (T) next.next[0].element;
    }

    /**
     * Find the element present in the skip list or not
     *
     * @param x element to be search in the list
     *          updates the next[] array which can be used to track of the height of the skip list
     */

    public void find(T x) {
        prev = (SkipListStack[])
                Array.newInstance(SkipListStack.class, Math.min(maxLevel + 2, PossibleLevels+1));
        Entry p = head;
        for (int i = Math.min(maxLevel + 1, PossibleLevels); i >= 0; i--) {
            int hops = 0;
            while (p.next[i].element != null && x.compareTo((T) p.next[i].element) > 0) {
                hops += p.width[i];
                p = p.next[i];
            }
            prev[i] = new SkipListStack(p, hops);
        }
    }

    /**
     * Does the list contains x
     *
     * @param x element to check
     * @return true if the element present else false
     */

    public boolean contains(T x) {
        find(x);
        Entry target = prev[0].entry;
        return target.next[0].element != null && x.compareTo((T) target.next[0].element) == 0;
    }

    /**
     * Return the first element in the list
     *
     * @return first in the skip list
     */

    public T first() {
        return (T) head.next[0].element;
    }

    /**
     * largest element that is less than or equal to x
     *
     * @param x element to floor
     * @return floor value of x
     */


    public T floor(T x) {
        if (contains(x)) {
            return x;
        }
        Entry node = prev[0].entry;
        return (T) node.element;
    }

    /**
     * Return element at index n of list.  First element is at index 0.
     *
     * @param n index of list
     * @return element at index n in the list
     **/

    public T get(int n) {
        if (n < 0 || n >= size())
            return null;
//        return getLinear(n);
        return getLog(n);
    }

    /**
     * Finding the nth element in O(N) time
     *
     * @param n index
     * @return element at nth index
     */

    public T getLinear(int n) {
        SkipListIterator<T> iterator = new SkipListIterator<>(0);
        int counter = -1;
        T element = null;
        while (counter++ < n && iterator.hasNext()) {
            element = iterator.next();
        }
        return element;
    }

    /**
     * Finding the nth element in O(log N)
     *
     * @param n index
     * @return element at nth index
     */

    public T getLog(int n) {
        int currSpan = 0;
        Entry node = head;
        for (int i = maxLevel; i >= 0; i--) {
            while (node.next[i] != null && currSpan + node.width[i] <= n) {
                currSpan += node.width[i];
                node = node.next[i];
            }
        }
        return (T) node.next[0].element;
    }

    /**
     * check if the skip list is empty or not
     *
     * @return true if the skip list is empty else false
     */

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Iterate through the elements of list in sorted order
     */

    public Iterator<T> iterator() {
        return new SkipListIterator<>(0);
    }

    /**
     * @return last element in the list
     */

    public T last() {
        return (T) this.lastNode.element;
    }

    /**
     * Reorganize the elements of the list into a perfect skip list
     */

    public void rebuild() throws CloneNotSupportedException {
        Entry[] copy = (Entry[]) clone();
        this.maxLevel = (int) Math.pow(2, Math.ceil(Math.log(this.size()) / Math.log(2)) - 1);
        int nextPower = (int) Math.pow(2, Math.ceil(Math.log(this.size()) / Math.log(2)) );
        initializeSentinels();
        rebuildWithLevels(copy, 0, nextPower, maxLevel);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return cloneSkipHeader();
    }

    /**
     * Divide and conquer
     */
    private void rebuildWithLevels(Entry[] skipArr, int start, int end, int level) {

        if (start > end)
            return;


        int mid = (end - start) / 2 + start;

        if (mid < skipArr.length)

            this.addElementsWithLevel((T) skipArr[mid].element, level);

        // recurse both sub array

        rebuildWithLevels(skipArr, start, mid - 1, level - 1);

        if (mid < skipArr.length) // check mid is less than total elements, for right recurse

            rebuildWithLevels(skipArr, mid + 1, end, level - 1);
    }


    private Entry[] cloneSkipHeader() {
        Entry[] copy = (Entry[]) Array.newInstance(Entry.class, this.size());
        Entry tempHead = this.head.next[0];
        int i = 0;
        while (tempHead.element != null) {
            copy[i++] = tempHead;
            tempHead = tempHead.next[0]; // move to next level
        }
        return copy;
    }

    private void initializeSentinels() {
        for (int i = 0; i <= PossibleLevels; i++) {
            this.head.next[i] = tail;
            this.head.width[i] = 1;
            this.tail.next[i] = null;
        }
    }


    /**
     * Remove x from list.
     *
     * @return Removed element is returned. Return null if x not in list
     */

    public T remove(T x) {
        if (!contains(x))
            return null;

        Entry ent = prev[0].entry.next[0];
        // last before
        if (ent.next[0].element == null) {
            this.lastNode = prev[0].entry;
        }


        for (int i = 0; i <= PossibleLevels; i++) {
            Entry prevNode = i < prev.length ? prev[i].entry : head;
            if (prevNode.next[i].element != null && prevNode.next[i].element.equals(x)) {
                prevNode.width[i] += ent.width[i] - 1;
                prevNode.next[i] = ent.next[i]; //bypass
            } else {
                prevNode.width[i]--;
            }

        }

        size = size - 1;
        return (T) ent.element;
    }

    public void printList() {
        Entry node = head.next[0];
        System.out.println("----------START----------");
        while (node != null && node.element != null) {
            for (int i = 0; i < node.level; i++) {
                System.out.print(node.element + "\t");
            }
            for (int j = node.level; j < maxLevel; j++) {
                System.out.print("|\t");
            }
            System.out.println();
            node = node.next[0];
        }
        System.out.println("----------END----------");
    }
    /**
     * @return size of the skip list
     */

    public int size() {
        return size;
    }

    /**
     * Iterator for skip list
     */

    private class SkipListIterator<T> implements Iterator<T> {
        //
        Entry it;
        int level;

        SkipListIterator(int level) {
            this.level = level;
            it = head.next[level];
        }

        @Override
        public boolean hasNext() {
            if (it != null && it.element != null) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            Entry<T> node = null;
            if (hasNext()) {
                node = it;
                it = it.next[level];
            }
            return node == null ? null : node.element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }
    }
}