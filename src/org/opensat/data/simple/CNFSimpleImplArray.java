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
 * Created on 28 janv. 2003
 * 
 */
package org.opensat.data.simple;

import java.util.List;

import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class CNFSimpleImplArray extends CNFSimpleImpl {

	/**
	 * Constructor for CNFSimpleImplArray.
	 */
	public CNFSimpleImplArray() {
		super();
	}

	/**
	 * Constructor for CNFSimpleImplArray.
	 * @param voc
	 */
	public CNFSimpleImplArray(IVocabulary voc) {
		super(voc);
	}

    /* (non-Javadoc)
     * @see org.opensat.data.ICNF#createClause(org.opensat.data.ILiteral[], int)
     */
    public IClause createClause(ILiteral[] lits, int size) {
        return new ClauseSimpleImplArray(lits,size);
    }

	/**
	 * @see org.opensat.data.simple.CNFSimpleImpl#createClause(List)
	 */
	public IClause createClause(List literals) {
		return new ClauseSimpleImplArray(literals);
	}

}
