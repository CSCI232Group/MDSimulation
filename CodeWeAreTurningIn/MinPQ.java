package mdsimulation;

import java.util.*;

/*@author Jake Yakawich, Ian Hecker, Michael Hewitt, Garret Hilton 
 * MinPQ is modeled after the design implemented by: 
 * ~Robert Sedgewick~
 * ~Kevin Wayne~
 * due to it's simplicity and our desire for a high functioning 
 * priority que. This tree design allows us to have logN time 
 * for reading and writing, which is important for huge sets
 * of particle data
 */
public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;                                   // take our variables and store them
    private int n;                                      // this sets the number of items on the que
    private Comparator<Key> comparator;                 // comparator

    public MinPQ(int initCapacity)
    {
        pq = (Key[]) new Object[initCapacity + 1];
        n = 0;
    }
    public MinPQ()// intial priority Que
        {this(1);}

    public MinPQ(int initCapacity, Comparator<Key> comparator) {        //intialize by the number we hold
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        n = 0;
    }
    public MinPQ(Comparator<Key> comparator) {                          //initalize comparator
        this(1, comparator);
    }

    public MinPQ(Key[] keys) {                                          // initalize array
        n = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        for (int i = 0; i < n; i++)
            pq[i+1] = keys[i];
        for (int k = n/2; k >= 1; k--)
            sinkCollision(k);
        assert isMinHeap();
    }

    public boolean isEmpty()//check the Priority Que if empty
        {return n == 0;}

    public int size()//Returns the size on the que
        {return n;}   

    private void resizePQ(int capacity) {                                     //resize queue with capacity
        assert capacity > n;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    public void insertCollision(Key x)//insert new key
    {// double size of array if necessary
        if (n == pq.length - 1) resizePQ(2 * pq.length);        
        pq[++n] = x;
        swimCollision(n);
        assert isMinHeap();
    }

    public Key delMinCollision() 
    {//Removes and returns smallest keys
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        exch(1, n);
        Key min = pq[n--];
        sinkCollision(1);
        pq[n+1] = null;                                                             
        if ((n > 0) && (n == (pq.length - 1) / 4)) resizePQ(pq.length  / 2);
        assert isMinHeap();
        return min;
    }

    private void swimCollision(int k)
    {// moves key up the tree
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sinkCollision(int k)
    {// moves key down the tree
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j)
    {//compare two integers
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    private void exch(int i, int j)
    {//swap two keys
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }
    
    private boolean isMinHeap()
    {//  is thes the min heap
        return isMinHeap(1);
    }
    
    private boolean isMinHeap(int k)
    {//is heap rooted at k min heap
        if (k > n) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= n && greater(k, left))  return false;
        if (right <= n && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }

    public Iterator<Key> iterator() {return new HeapIterator();}

    private class HeapIterator implements Iterator<Key>
    { 
        private MinPQ<Key> copy;// add all items to copy of heap
        
        public HeapIterator()
        {
            if (comparator == null) copy = new MinPQ<Key>(size());
            else                    copy = new MinPQ<Key>(size(), comparator);
            for (int i = 1; i <= n; i++)
                copy.insertCollision(pq[i]);
        }

        public boolean hasNext()  {return !copy.isEmpty();}
        public void remove()      {throw new UnsupportedOperationException();}

        public Key next()
        {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMinCollision();
        }
    }
}