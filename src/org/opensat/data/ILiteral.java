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
 * Created on 24 nov. 2002
 * 
 */
package org.opensat.data;

import java.util.Iterator;

/**
 * Represents a literal in the formula. The same literal is used to
 * represent the same positive or negative occurrence of a variable 
 * in clauses.
 * 
 * @author leberre
 *
 */
public interface ILiteral {
	
	int UNASSIGNED = 0;
	int SATISFIED      = 1;
	int FALSIFIED      = -1;
	
	/**
	 * Mark a literal as satisfied.
	 */
	void satisfy();

	/**
	 * Mark a literal as falsified.
	 */
	void falsify();

	/**
	 * Unmark a literal.
	 */
	void unassign();

	/**
	 * To get the opposite literal.
	 * 
	 * this.opposite().opposite()==this
	 * @return ILiteral
	 */
	ILiteral opposite();
	
	/**
	 * Method clauseIterator.
	 * @return Iterator
	 */
	Iterator clauseIterator();

	/**
	 *  To check the state of the literal.
	 * 
	 * @return either SATISFIED, FALSIFIED or UNASSIGNED.
	 */
	int value();
	
	/**
	 * Register a clause that contains that literal.
	 * This method is used to know EXACTLY in how
	 * many clauses that literal appears.
	 * 
	 * @param cl
	 */
	void register(IClause cl);
	
	/**
	 * Unregister a clause that contained that literal.
	 * 
	 * @param cl
	 * @see #register
	 */
	void unregister(IClause cl);

	/**
	 * Watch that particular clause.
	 * 
	 * @param cl a clause that contains that literal.
	 */
	void watch(IClause cl);
	
	/**
	 * Hide that clause to the literal
	 * 
	 * @param cl a clause that contains that literal
	 */
	void hide(IClause cl);
	
	/**
	 * Method isUnassigned.
	 * @return boolean
	 */
	boolean isUnassigned();

	/**
	 * Method isSatisfied.
	 * @return boolean
	 */
	boolean isSatisfied();

	/**
	 * Method isFalsified.
	 * @return boolean
	 */
	boolean isFalsified();

	/**
	 * Method uniquelySatisfiesAClause.
	 * @return boolean
	 */
	boolean uniquelySatisfiesAClause();

	/**
	 * Returns the original literal id.
	 * @return int
	 */
	int getId();

	/**
	 * To know the number of occurrences of that literal in non 
	 * satisfied clause.
	 * @return int
	 */
	int occurrences();
	
	/**
	 * To know if that literal is marked as relevant.
	 * 
	 * @return boolean
	 */
	boolean isRelevant();
	
	/**
	 * Sets the relevance of the literal and its opposite literal.
	 * (a literal is relevant iff its opposite is relevant).  
	 * @param b true iff the literal is relevant.
	 */
	void setRelevance(boolean b);
    
    /**
     * Set a weight to that literal.
     * 
     * @param w
     */
    void setWeight(long w);
    
    /**
     * Get that literal weight, as fixed by setWeight.
     * No weight should be computed here. It is just a convenient 
     * place to store that information.
     * 
     * @return long
     */
    long getWeight();
    
    
    /**
     * To know the total number of occurrences of that literal in registered
     * clauses.
     * 
     * @return int
     */
    int totalOccurrences();
    
    
    /**
     * To know when that literal was assigned.
     * 
     * @return int the decision level of the assignment, or -1 if unassigned.
     */
    int getDecisionLevel();
    
    /**
     * To set the decision level of that literal.
     * Note that a literal and its opposite have the same decision level.
     * -1 is used for unassigned literals.
     * @param i
     */
    void setDecisionLevel(int i);
    
	/**
	 * To get the reason why that literal is satisfied.
	 * Null means no reason.
	 * 
	 * @return IClause
	 */
    IClause getReason();
    
	/**
	 * To associate a reason to that 
	 * literal.
	 * 
	 * @param cl
	 */
    void setReason(IClause cl);
}
