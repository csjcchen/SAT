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
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.opensat.IFormula;

/**
 * This interface represents services available from a 
 * formula. A formula can be in two modes: construction mode,
 * required when constructing the formula from a file for instance,
 * and search mode. The user put a formula in construction mode 
 * by calling beginLoadFormula() and put it back in search mode by
 * calling endLoadFormula().
 * 
 * @author leberre
 */
public interface ICNF extends IFormula {

	/**
	 * A method to be used in conjunction with nextUnitClause
	 * to simulate an "iterator" over the unit clauses.
	 * 
	 * @return boolean
	 * @see #nextUnitClause
	 * @throws ContradictionFoundException if a null clause is
	 * detected during the process (NOTE: it looks like it never happens.
	 * We need to check this out).
	 */
	boolean hasUnitClause() throws ContradictionFoundException;

	/**
	 * Pop the next unit clause and remove it from the stack
	 * of unit clauses.
	 *
	 * @return IClause
	 */
	IClause nextUnitClause();

	/**
	 * Propagates the satisfaction of a literal.
	 * Clauses containing that literal are notified
	 * being satisfied, while clauses containing the
	 * opposite literal are reduced.
	 * @param l the literal being satisfied. Note that the literal 
	 * IS NOT satisfied before the method is called, while it will
	 * be satisfied after a call to this method. That method is 
	 * responsible for satisfying the literal (it does not makes sense
	 * to do it before calling the method, and the code is clearer like that).
	 * 
	 * @return false iff an empty clause is detected.
	 * @see #unpropagateAssignment(ILiteral)
	 * @see IClause#satisfy(ILiteral)
	 * @see IClause#reduce(ILiteral)
	 * @see ILiteral#satisfy()
	 */
	boolean propagateAssignment(ILiteral l);

	/**
	 * Unpropagate a literal satisfaction.
	 * Clauses containing that literal or the opposite literal are updated,
	 * 
	 * @param l a satisfied literal. After a call to that method, the literal must be unAssigned().
	 * @see #propagateAssignment(ILiteral)
	 * @see IClause#unassign(ILiteral)
	 * @see IClause#restore(ILiteral)
	 * @see ILiteral#unassign() 
	 */
	void unpropagateAssignment(ILiteral l);

	/**
	 * To check if a formula is really satisfied.
	 * Iterates on all clauses to check that they are all satisfied.
	 * This code should just be used to check that a satisfiable assignment
	 * is really found (costly operation).
	 * 
	 * @return true if the formula does not contain any unsatisfied clause.
	 */
	boolean isSatisfied();

	/**
	 * To obtain the vocabulary of the formula.
	 * 
	 * @return IVocabulary
	 */
	IVocabulary getVocabulary();

	/**
	 * To add a clause to the formula in construction mode.
	 * 
	 * @param literals
	 */
	void addClause(List literals);

	/**
	 * Clean up the formula in order to build a new one.
	 * Put the formula in creation mode.
	 */
	void beginLoadFormula();

	/**
	 * Notify the formula that the construction is finished.
	 * Put the formula in search mode.
	 */
	void endLoadFormula();

	/**
	 * Returns true if a null clause appears in the formula.
	 * @return boolean
	 */
	boolean hasNullClause();

	
	/**
	 * Returns the last null (empty) clause detected in the
	 * formula.
	 * @return IClause
	 */
     IClause getLastNullClause(ILiteral lit);

	/**
	 * access to the clauses
	 * @return Iterator iterator on the set of active clauses
	 */
	Iterator activeClauseIterator();
	
	/*added by Jinchuan Chen 17, June, 2014
	 *To get the full list of clauses 
	 * */
	Iterator fullClauseIterator();
	
	/**
	 * Formula size in terms of active clauses.
	 * 
	 * will return 0 as long as endLoadFormula() hasn't been called
	 * 
	 * @return int
	 */
	int size();

	/**
	 * display this formula in Dimacs format
	 * @param out : output stream
	 */
	void display(PrintWriter out);

	/**
	 * provide information concerning the maximum variable id
	 * and the expected number of clauses in the formula.
	 * 
	 * @see IVocabulary#setUniverse(int)
	 */	
	void setUniverse(int nbvars, int nbclauses);
	
	Iterator getNullClauses();
	
	/**
	 * To add a clause in the formula during search.
	 * 
	 * @param cl the clause to add in the formula.
	 */
	void learn(IClause cl);
	
	/**
	 * Flush all stacks to be able to reuse the formula
	 * for a new search (usefull for restart strategies).
	 */
	void flush();
	
	/**
	 * To know the size of the bigger clause.
	 * 
	 * @return int
	 */
	int maxLengthClause();
    
    IClause createClause(List i);
    
    IClause createClause(ILiteral [] lits, int size);
    
 
    
}
