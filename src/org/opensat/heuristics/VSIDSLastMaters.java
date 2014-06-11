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
public class VSIDSLastMaters implements ISatHeuristic {

    class Triplet {
        ILiteral literal;
        long oldweight;
        long oldweightopp;

        Triplet(ILiteral lit) {
            if (lit.getWeight() > lit.opposite().getWeight()) {
                literal = lit;
            } else {
                literal = lit.opposite();
            }
            oldweight = literal.getWeight();
            oldweightopp = literal.opposite().getWeight();
        }

        void update() {
            long w1 = literal.getWeight();
            long w2 = literal.opposite().getWeight();

            long nw1 = w1/2+(w1-oldweight);
            literal.setWeight(nw1);
            
            long nw2 = w2/2+(w2-oldweightopp);
            literal.opposite().setWeight(nw2);
                        
            if (nw2 > nw1) {
                literal = literal.opposite();
                oldweight = w2;
                oldweightopp = w1;
            } else {
                oldweight = w1;
                oldweightopp = w2;
            }
        }
        
        void adjust() {
            long w1 = literal.getWeight();
            long w2 = literal.opposite().getWeight();

            if (w2>w1) {
                literal = literal.opposite();
                long tmp = oldweight;
                oldweight = oldweightopp;
                oldweightopp = tmp;
            }
        }
    }

    class WeightComparator implements Comparator {

        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(Object o1, Object o2) {
            ILiteral l1 = ((Triplet) o1).literal;
            ILiteral l2 = ((Triplet) o2).literal;
            long w1 = l1.getWeight() + l1.opposite().getWeight();
            long w2 = l2.getWeight() + l2.opposite().getWeight();
            return (int) (w2 - w1);
        }

    }
    private static final int RESORT = 100;
    private final Comparator comparator = new WeightComparator();
    private int learned;

    protected Triplet [] triplets;

    /**
     * Constructor for VSIDS.
     */
    public VSIDSLastMaters() {
        super();
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.IFormula)
     */
    public ILiteral choose(IFormula f) {
        return chooseBest();
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.data.ILiteral)
     */
    public ILiteral choose(ILiteral[] vars) {
        return chooseBest();
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(java.util.Iterator)
     */
    public ILiteral choose(Iterator it) {
        return chooseBest();
    }

    protected ILiteral chooseBest() {
        for (int next = 0; next < triplets.length; next++) {
            if (triplets[next].literal.isUnassigned()) {
                return triplets[next].literal;
            }
        }
        return null;
    }

    /**
     * @see org.opensat.ISatHeuristic#init(org.opensat.IFormula)
     */
    public void init(IFormula f) {
        IVocabulary voc = ((ICNF) f).getVocabulary();
        triplets = new Triplet[voc.getNumberOfVariables()];
        ILiteral[] vars = voc.getVariables();
        for (int i = 0; i < vars.length; i++) {
            vars[i].setWeight(vars[i].totalOccurrences());
            vars[i].opposite().setWeight(vars[i].opposite().totalOccurrences());
            triplets[i]=new Triplet(vars[i]);
        }
        assert vars.length == triplets.length;
        Arrays.sort(triplets, comparator);

        learned = 0;
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
            // FIXME: DLB should also sort old values.
            Arrays.sort(triplets, comparator);
        }
        if (learned % RESORT * 10 == 0) {
            for (int i = 0; i < triplets.length; i++) {
                triplets[i].update();
             }
        }
    }
    /**
     * @see org.opensat.ISatHeuristic#onRestart(IFormula)
     */
    public void onRestart(IFormula f) {
        for (int i = 0; i < triplets.length; i++) {
            triplets[i].adjust();
        }
        Arrays.sort(triplets, comparator);
    }

    /**
     * @see org.opensat.ISatHeuristic#onSatisfyingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onSatisfyingLiteral(IFormula f, ILiteral lit) {
    }
}
