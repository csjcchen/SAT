package org.opensat.data.simple;

import org.opensat.data.ILiteral;

/**
 * @author leberre
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FalsifiedLiteral extends LiteralSimpleImpl {

    /**
     * Constructor for FalsifiedLiteral.
     * @param id
     */
    private FalsifiedLiteral() {
        // use MAX_VALUE in order to sort that literal last!
        super(Integer.MAX_VALUE);
    }

    /**
     * Constructor for FalsifiedLiteral.
     * @param opposite
     */
    private FalsifiedLiteral(LiteralSimpleImpl opposite) {
        super(opposite);
    }

    /**
     * @see org.opensat.data.ILiteral#falsify()
     */
    public void falsify() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.opensat.data.ILiteral#isFalsified()
     */
    public boolean isFalsified() {
        return true;
    }

    /**
     * @see org.opensat.data.ILiteral#isSatisfied()
     */
    public boolean isSatisfied() {
        return false;
    }

    /**
     * @see org.opensat.data.ILiteral#isUnassigned()
     */
    public boolean isUnassigned() {
        return false;
    }

    /**
     * @see org.opensat.data.ILiteral#satisfy()
     */
    public void satisfy() {
        throw new UnsupportedOperationException();
   }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "F";
    }

    /**
     * @see org.opensat.data.ILiteral#unassign()
     */
    public void unassign() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.opensat.data.ILiteral#value()
     */
    public int value() {
        return FALSIFIED;
    }

    public static ILiteral FALSIFIED_LITERAL = new FalsifiedLiteral();
    
}
