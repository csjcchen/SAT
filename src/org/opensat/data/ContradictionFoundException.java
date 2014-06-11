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
 * Created on 9 déc. 2002
 * 
 */
package org.opensat.data;

/**
 * That exception is launched whenever a contradiction in found in the 
 * search process, local processing, etc.
 * One might extend that contradiction in order to provide an explanation 
 * of the contradiction.
 * 
 * @author leberre
 *
 */
public class ContradictionFoundException extends Exception {
	
	
	/**
	 * Constructor for ContradictionFoundException.
	 */
	public ContradictionFoundException() {
		super();
	}

	/**
	 * Constructor for ContradictionFoundException.
	 * @param message
	 */
	public ContradictionFoundException(String message) {
		super(message);
	}

	/**
	 * Constructor for ContradictionFoundException.
	 * @param message
	 * @param cause
	 */
	public ContradictionFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for ContradictionFoundException.
	 * @param cause
	 */
	public ContradictionFoundException(Throwable cause) {
		super(cause);
	}

}
