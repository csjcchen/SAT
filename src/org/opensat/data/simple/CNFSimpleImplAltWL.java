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
 * Created on 28 janv. 2003
 * 
 */
package org.opensat.data.simple;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class CNFSimpleImplAltWL extends CNFSimpleImplAlt {

    /**
     * Constructor for CNFSimpleImplWL.
     */
    public CNFSimpleImplAltWL() {
        super();
    }

    /**
     * Constructor for CNFSimpleImplWL.
     * @param voc
     */
    public CNFSimpleImplAltWL(IVocabulary voc) {
        super(voc);
    }

    /* (non-Javadoc)
     * @see org.opensat.data.ICNF#createClause(org.opensat.data.ILiteral[], int)
     */
    public IClause createClause(ILiteral[] lits, int size) {
        if (size >= 2) {
            return new ClauseSimpleImplWL(lits, size);
        } else {
            assert size == 1;
            return new UnitClause(lits[0]);
        }
    }

    /**
     * @see org.opensat.data.simple.CNFSimpleImpl#createClause(List)
     */
    public IClause createClause(List literals) {
        if (literals.size() >= 2) {
            return new ClauseSimpleImplWL(literals);
        } else {
            assert literals.size() == 1;
            ILiteral l = (ILiteral) literals.get(0);
            return new UnitClause(l);
        }
    }

    /* (non-Javadoc)
     * @see org.opensat.data.ICNF#propagateAssignment(org.opensat.data.ILiteral)
     */
    public boolean propagateAssignment(ILiteral l) {
        assert l.isUnassigned();
        l.satisfy();

        Iterator it = l.opposite().clauseIterator();
        while (it.hasNext()) {
            IClause cl = (IClause) it.next();
            cl.reduce(l.opposite(), it);
            // if (!cl.isSatisfied()) {
            if (cl.isUnit()) {
                // assert !unitclauses.contains(cl) : cl.isLearned();
                addUnitClause( cl);
            } else if (cl.isNull()) {
                assert !nullclauses.contains(cl);
                nullclauses.add(cl);
            }
            // }
        }
        return nullclauses.isEmpty();

    }

    /* (non-Javadoc)
     * @see org.opensat.data.ICNF#unpropagateAssignment(org.opensat.data.ILiteral)
     */
    public void unpropagateAssignment(ILiteral l) {
        assert l.isSatisfied();
        // need to unassign before looking for unit clauses
        l.unassign();

        Iterator it = l.opposite().clauseIterator();
        while (it.hasNext()) {
            IClause cl = (IClause) it.next();
            if (cl.isUnit()) {
                assert nullclauses.contains(cl);
                nullclauses.remove(cl);
                // assert !unitclauses.contains(cl);
                addUnitClause(cl);
            }
        }
    }
    
    /*
     * to show the assignments at current node
     * by JC Chen 2014.5.29
     * */
	public void showAssignments(){
		Iterator it = this.clauses.iterator();
		int numVar = this.getVocabulary().getMaxVariableId();
		int[] assign = new int[numVar*2+1]; 

		while (it.hasNext()) {
			System.out.println("___________________________");
            IClause cl = (IClause) it.next();
            Iterator litIterator = cl.literalIterator();
            while (litIterator.hasNext()){
            	ILiteral lit = (ILiteral) litIterator.next();
            	int idx = lit.getId();
            	if (idx<0) 
            		idx = numVar - idx;
            	assign[idx] = lit.value();
            }
        }
		
		for (int i=1;i<assign.length;i++){
			int k = i;
			if (i>numVar)
				k = numVar - i;
			System.out.print(k + "|" + assign[i] + ";");
		}
		System.out.println();
	}
	
	 /*
     * to get all the assignments at current node
     * by JC Chen 2014.5.29
     * */
	public int[]  getAllAssignments() {
		Iterator it = this.clauses.iterator();
		int numVar = this.getVocabulary().getMaxVariableId();
		int[] assign = new int[numVar*2+1]; 
			// 1.... numVar store positive literals
			// numVar+1, ..., assign.length-1 store negative literals		

		while (it.hasNext()) {
			IClause cl = (IClause) it.next();
            Iterator litIterator = cl.literalIterator();
            while (litIterator.hasNext()){
            	ILiteral lit = (ILiteral) litIterator.next();
            	int idx = lit.getId();
            	if (idx<0) 
            		idx = numVar - idx;
            	assign[idx] = lit.value();
            }
        }  
		
		return assign;
	}
 
}
