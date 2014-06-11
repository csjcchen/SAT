package org.opensat.data;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

/**
 * A Double Ended QUEeue of IClause
 * 
 * @author roussel
 *
 */
public class DequeOfIClause {

    /**
     * Initialize the deque with a maximum number of elements.
     * 
     * @param size maximum number of items this deque can contain
     */
    public DequeOfIClause(int size) {
        tab = new IClause[size+1];
        clear();
    }

    /**
     * Empties the deque.
     */
    public void clear() {
        front = 0;
        back = 0;
        // FIXME (OR): should we take care of deallocation ?
        // that is to say setting to null every cell of the array
    }

    /**
     * Add an item at the beginning of the queue.
     * 
     * @param c element to push at the front of the deque (queue point of view)
     */
    public void uncheckedPushFront(IClause c) {
        tab[front--] = c;
        if (front < 0)
            front = tab.length - 1;

        assert back + 1 != front
            && (front != 0 || back != tab.length - 1) : "Deque is full !";
    }

    /**
     * Add an item at the end of the queue (that is the top of the stack).
     * 
     * @param c element to push at the end of the deque (stack point of view)
     */
    public void uncheckedPushBack(IClause c) {
        back++;
        if (back >= tab.length)
            back = 0;
        tab[back] = c;

        assert back + 1 != front
            && (front != 0 || back != tab.length - 1) : "Deque is full !";
    }

    /**
     * Add an item at the beginning of the queue.
     *
     * @param c element to push at the front of the deque (queue point of view)
     * @throws BufferOverflowException if deque is full
     */
    public void pushFront(IClause c) {
        tab[front--] = c;
        if (front < 0)
            front = tab.length - 1;

        if (back + 1 == front || (front == 0 && back == tab.length - 1))
            throw new BufferOverflowException();
    }

    /**
     * Add an item at the end of the queue (that is the top of the stack).
     *
     * @param c element to push at the end of the deque (stack point of view)
     * @throws BufferOverflowException if deque is full
     */ 
    public void pushBack(IClause c) {
        back++;
        if (back >= tab.length)
            back = 0;
        tab[back] = c;

        if (back + 1 == front || (front == 0 && back == tab.length - 1))
            throw new BufferOverflowException();
    }

    /**
     * Remove the element at the beginning of the queue and return it.
     * 
     * @return the element at the beginning of the queue
     */
    public IClause uncheckedPopFront() {
        assert back!=front : "Deque is empty";

        front++;
        if (front >= tab.length)
            front = 0;
        return tab[front];
    }

    /**
      * Remove the element at the end of the queue (top of the stack) and return it.
      *
      * @return IClause the element at the beginning of the queue
      */
   public IClause uncheckedPopBack() {
   		assert back!=front : "Deque is empty";

        IClause c = tab[back--];
        if (back < 0)
            back = tab.length - 1;
        return c;
    }

     /**
      * Remove the element at the beginning of the queue and return it.
      *
      * @return the element at the beginning of the queue
      * @throws BufferUnderflowException if the deque is empty
      */
     public IClause popFront() {
         if (back==front)
         	throw new BufferUnderflowException();

         front++;
         if (front >= tab.length)
             front = 0;
         return tab[front];
     }

     /**
       * Remove the element at the end of the queue (top of the stack) and return it.
       *
       * @return IClause the element at the end of the queue
       * @throws BufferUnderflowException if the deque is empty
       */
    public IClause popBack() {
        if (back==front)
           throw new BufferUnderflowException();

         IClause c = tab[back--];
         if (back < 0)
             back = tab.length - 1;
         return c;
     }

    /**
     * Get the last element of the queue.
     * 
     * @return the element at the end of the queue
     */
    public IClause uncheckedBack() {
        assert back!=front : "Deque is empty";

        return tab[back];
    }

    /**
     * Get the first element of the queue.
     * 
     * @return the element at the beginning of the queue
     */
    public IClause uncheckedFront() {
        assert back!=front : "Deque is empty";

        int i = front - 1;
        if (i < 0)
            i = tab.length - 1;
        return tab[i];
    }

    /**
     * Get the last element of the queue.
     *
     * @return the element at the end of the queue
     * @throws BufferUnderflowException if the deque is empty
     */
    public IClause back() {
        if (back==front)
           throw new BufferUnderflowException();

        return tab[back];
    }

    /**
     * Get the first element of the queue.
     *
     * @return the element at the beginning of the queue
     * @throws BufferUnderflowException if the deque is empty
     */
    public IClause front() {
        if (back==front)
           throw new BufferUnderflowException();

        int i = front - 1;
        if (i < 0)
            i = tab.length - 1;
        return tab[i];
    }

    /**
     * Tell if the deque if empty.
     * 
     * @return true iff the deque contains no element
     */
    public boolean isEmpty() {
        return front == back;
    }

    private IClause[] tab;
    // array of elements : valid elements are in ]front..back]
    private int front, back; // front and back of the deque
    // deque is empty iff front==back
}
