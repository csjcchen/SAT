package org.opensat.heuristics;

import java.util.Iterator;

import org.opensat.IFormula;
import org.opensat.algs.ISatHeuristic;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;
import org.opensat.data.simple.LiteralSimpleImpl;
import org.opensat.utils.EmptyHeapException;
import org.opensat.utils.Heap;

/**
 * @author leberre
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class VSIDSHeap implements ISatHeuristic {

    private Heap literals;
    private int learned;
    private ILiteral[] vars;
    private static final int RESORT = 100;

    /**
     * Constructor for VSIDS.
     */
    public VSIDSHeap() {
        super();
    }

    /**
     * @see org.opensat.ISatHeuristic#init(org.opensat.IFormula)
     */
    public void init(IFormula f) {
        IVocabulary voc = ((ICNF) f).getVocabulary();
        literals = new Heap();
        vars = voc.getVariables();
        int index = 0;
        for (int i = 0; i < vars.length; i++) {
            ILiteral lit = vars[i];
            lit.setWeight(lit.totalOccurrences());
            lit.opposite().setWeight(lit.opposite().totalOccurrences());
            if (lit.getWeight() > lit.opposite().getWeight()) {
                literals.insert((LiteralSimpleImpl) lit);
            } else {
                literals.insert((LiteralSimpleImpl) lit.opposite());
            }
        }
        learned = 0;
    }

    /**
     * @see org.opensat.ISatHeuristic#onSatisfyingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onSatisfyingLiteral(IFormula f, ILiteral lit) {
    }

    /**
     * @see org.opensat.ISatHeuristic#onBacktrackingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onBacktrackingLiteral(IFormula f, ILiteral l) {
        literals.insert((LiteralSimpleImpl) l);
    }

    /**
     * @see org.opensat.ISatHeuristic#onLearnedClause(org.opensat.IFormula, org.opensat.data.IClause)
     */
    public void onLearnedClause(IFormula f, IClause c) {
        ILiteral[] cLiterals = c.getLiterals();
        int cNbLiterals = c.getLiteralsSize();

        for (int i = 0; i < cNbLiterals; i++) {
            cLiterals[i].setWeight(cLiterals[i].getWeight() + 1);
        }
        //        if (++learned % RESORT == 0) {
        //            Arrays.sort(literals, comparator);
        //        }
        if (learned % RESORT * 10 == 0) {
            for (int i = 0; i < vars.length; i++) {
                vars[i].setWeight(vars[i].getWeight() >> 1);
                vars[i].opposite().setWeight(
                    vars[i].opposite().getWeight() >> 1);
            }
        }
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.IFormula)
     */
    public ILiteral choose(IFormula f) {
        return choose(((ICNF) f).getVocabulary().variableIterator());
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(java.util.Iterator)
     */
    public ILiteral choose(Iterator it) {
        try {
            ILiteral lit;
            do {
                lit = (ILiteral) literals.deleteMin();
            } while (!lit.isUnassigned());
            return lit;
        } catch (EmptyHeapException e) {
            return null;
        }
        //        for (int next = 0; next < literals.length; next++) {
        //            if (literals[next].isUnassigned()) {
        //                if (literals[next].getWeight()
        //                    > literals[next].opposite().getWeight()) {
        //                    return literals[next];
        //                } else {
        //                    return literals[next].opposite();
        //                }
        //            }
        //        }
        //        return null;
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.data.ILiteral)
     */
    public ILiteral choose(ILiteral[] vars) {
        throw new UnsupportedOperationException();
        //        for (int next = 0; next < literals.length; next++) {
        //            if (literals[next].isUnassigned()) {
        //                if (literals[next].getWeight()
        //                    > literals[next].opposite().getWeight()) {
        //                    return literals[next];
        //                } else {
        //                    return literals[next].opposite();
        //                }
        //            }
        //        }
        //        return null;
    }

    /**
     * @see org.opensat.ISatHeuristic#onRestart(IFormula)
     */
    public void onRestart(IFormula f) {
        literals = new Heap();
        for (int i = 0; i < vars.length; i++) {
            ILiteral lit = vars[i];
            if (lit.getWeight() > lit.opposite().getWeight()) {
                literals.insert((LiteralSimpleImpl) lit);
            } else {
                literals.insert((LiteralSimpleImpl) lit.opposite());
            }
        }
    }

}
