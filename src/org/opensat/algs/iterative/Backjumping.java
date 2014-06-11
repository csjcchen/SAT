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
 * Created on 6 févr. 2003
 * 
 */
package org.opensat.algs.iterative;

import org.opensat.data.ContradictionFoundException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.Resolver;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class Backjumping implements IBacktracker {

    private Resolver resolver;

    /**
     * Constructor for Backjumping.
     */
    public Backjumping() {
        super();
    }

    /**
     * @see org.opensat.algs.iterative.IBacktracker#backtrack(StackBasedDPLL, ICNF)
     */
    public ILiteral backtrack(StackBasedDPLL dpll, ICNF f) {
        // here should be computed the reasons
        assert dpll.current().isSatisfied();
        // Init working reason
        IClause conflictClause = f.getLastNullClause(dpll.current().opposite());
        resolver.initialize(conflictClause);
        while (dpll.getDecisionLevel() >= 0) {
            IClause reason = dpll.current().getReason();
            dpll.unfix(f);
            assert dpll.current().isUnassigned();
            if (resolver.contains(dpll.current().opposite())) {
                // The literal is in conflict clause
                if (dpll.next() == null) {
                    // NO other branch with this litteral 
                    // Resolve it reason with the conflictClause
                    try {
                        resolver.resolveWith(reason);
                        // conflictClause = conflictClause.resolveWith(reason);
                    } catch (ContradictionFoundException e) {
                        // If the conflict clause is null, then the problem is UNSAT
                        dpll.down();
                        return null;
                    }
                    dpll.down();
                    // the resulting clause should not be tautological.
                    assert conflictClause != null;

                } else {
                    //We have found the litteral which causes the reason
                    // We stop the backjump and go on the other side
                    // We have to put a reason to this literal
                    // Be careful if conflict clause is NULL
                    conflictClause = resolver.getResolvent(f);
                    assert conflictClause != null;
                    if (!conflictClause.isRegistered()) {
                        dpll.learn(f, conflictClause);
                    }
                    dpll.change(f, conflictClause);
                    return dpll.current();
                }
            } else { // Youpi : skipping litteral !
                if (dpll.next() != null) {
                    dpll.skip();
                }
                dpll.down();
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opensat.algs.iterative.IBacktracker#init(org.opensat.data.ICNF)
     */
    public void init(ICNF f) {
        resolver = new Resolver(f.getVocabulary().getNumberOfVariables());
    }

}
