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
 * Created on 8 janv. 2003
 * 
 */
package org.opensat.utils;

import org.opensat.Default;
import org.opensat.IFormula;
import org.opensat.IParser;
import org.opensat.ISolver;
import org.opensat.data.ICNF;
import org.opensat.parsers.Dimacs;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class Launcher extends AbstractLauncher {

	private Dimacs parser;
	private ICNF formula;

	public Launcher() {
		super("OpenSAT", "org.opensat.algs", "org.opensat.heuristics");
	}

	protected ISolver createDefaultSolver() {
		return Default.solver();
	}

	public static void main(String[] args) {
		(new Launcher()).run(args);
	}

	/**
	 * @see org.opensat.utils.AbstractLauncher#createParser()
	 */
	protected IParser createParser() {
		return Default.parser();
	}

	/**
	 * @see org.opensat.utils.AbstractLauncher#createDefaultFormula()
	 */
	protected IFormula createDefaultFormula() {
		return Default.cnf();
	}

}
