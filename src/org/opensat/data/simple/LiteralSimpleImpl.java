/*
 * The OpenSAT project
 * Copyright (c) 2002, Joao Marques-Silva and Daniel Le Berre
 * 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Created on 26 nov. 2002
 * 
 */
package org.opensat.data.simple;

import java.util.ArrayList;
import java.util.Iterator;

import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.utils.HeapItem;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class LiteralSimpleImpl implements ILiteral, Comparable, HeapItem {

    private final ArrayList clauses;
    private int decisionLevel = -1;
    private final int id;
    private LiteralSimpleImpl opposite;
    private IClause reason = null;
    private boolean relevance = false;

    private int totalnumberofOccurrences = 0;

    private int value;
    private long weight = 0;

    /**
     * This is the normal constructor for a literal.
     * 
     * @param id
     */
    protected LiteralSimpleImpl(int id) {
        this.id = id;
        this.opposite = null;
        clauses = new ArrayList();
    }

    /**
     * This constructor is to be used only to create the opposite literal.
     * 
     * @param opposite
     */
    protected LiteralSimpleImpl(LiteralSimpleImpl opposite) {
        id = -opposite.id;
        this.opposite = opposite;
        opposite.opposite = this;
        clauses = new ArrayList();
    }

    /**
     * @see org.opensat.ILiteral#clauseIterator()
     */
    public Iterator clauseIterator() {
        return clauses.iterator();
    }

    /**
    
    * @see java.lang.Comparable#compareTo(java.lang.Object)	
    */
    public int compareTo(Object o) {
        if (o instanceof LiteralSimpleImpl) {
            return Math.abs(id) - Math.abs(((LiteralSimpleImpl) o).id);
        }
        return 0;
    }
    /**
     * @see org.opensat.ILiteral#falsify()
     */
    public void falsify() {
        value = FALSIFIED;
        opposite.value = SATISFIED;
    }

    /**
    * @see org.opensat.data.ILiteral#getDecisionLevel()
    */
    public int getDecisionLevel() {
        return decisionLevel;
    }
    /**
    * @see org.opensat.ILiteral#getId()
    */
    public int getId() {
        return id;
    }

    public IClause getReason() {
        return reason;
    }

    /**
    * @see org.opensat.data.ILiteral#getWeight()
    */
    public long getWeight() {
        return weight;
    }

    /**
     * @see org.opensat.ILiteral#hide(IClause)
     */
    public void hide(IClause cl) {
        assert clauses.contains(cl);
        clauses.remove(cl);
    }

    /**
    * @see org.opensat.ILiteral#isFalsified()
    */
    public boolean isFalsified() {
        return value == FALSIFIED;
    }
    
    /**
    * @see org.opensat.ILiteral#isRelevant()
    */
    public boolean isRelevant() {
        return relevance;
    }

    /**
     * @see org.opensat.ILiteral#isSatisfied()
     */
    public boolean isSatisfied() {
        return value == SATISFIED;
    }
    /**
    	* @see org.opensat.ILiteral#isUnassigned()
    	*/
    public boolean isUnassigned() {
        return value == UNASSIGNED;
    }

    public int occurrences() {
        int counter = 0;
        Iterator it = clauses.iterator();
        while (it.hasNext()) {
            IClause cl = (IClause) it.next();
            if (!cl.isSatisfied()) {
                assert !cl.isFalsified();
                counter++;
            }
        }
        return counter;
    }

    /**
     * @see org.opensat.ILiteral#opposite()
     */
    public ILiteral opposite() {
        return opposite;
    }

    /**
     * @see org.opensat.data.ILiteral#register(org.opensat.data.IClause)
     */
    public void register(IClause cl) {
        totalnumberofOccurrences++;
    }

    /**
     * @see org.opensat.ILiteral#satisfy()
     */
    public void satisfy() {
        value = SATISFIED;
        opposite.value = FALSIFIED;
    }

    /**
    * @see org.opensat.data.ILiteral#setDecisionLevel(int)
    */
    public void setDecisionLevel(int i) {
        decisionLevel = i;
        opposite.decisionLevel = i;
    }

    public void setReason(IClause cl) {
        reason = cl;
    }

    /**
    															 * @see org.opensat.ILiteral#setRelevance(boolean)
    															 */
    public void setRelevance(boolean b) {
        relevance = b;
        opposite.relevance = b;
    }
    /**
    																			 * @see org.opensat.data.ILiteral#setWeight(long)
    																			 */
    public void setWeight(long w) {
        weight = w;
    }

    public String toString() {
        if (isUnassigned()) {
            return Integer.toString(id) + "|" + this.value;
        } else {
            return "(" + id + "|" + this.value +  "@" + decisionLevel + ") ";
        }
    }

    /**
    * @see org.opensat.data.ILiteral#totalOccurrences()
    */
    public int totalOccurrences() {
        return totalnumberofOccurrences;
    }

    /**
     * @see org.opensat.ILiteral#unassign()
     */
    public void unassign() {
        value = UNASSIGNED;
        opposite.value = UNASSIGNED;
    }
    /**
    																			 * @see org.opensat.ILiteral#uniquelySatisfiesAClause()
    																			 */
    public boolean uniquelySatisfiesAClause() {
        Iterator it = clauses.iterator();
        while (it.hasNext()) {
            IClause cl = (IClause) it.next();
            if (cl.isSatisfiedByOnlyOneLiteral()) {
                return true;
            }
        }
        return false;
    }
    /**
     * @see org.opensat.data.ILiteral#unregister(org.opensat.data.IClause)
     */
    public void unregister(IClause cl) {
        totalnumberofOccurrences--;
    }

    /**
     * @see org.opensat.ILiteral#value()
     */
    public int value() {
        return value;
    }

    /**
     * @see org.opensat.ILiteral#watch(IClause)
     */
    public void watch(IClause cl) {
        assert cl.contains(this);
        assert !clauses.contains(cl);
        clauses.add(cl);
    }

    ArrayList list() {
        return clauses;
    }
    /**
     * @see org.opensat.utils.HeapItem#greaterThan(HeapItem)
     */
    public boolean greaterThan(HeapItem item) {
    	if (item instanceof LiteralSimpleImpl) {
    		return weight>((LiteralSimpleImpl)item).weight;
    	}
        return false;
    }

    /**
     * @see org.opensat.utils.HeapItem#print()
     */
    public void print() {
    	System.out.print(this+"("+weight+")");
    }

}
