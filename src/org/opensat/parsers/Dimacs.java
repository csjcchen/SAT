/*
 * OpenSAT, the Open Satisfiability platform
 * Copyright (C) 2002 Joao Marques Silva, Ines Lynce, Daniel Le Berre 
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
 */
package org.opensat.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import org.opensat.IFormula;
import org.opensat.IParser;
import org.opensat.ParseFormatException;
import org.opensat.data.ICNF;
import org.opensat.data.ILiteral;
import org.opensat.data.IVocabulary;

/**
 * Very simple Dimacs file parser.
 * 
 * @version 	1.0
 * @author		dlb
 * @author 		or
 */
public class Dimacs implements IParser {

	private int expectedNbOfClauses; // as announced on the p cnf line

	protected void skipComments(LineNumberReader in) throws IOException {
		int c;

		do {
			in.mark(4);
			c = in.read();
			if (c == 'c')
				in.readLine();
			else
				in.reset();
		} while (c == 'c');
	}

	protected void readProblemLine(LineNumberReader in, ICNF formula)
		throws IOException, ParseFormatException {

		// Reads the p cnf line
		String line = in.readLine();

		if (line == null)
			throw new ParseFormatException(
				"premature end of file : problem line expected (p cnf ...) on line "
					+ in.getLineNumber());

		StringTokenizer stk = new StringTokenizer(line);

		if (!(stk.hasMoreTokens()
			&& stk.nextToken().equals("p")
			&& stk.hasMoreTokens()
			&& stk.nextToken().equals("cnf")))
			throw new ParseFormatException(
				"problem line expected (p cnf ...) on line "
					+ in.getLineNumber());

		int vars;

		// reads the max var id
		vars = Integer.parseInt(stk.nextToken());
		assert vars > 0;

		// reads the number of clauses
		expectedNbOfClauses = Integer.parseInt(stk.nextToken());
		assert expectedNbOfClauses > 0;

		formula.setUniverse(vars, expectedNbOfClauses);
		assert formula.getVocabulary().getNumberOfVariables() == vars;
	}

	protected void readClauses(LineNumberReader in, ICNF formula)
		throws IOException, ParseFormatException {
		int lit, nbVars;
		String line;
		StringTokenizer stk;

		ILiteral literal;
		int realNbOfClauses = 0;
		
		IVocabulary voc=formula.getVocabulary();

		List literals = new ArrayList();

		nbVars = voc.getNumberOfVariables();
		assert nbVars != 0;

		while (true) {
			line = in.readLine();

			if (line == null) {
				// end of file
				if (!literals.isEmpty()) {
					// no 0 end the last clause
					formula.addClause(literals);
					realNbOfClauses++;
				}

				break;
			}

			stk = new StringTokenizer(line);

			while (stk.hasMoreTokens()) {
				lit = Integer.parseInt(stk.nextToken());

				if (Math.abs(lit) > nbVars) {
					throw new ParseFormatException(
						"var id greater than maxvarid on line "
							+ in.getLineNumber()
							+ "("
							+ lit
							+ ","
							+ nbVars
							+ ")");
				}

				if (lit != 0) {
					literal = voc.getLiteral(lit);
					literals.add(literal);
				} else {
					formula.addClause(literals);
					literals.clear();
					realNbOfClauses++;
				}
			}
		}
		if (expectedNbOfClauses != realNbOfClauses) {
			throw new ParseFormatException(
				"wrong nbclauses parameter. Found "
					+ realNbOfClauses
					+ ", "
					+ expectedNbOfClauses
					+ " expected");
		}
	}

	public void parseInstance(String filename, IFormula formula)
		throws FileNotFoundException, ParseFormatException, IOException {

		if (filename.endsWith(".gz"))
			parseInstance(
				new LineNumberReader(
					new InputStreamReader(
						new GZIPInputStream(new FileInputStream(filename)))),
				formula);
		else
			parseInstance(
				new LineNumberReader(new FileReader(filename)),
				formula);
	}

	public void parseInstance(LineNumberReader in, IFormula formula)
		throws ParseFormatException {

		if (!(formula instanceof ICNF)) {
			throw new UnsupportedOperationException();
		}

		ICNF cnf = (ICNF)formula;
		cnf.beginLoadFormula();
		try {
			skipComments(in);
			readProblemLine(in, cnf);
			readClauses(in, cnf);
		} catch (IOException e) {
			throw new ParseFormatException(e);
		} catch (NumberFormatException e) {
			throw new ParseFormatException(
				"integer value expected on line " + in.getLineNumber(),
				e);
		}

		cnf.endLoadFormula();
	}

}