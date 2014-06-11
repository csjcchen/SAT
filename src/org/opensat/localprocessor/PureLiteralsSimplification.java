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
 * Created on 8 d?c. 2002
 * 
 */
package org.opensat.localprocessor;

import org.opensat.algs.DPLLStats;
import org.opensat.algs.SolverAdapter;
import org.opensat.data.ICNF;
import org.opensat.data.ILiteral;
/**
 * This class allows Pure Literal simplification as a local processing.
 * BEWARE: pure literals simplification does not preserve full equivalency
 * but only satisfiability equivalency. One cannot use that simplification into 
 * non chronological bactracking algorithms or with the mysteriousSimplification
 * scheme in StackBasedDPLL.
 * 
 * @author leberre
 *
 */
public class PureLiteralsSimplification implements ILocalProcessor {

    protected SolverAdapter dpll;

    /**
     * Constructor for PureLiteralsSimplification.
     */
    public PureLiteralsSimplification(SolverAdapter dpll) {
        this.dpll = dpll;
    }

    /**
     * Method performPropagation.
     * Performed operations with a pure literal
     * @param f
     * @param l
     */
    protected void performPropagation(ICNF f, ILiteral l) {
        dpll.fix(f, l);
    }

    /*
     * @see org.opensat.algs.ILocalProcessor#process(ICNF)
     */
    public boolean process(ICNF f) {
        boolean modified = false;
        ILiteral[] vars = f.getVocabulary().getVariables();
        for (int i = 0; i < vars.length; i++) {
            ILiteral lit = vars[i];
            if (lit.isUnassigned()) {
                int nbpos = lit.occurrences();
                int nbneg = lit.opposite().occurrences();

                if ((nbpos > 0) && (nbneg == 0)) {
                    performPropagation(f, lit);
                    ((DPLLStats) (dpll.getStatistics())).newPure();
                    modified = true;
                } else if ((nbneg > 0) && (nbpos == 0)) {
                    performPropagation(f, lit.opposite());
                    ((DPLLStats) (dpll.getStatistics())).newPure();
                    modified = true;
                }
            }
        }
        return modified;
    }
}
