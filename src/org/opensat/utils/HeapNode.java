/*
 * @(#)HeapNode.java	1.0 96/04/14 Walter Korman
 */

package org.opensat.utils;

/**
 * HeapNode class, stores data and children for the Heap.
 * Offers construction, printing, and insertion/removal facilities.
 * This class is private, and only meant for use by the Heap
 * class.
 *
 * @version	1.0, 04/14/96
 * @author	Walter Korman
 * @see Heap
 */
class HeapNode {
    HeapNode	left, right;  /* children */
    HeapItem	item;         /* data */

    /**
     * Constructor, initializes member data.
     */
    HeapNode() {
	left = null;
	right = null;
	item = null;
    }

    /**
     * Constructor, allows setting data.
     * @param HeapItem item The data item
     */
    HeapNode(HeapItem item) {
	left = right = null;
	this.item = item;
    }

    /**
     * Constructor, allows setting children and data.
     * @param HeapNode l Left child
     * @param HeapNode r Right child
     * @param HeapItem item The data item
     */
    HeapNode(HeapNode l, HeapNode r, HeapItem item) {
	left = l;
	right = r;
	this.item = item;
    }

    /**
     * Print this node and its children in pre-order fashion.
     */
    public void print() {
	if(left != null)
	    left.print();

	if(item != null)
	    item.print();

	if(right != null)
	    right.print();
    }
}

