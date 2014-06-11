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
package org.opensat.algs;

import org.opensat.ICertificate;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * An implementation of a geral certificate for satisfiability.
 * It makes the interface between the internal representation 
 *  of a literal (ILiteral) and its external representation (a number).
 * 
 * The certificate is filled using the add(ILiteral) method.
 * The truth value of a given variable is retrieve using its id.
 * 
 * @author leberre
 *
 */
public class Certificate implements ICertificate {

	/**
	 * Constructor for Certificate.
	 */
	public Certificate(IVocabulary voc) {
		super();
		assignment = new int[voc.getMaxVariableId()+1];
		// the array is assigned with zeros
	}

	/*
	 * @see org.opensat.algs.ICertificate#valueOf(int)
	 */
	public int valueOf(int i) {
		return assignment[i];
	}


	private int [] assignment;

	/**
	 * Add a literal to the certificate.
	 * 
     * @param lit an internal representation of the literal.
     * @return ICertificate a certificate containing lit.
     */
    public ICertificate add(ILiteral lit) {
		assignment[Math.abs(lit.getId())]=lit.getId()>0?TRUE:FALSE;
        return this;
	}

	/**
	 * Produces a textual repesentation of the certificate.
	 * 
	 * @return a a String representation of the certificate as a 
	 * sequence of numbers finished by 0.
	 */
	public String toString() {
		StringBuffer stb = new StringBuffer();
		for (int i=1;i<assignment.length;i++) {
			if (assignment[i]!=UNSET) {
				stb.append(i*assignment[i]);
				stb.append(" ");
			}
		}
		stb.append("0");
		return stb.toString();
	}

}
