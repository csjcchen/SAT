/*
 * @(#)HeapItem.java	1.0 96/04/14 Walter Korman
 */

package org.opensat.utils;

/**
 * HeapItem interface.  Must be implemented by any class which will
 * be stored in the Heap class.  The print() method isn't really
 * necessary, but can be useful for debugging/perusal of your
 * data structures.
 *
 * @version	1.0, 04/14/96
 * @author	Walter Korman
 * @see Heap
 */
public interface HeapItem {

    /**
     * Returns whether this item's value is greater than the passed item.
     * @param HeapItem item The item to compare this item to.
     */
    public boolean greaterThan(HeapItem item);

    /**
     * Prints this item.
     */
    public void print();
}

