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
package org.opensat.heuristics;

import java.util.Iterator;

import org.opensat.IFormula;
import org.opensat.algs.ISatHeuristic;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class TwoSidedJW implements ISatHeuristic {

    /**
     * @see org.opensat.heuristics.ISatHeuristic#choose(ICNF)
     */
    public ILiteral choose(IFormula f) {
        return choose(((ICNF) f).getVocabulary().variableIterator()); 
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(java.util.Iterator)
     */
    public ILiteral choose(Iterator it) {
        ILiteral res = null;
        long max = 0;
        while (it.hasNext()) {
            ILiteral l = (ILiteral) it.next();
            if (l.isUnassigned()) {
                long valuepos = getWeightForLiteral(l);
                assert valuepos >= 0;
                long valueneg = getWeightForLiteral(l.opposite());
                assert valueneg >= 0;
                long total = agregate(valuepos, valueneg);
                assert total >= 0;
                if (total > max) {
                    res = l;
                    max = total;
                }
            }
        }
        return res;
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.data.ILiteral)
     */
    public ILiteral choose(ILiteral[] vars) {
        ILiteral res = null;
        long max = 0;
        for(int i=0;i<vars.length;i++) {
            ILiteral l = vars[i];
            if (l.isUnassigned()) {
                long valuepos = getWeightForLiteral(l);
                assert valuepos >= 0;
                long valueneg = getWeightForLiteral(l.opposite());
                assert valueneg >= 0;
                long total = agregate(valuepos, valueneg);
                assert total >= 0;
                if (total > max) {
                    res = l;
                    max = total;
                }   
            }  
        }
        return res;
    }

    protected int getWeightForLiteralInClause(ILiteral l, IClause cl) {
        if (cl.size() >= weights.length) {
            return weights[weights.length - 1];
        } else {
            return weights[cl.size()];
        }
    }

    protected long getWeightForLiteral(ILiteral l) {
        long weight = 0;
        Iterator it = l.clauseIterator();
        while (it.hasNext()) {
            IClause cl = (IClause) it.next();
            if (!cl.isSatisfied()) {
                weight += getWeightForLiteralInClause(l, cl);
            }
        }
        return weight;
    }

    protected long agregate(long pos, long neg) {
        // equivalent to pos*neg*1024 + pos + neg
        return ((pos * neg) << 10) + pos + neg;
    }

    protected static final int[] weights = { 0, 125, 25, 5, 1 };

    /**
     * @see org.opensat.ISatHeuristic#init(org.opensat.IFormula)
     */
    public void init(IFormula f) {
        ICNF cnf = (ICNF) f;
        ILiteral [] vars = cnf.getVocabulary().getVariables();
        long max = 0;
        for(int i=0;i<vars.length;i++) {
            ILiteral l = vars[i];
            long valuepos = getWeightForLiteral(l);
            assert valuepos >= 0;
            l.setWeight(valuepos);
            long valueneg = getWeightForLiteral(l.opposite());
            assert valueneg >= 0;
            l.opposite().setWeight(valueneg);
        }
    }

    /**
     * @see org.opensat.ISatHeuristic#onBacktrackingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onBacktrackingLiteral(IFormula f, ILiteral l) {
        // add satisfied clause weight to its literals
        
        // change weight for restored clauses 
    }

    /**
     * @see org.opensat.ISatHeuristic#onLearnedClause(org.opensat.IFormula, null)
     */
    public void onLearnedClause(IFormula f, IClause c) {
        // add weight for clauses.
    }

    /**
     * @see org.opensat.ISatHeuristic#onSatisfyingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onSatisfyingLiteral(IFormula f, ILiteral lit) {
        // remove satisfied clauses weight to the literals they contain
        
        // change weight for clauses reduced
    }

	/**
	 * @see org.opensat.ISatHeuristic#onRestart(IFormula)
	 */
	public void onRestart(IFormula f) {
	}

}
