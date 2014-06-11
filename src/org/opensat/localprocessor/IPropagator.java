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
 * Created on 12 jan 2002
 * 
 */
package org.opensat.localprocessor;


import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
/**
 * That interface represents a propagator engine for LookAhead process.
 * 
 * @author audemard
 *
 */
public interface IPropagator {

    /**
     * The propagation function
     * @param f  a formula
     * @param lp the lookahead processor associated
     * @param lit the literal to propagate
     * @return false if the propagation of l implies inconsistency
     */

    public  boolean propagateTrial(ICNF f,LookAhead lp,ILiteral lit);

    /**
     * The reason associated to the failed literal
     * Only uses when backjumping is enable
     * @return : the reason
     */
    
    public  IClause getReason();

}
