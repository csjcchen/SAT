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

import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class CNFSimpleImplWL extends CNFSimpleImpl {

    private static final int MINIMUM_SIZE_FOR_WATCHED_LITERALS = 2;

    /**
     * Constructor for CNFSimpleImplWL.
     */
    public CNFSimpleImplWL() {
        super();
    }

    /**
     * Constructor for CNFSimpleImplWL.
     * @param voc
     */
    public CNFSimpleImplWL(IVocabulary voc) {
        super(voc);
    }

    /* (non-Javadoc)
     * @see org.opensat.data.ICNF#createClause(org.opensat.data.ILiteral[], int)
     */
    public IClause createClause(ILiteral[] lits, int size) {
        if (size >= MINIMUM_SIZE_FOR_WATCHED_LITERALS) {
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
        if (literals.size() >= MINIMUM_SIZE_FOR_WATCHED_LITERALS) {
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
                assert !unitclauses.contains(cl) : cl.isLearned();
                unitclauses.add(cl);
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
                assert !unitclauses.contains(cl);
                unitclauses.add(cl);
            }
        }
    }

}
