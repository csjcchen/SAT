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
 * Created on 23 janv. 2003
 * 
 */
package org.opensat;


/**
 * That interface provides services necessary for
 * a certificate of satisfiability in the opensat framework.
 * 
 * @author leberre
 *
 */
public interface ICertificate {
	public static final int TRUE = 1;
	public static final int FALSE = -1;
	public static final int UNSET = 0;
	
	/**
	 * To obtain the truth value for a given variable id (as in the original 
	 * Dimacs formatted file).
	 * 
     * @param i a variable id
     * @return int the truth value of that variable. Must return TRUE, FALSE or UNSET. 
     */
    int valueOf(int i);
	
}
