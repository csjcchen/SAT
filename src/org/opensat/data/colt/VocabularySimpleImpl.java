package org.opensat.data.colt;

import java.util.Iterator;

import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * @author roussel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
class VocabularySimpleImpl implements IVocabulary {

    private ILiteral[] variables; // literals in this vocabulary
    private boolean frozen; // when true, no modification is allowed
    private int maxVarId = 0; // greatest id used for a variable

    class VariableIterator implements Iterator {

        int pos;

        void init() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < variables.length;
        }

        public Object next() {
            return variables[pos++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public VocabularySimpleImpl() {
        clear();
    }

    public Iterator variableIterator() {
        VariableIterator it = new VariableIterator();
        it.init();
        return it;
    }

    /**
     * @see org.opensat.data.IVocabulary#getVariables()
     */
    public ILiteral[] getVariables() {
        return variables;
    }

    public void clear() {
        variables = new ILiteral[0];
        frozen = false;
    }

    /**
     * @see org.opensat.IVocabulary#freeze()
     */
    public void freeze(Iterator clausesIt) {
        // remove holes in variables.
        boolean modified = false;
        int last = variables.length;

        //		maxVarId = 0;

        while (last > 0 && variables[last - 1] == null) {
            modified = true;
            last--;
        }

        // Put relevance for all literals appearing in clauses
        while (clausesIt.hasNext()) {
            IClause clause = (IClause) clausesIt.next();
            clause.setRelevance(true);
        }

        // For all var if var is not relevant : it does not  appear in clauses
        for (int i = 0; i < last; i++) {
            if ((variables[i] == null) || (!variables[i].isRelevant())) {
                variables[i] = variables[last - 1];
                variables[last - 1] = null;
                last--;
                modified = true;
            } else
                variables[i].setRelevance(true);
            while (variables[last - 1] == null) {
                modified = true;
                last--;
            }
            //			if (i < last)
            //				maxVarId = Math.max(maxVarId, variables[i].getId());
        }

        assert(last == variables.length) || modified;

        if (modified) {
            ILiteral[] newvariables = new ILiteral[last];
            System.arraycopy(variables, 0, newvariables, 0, last);
            variables = newvariables;
            assert variables.length == last;
        }
        frozen = true;
    }

    /**
     * @see org.opensat.IVocabulary#setUniverse(int)
     */
    public void setUniverse(int nbvars) {
        variables = new ILiteral[nbvars];
        maxVarId = nbvars;
    }

    /**
     * This is a helper method to translate an integer into an
     * ILiteral object. If the literal is not found during the formula
     * construction, then a new ILiteral is returned, else an exception is launched.
     * (DLB: or should we only return null?)
     *
     * @see org.opensat.IVocabulary#getLiteral(int)
     */
    public ILiteral getLiteral(int i) {

        maxVarId = Math.max(maxVarId, Math.abs(i));

        ILiteral lit;
        int id = Math.abs(i) - 1; // maps from 1..n to 0..n-1
        if (variables[id] == null) {
            if (frozen) {
                throw new UnsupportedOperationException("Not constructing formula");
            }
            variables[id] = createLiteral(id + 1);
        }
        if (i > 0) {
            lit = variables[id];
        } else {
            lit = variables[id].opposite();
        }
        return lit;
    }

    /**
     * Factory method to create a literal and its opposite.
     *
     * @param i
     * @return ILiteral
     */
    protected ILiteral createLiteral(int i) {
        LiteralSimpleImpl lit = new LiteralSimpleImpl(i);
        LiteralSimpleImpl opposite = new LiteralSimpleImpl(lit);
        return lit;
    }
    /**
     * @see org.opensat.IVocabulary#getNumberOfVariables()
     */
    public int getNumberOfVariables() {
        assert variables != null;
        return variables.length;
    }

    /**
     * @see org.opensat.IVocabulary#getMaxVariableId()
     */
    public int getMaxVariableId() {
        return maxVarId;
    }
}
