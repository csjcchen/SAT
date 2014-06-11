package org.opensat.heuristics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.opensat.IFormula;
import org.opensat.algs.ISatHeuristic;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * @author leberre
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class VSIDS implements ISatHeuristic {

    protected ILiteral[] literals;
    private final Comparator comparator = new WeightComparator();
    private int learned;
    private static final int RESORT = 100;

    /**
     * Constructor for VSIDS.
     */
    public VSIDS() {
        super();
    }

    /**
     * @see org.opensat.ISatHeuristic#init(org.opensat.IFormula)
     */
    public void init(IFormula f) {
        IVocabulary voc = ((ICNF) f).getVocabulary();
        literals = new ILiteral[voc.getNumberOfVariables()];
        ILiteral[] vars = voc.getVariables();
        for (int i = 0; i < vars.length; i++) {
            vars[i].setWeight(vars[i].totalOccurrences());
            vars[i].opposite().setWeight(vars[i].opposite().totalOccurrences());
            if (vars[i].getWeight() > vars[i].opposite().getWeight()) {
                literals[i] = vars[i];
            } else {
                literals[i] = vars[i].opposite();
            }
        }
        assert vars.length == literals.length;
        Arrays.sort(literals, comparator);
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
        if (++learned % RESORT == 0) {
            Arrays.sort(literals, comparator);
        }
        if (learned % RESORT * 10 == 0) {
            for (int i = 0; i < literals.length; i++) {
                literals[i].setWeight(literals[i].getWeight() >> 1);
                literals[i].opposite().setWeight(
                    literals[i].opposite().getWeight() >>1);
            }
        }
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.IFormula)
     */
    public ILiteral choose(IFormula f) {
        return chooseBest();
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(java.util.Iterator)
     */
    public ILiteral choose(Iterator it) {
    	return chooseBest();
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.data.ILiteral)
     */
    public ILiteral choose(ILiteral[] vars) {
    	return chooseBest();
    }

    protected ILiteral chooseBest() {
        for (int next = 0; next < literals.length; next++) {
            if (literals[next].isUnassigned()) {
                return literals[next];
            }
        }
        return null;
    }

    class WeightComparator implements Comparator {

        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(Object o1, Object o2) {
            ILiteral l1 = (ILiteral) o1;
            ILiteral l2 = (ILiteral) o2;
            long w1 = l1.getWeight() + l1.opposite().getWeight();
            long w2 = l2.getWeight() + l2.opposite().getWeight();
            return (int) (w2-w1);
        }

    }
    /**
     * @see org.opensat.ISatHeuristic#onRestart(IFormula)
     */
    public void onRestart(IFormula f) {
    	for (int i = 0; i < literals.length; i++) {
            if (literals[i].getWeight() < literals[i].opposite().getWeight()) {
                literals[i] = literals[i].opposite();
            }
        }
        Arrays.sort(literals, comparator);
    }

}
