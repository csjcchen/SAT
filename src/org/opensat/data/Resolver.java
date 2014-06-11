package org.opensat.data;

import java.nio.BufferOverflowException;

/**
 * This class stores one single clause and let us perform resolution on it. The resolvent replaces the clause.
 * 
 * @author roussel
 */
public class Resolver {

    private ILiteral[] dest; // where to store the resolvent 
    private int destNbLit; // size of the resolvent

    // was a resolvent bigger that the maximum allowed ?
    private boolean overflow = false;

    private ILiteral[] src;
    // clause stored and that will be resolved (center clause)
    private int srcNbLit; // size of this clause

    /**
     * Initialize the resolver
     * 
     * @param maxResolventSize maximum size of the resolvent. 
     * If a resolvent becomes too large, overflowed() will become true
     * @see #overflowed() 
     */
    public Resolver(int maxResolventSize) {
        src = new ILiteral[maxResolventSize];
        dest = new ILiteral[maxResolventSize];
    }

    /**
     * Method getResolvent.
     * @return IClause
     * @throws BufferOverflowException when the resolver has overflowed
     */
    public IClause getResolvent(ICNF cf) {
        if (overflow)
            throw new BufferOverflowException();

        return cf.createClause(src, srcNbLit);
    }

    /**
     * Replaces any current clause with the clause provided as a parameter.
     * 
     * @param c clause that will be resolved upon later (center clause)
     * @see #resolveWith(IClause)
     */
    public void initialize(IClause c) {
        overflow = false; // so far, so good

        ILiteral[] lits = c.getLiterals();
        int nbLits = c.getLiteralsSize();

        srcNbLit = 0;

        for (int i = 0; i < nbLits; i++) {
            // check clause is ordered
            assert(
                (i < nbLits - 1)
                    ? (Math.abs(lits[i].getId()) < Math.abs(lits[i + 1].getId()))
                    : true);

            src[srcNbLit++] = lits[i];
        }
    }

    /**
     * Return true if the resolver has overflowed (one resolvent was bigger 
     * that the limit set in the constructor).
     * 
     * @return true iff this resolver has overflowed
     */
    public boolean overflowed() {
        return overflow;
    }

    /**
     * Resolve the center clause (stored in this resolver) with the side clause passed as parameter.
     * 
     * @param c clause to perform resolution with (side clause)
     */
    public void resolveWith(IClause c) throws ContradictionFoundException {
        // do we have a valid resolvent ?
        if (overflow)
            return; // no need to waste our time

        // resolve src with c, put result in dest
        destNbLit = 0;

        ILiteral[] other = c.getLiterals();
        int otherNbLit = c.getLiteralsSize();

        int iSrc = 0, iOther = 0, oppositecounter = 0;

        // modified merge algorithm
        while (iSrc < srcNbLit && iOther < otherNbLit) {
            if (destNbLit >= dest.length) {
                overflow = true;
                return;
            }

            if (src[iSrc] == other[iOther].opposite()) {
                // literal resolved upon
                iSrc++;
                iOther++;
                oppositecounter++;
            } else if (src[iSrc] == other[iOther]) {
                // merged literal
                dest[destNbLit++] = src[iSrc++];
                iOther++;
            } else if (
                Math.abs(src[iSrc].getId())
                    < Math.abs(other[iOther].getId())) {
                dest[destNbLit++] = src[iSrc++];
            } else {
                dest[destNbLit++] = other[iOther++];
            }
        }

        // do we get an overflow with the remaining literals ?
        if (destNbLit + srcNbLit - iSrc >= dest.length
            || destNbLit + otherNbLit - iOther >= dest.length) {
            overflow = true;
            return;
        }

        // copy the remaining literals in src (if any)
        while (iSrc < srcNbLit) {
            dest[destNbLit++] = src[iSrc++];
        }

        // copy the remaining literals in other (if any)
        while (iOther < otherNbLit) {
            dest[destNbLit++] = other[iOther++];
        }

        // swap src and dest
        ILiteral[] tmp;
        tmp = src;
        src = dest;
        dest = tmp;

        srcNbLit = destNbLit;

        // if the resolvent is a tautology, we restart with an empty center clause
        if (oppositecounter > 1) {
            srcNbLit = 0;
            return; // do not throw an exception even if clause is empty
        }

        if (srcNbLit == 0) {
            throw new ContradictionFoundException();
        }

    }

    public boolean contains(ILiteral lit) {
        int high = srcNbLit - 1;
        int low = 0;
        while (low <= high) {
            int mid = (low + high) >> 1;
            Object midVal = src[mid];
            int cmp = ((Comparable) midVal).compareTo(lit);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return true; // key found
        }
        return false; // key not found.
    }
}
