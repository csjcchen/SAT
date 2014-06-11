/*
 * @(#)Heap.java	1.0 96/04/14 Walter Korman
 */

package org.opensat.utils;

/**
 * Heap class, maintains a self-adjusting Heap/Priority Queue of
 * HeapNodes containing HeapItems.  The least-weighted node is always 
 * at the top of the heap.  Based on the self-adjusting heap described in
 * Tarjan & Sleator's "Self-Adjusting Binary Trees", (c) 1983,
 * published by the ACM.
 * @version	1.0, 04/14/96
 * @author	Walter Korman
 */
public class Heap {
    HeapNode root;    /* root node */

    /**
     * Construct default heap.
     */
    public Heap() {
	root = null;
    }

    /**
     * Returns the minimum item in the Heap.
     * @exception EmptyHeapException If the heap is empty.
     */
    public HeapItem findMin() {
	if(root == null)
	    throw new EmptyHeapException();

	return root.item;
    }

    /**
     * Inserts an item into the heap.
     * @param item The item to be inserted.
     */
    public boolean insert(HeapItem item) {
	HeapNode	n = new HeapNode(null, null, item);

	root = imeld(n, root);
		
	return true;
    }

    /**
     * Deletes the minimum item in the stack.
     * @exception EmptyHeapException If the heap is empty.
     */
    public HeapItem deleteMin() {
	HeapNode	n = null;

	if(root == null)
	    throw new EmptyHeapException();

	n = root;
	root = imeld(root.left, root.right);

	return n.item;
    }

    /**
     * Melds two heaps iteratively.  Significantly faster than recursion.
     * @param HeapNode h1 first heap
     * @param HeapNode h2 second heap
     */
    public HeapNode imeld(HeapNode h1, HeapNode h2) {
	HeapNode x, y, temp;

	if(h1 == null)
	    return h2;
		
	if(h2 == null)
	    return h1;

	if(h1.item.greaterThan(h2.item)) {
	    temp = h1;
	    h1 = h2;
	    h2 = temp;
	}

	x = h1;
	y = h1;
	h1 = h1.right;
	y.right = y.left;

	while(h1 != null) {
	    if(h1.item.greaterThan(h2.item)) {
		temp = h1;
		h1 = h2;
		h2 = temp;
	    }
			
	    y.left = h1;
	    y = h1;
	    h1 = y.right;
	    y.right = y.left;
	}
	y.left = h2;

	return x;
    }

    /**
     * Print the contents of the heap in preorder fashion.
     */
    public void print() {
	if(root != null)
	    root.print();
	else
	    System.out.println("<empty>");
    }

    /**
     * Returns true if the heap is empty.
     */
    public boolean empty() {
	return(root == null);
    }
}
