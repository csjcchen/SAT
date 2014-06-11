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
 * Created on 6 févr. 2003
 * 
 */
package org.opensat.algs.iterative;

import org.opensat.data.ICNF;
import org.opensat.data.ILiteral;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public interface IBacktracker {


	/**
     * @param dpll
     * @param f
     * @return ILiteral
     */
    ILiteral backtrack(StackBasedDPLL dpll,ICNF f);
	
	/**
     * @param f
     */
    void init(ICNF f);
}
