/*
 * The OpenSAT project
 * Copyright (c) 2002-2003, Joao Marques-Silva and Daniel Le Berre
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
 * Created on 28 janv. 2003
 * 
 */
package org.opensat.algs;

import org.opensat.ISolver;
import org.opensat.data.ICNF;
import org.opensat.data.ILiteral;

/**
 * Basic methods and fields of a solver
 * 
 * @author audemard
 *
 */
public abstract class SolverAdapter implements ISolver {

    protected long timeout = Integer.MAX_VALUE;

    protected long bound;

    protected boolean stopASAP; // true iff we must abort the SAT test

    public SolverAdapter() {
        stopASAP = false;
        timeout = Integer.MAX_VALUE;
    }

    public void setTimeout(int seconds) {
        timeout = seconds;
    }

    /**
         * Method fix.
         * Fix the literal l in the formula f
         * @param f
         * @param l
         */
    public abstract void fix(ICNF f, ILiteral l);
}
