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
 * Created on 26 nov. 2002
 * 
 */

package org.opensat.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.opensat.IFormula;
import org.opensat.IHeuristic;
import org.opensat.IParser;
import org.opensat.ISolver;
import org.opensat.ParseFormatException;
import org.opensat.TimeoutException;
import org.opensat.data.ICNF;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public abstract class AbstractLauncher {
	static final String TIMEOUT_SHORT_OPTION = "t";
	static final String TIMEOUT_LONG_OPTION = "timeout";
	static final String TIMEOUT_DESCRIPTION = "set the timeout";
	static final String SOLVER_SHORT_OPTION = "s";
	static final String SOLVER_LONG_OPTION = "solver";
	static final String SOLVER_DESCRIPTION = "solver fully qualified name";
	static final String HEURISTIC_SHORT_OPTION = "H";
	static final String HEURISTIC_LONG_OPTION = "heuristic";
	static final String HEURISTIC_DESCRIPTION =
		"heuristic fully qualified name";
	static final String HELP_SHORT_OPTION = "h";
	static final String HELP_LONG_OPTION = "help";
	static final String HELP_DESCRIPTION = "help/usage";
	static final String LOCAL_PROCESSING_SHORT_OPTION = "l";
	static final String LOCAL_PROCESSING_LONG_OPTION = "local-proc";
	static final String LOCAL_PROCESSING_DESCRIPTION =
		"local processing fully qualified name";
	static final String DS_SHORT_OPTION = "ds";
	static final String DS_LONG_OPTION = "data-structure";
	static final String DS_DESCRIPTION = " Underlying formula data structure";
	static final String PARSER_SHORT_OPTION = "p";
	static final String PARSER_LONG_OPTION = "parser";
	static final String PARSER_DESCRIPTION = " fully qualified parser name";
	static final String VERBOSE_SHORT_OPTION = "v";
	static final String VERBOSE_LONG_OPTION = "verbose";
	static final String VERBOSE_DESCRIPTION = " display solver statistics";

	final String DEFAULT_SOLVER_PACKAGE;
	final String DEFAULT_HEURISTIC_PACKAGE;

	final String projectname;

	private ISolver solver;
	private IFormula formula;
	private IParser parser;

	protected AbstractLauncher(
		String projectname,
		String solverpkg,
		String heurpkg) {
		this.projectname = projectname;
		DEFAULT_SOLVER_PACKAGE = solverpkg;
		DEFAULT_HEURISTIC_PACKAGE = heurpkg;
	}

	ISolver createSolverByName(String solvername) {
		assert solvername != null;
		ISolver solver = null;
		// if the solver name is not fully qualified, then solver from default package assumed.
		if (solvername.indexOf('.') == -1) {
			solvername = DEFAULT_SOLVER_PACKAGE + "." + solvername;
		}
		try {
			solver = (ISolver) Class.forName(solvername).newInstance();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
		return solver;
	}

	protected abstract ISolver createDefaultSolver();

	protected abstract IFormula createDefaultFormula();

	void setTimeout(int timeout) {
		solver.setTimeout(timeout);
	}

	void setHeuristic(String heurclassname) {
		try {
			solver.setHeuristic(
				(IHeuristic) Class.forName(heurclassname).newInstance());
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	}

	void setFormula(String classname) {
		try {
			formula = (IFormula) Class.forName(classname).newInstance();
			return;
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	}

	void setParser(String classname) {
		try {
			parser = (IParser) Class.forName(classname).newInstance();
			return;
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	}

	void printStats(int sat, int unsat, int aborted, long time) {
		System.out.println("Solved " + sat + " SAT instances");
		System.out.println("Solved " + unsat + " UNSAT instances");
		System.out.println("Aborted " + aborted + " instances");
		System.out.println("Total time: " + time + " ms.");
	}

	protected abstract IParser createParser();

	public void run(String[] args) {

		// Specifies command line options
		Options options = createOptions();

		// to display usage information         
		HelpFormatter formatter = new HelpFormatter();

		GnuParser cmdlineparser = new GnuParser();
		CommandLine cmd;
		try {
			// parse command line options
			cmd = cmdlineparser.parse(options, args);

			// set solver
			if (cmd.hasOption(SOLVER_SHORT_OPTION)) {
				String solvername = cmd.getOptionValue(SOLVER_SHORT_OPTION);
				if (solvername == null) {
					showAvailableSolvers();
					System.exit(0);
				}
				solver = createSolverByName(solvername);
			} else {
				// Default solver
				solver = createDefaultSolver();
			}

			// set timeout
			if (cmd.hasOption(TIMEOUT_SHORT_OPTION)) {
				setTimeout(
					Integer.parseInt(cmd.getOptionValue(TIMEOUT_SHORT_OPTION)));
			} else {
				// default timeout: 30 seconds
				setTimeout(30);
			}

			// set heuristic
			if (cmd.hasOption(HEURISTIC_SHORT_OPTION)) {
				String heurclassname =
					cmd.getOptionValue(HEURISTIC_SHORT_OPTION);
				setHeuristic(heurclassname);
			}

			// display usage
			if (cmd.hasOption(HELP_SHORT_OPTION)) {
				formatter.printHelp(projectname, options);
				System.exit(0);
			}

			// set formula
			if (cmd.hasOption(DS_SHORT_OPTION)) {
				String dsclassname = cmd.getOptionValue(DS_SHORT_OPTION);
				setFormula(dsclassname);
			} else {
				formula = createDefaultFormula();
			}

			// set parser
			if (cmd.hasOption(PARSER_SHORT_OPTION)) {
				String parserclassname = cmd.getOptionValue(PARSER_SHORT_OPTION);
				setParser(parserclassname);
			} else {
				parser = createParser();
			}

			boolean verbose = cmd.hasOption(VERBOSE_SHORT_OPTION);

			int sat = 0;
			int unsat = 0;
			int timeout = 0;
			long total = 0;

			String[] filenames = cmd.getArgs();

			for (int i = 0; i < filenames.length; i++) {
				System.out.print(filenames[i] + "\t");
				try {
					long begin = System.currentTimeMillis();
					parser.parseInstance(filenames[i], formula);
					if (verbose) {
						System.out.print(" nbv: "+
						((ICNF)formula).getVocabulary().getNumberOfVariables()+
						" nbc: "+((ICNF)formula).size()+
						" maxl: "+((ICNF)formula).maxLengthClause());
					}
					System.out.print(
						" read: "
							+ (System.currentTimeMillis() - begin)
							+ " ms ");
					try {
						boolean res = solver.solve(formula);
						if (res) {
							sat++;
							System.out.print("SAT");
						} else {
							unsat++;
							System.out.print("UNSAT");
						}
					} catch (TimeoutException e) {
						timeout++;
						System.out.print("TIMEOUT");
					}
					long time = System.currentTimeMillis() - begin;
					total += time;
					System.out.print(" Time (all): " + time + " ms ");
					if (verbose)
						System.out.println(solver.getStatistics());
						else
						System.out.println();
				} catch (FileNotFoundException e) {
					System.err.println(e);
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println(e);
					e.printStackTrace();
				} catch (ParseFormatException e) {
					System.err.println(e);
					e.printStackTrace();
				}
			}
			printStats(sat, unsat, timeout, total);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			formatter.printHelp(projectname, options);
		}

	}

	void showAvailableSolvers() {
		try {
			List list =
				SubClassFinder.find(DEFAULT_SOLVER_PACKAGE + ".ISatSolver");
			System.out.println("Available solvers: " + list);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			System.exit(0);
		}
	}

	public Options createOptions() {
		Options options = new Options();

		Option timeout =
			new Option(
				TIMEOUT_SHORT_OPTION,
				TIMEOUT_LONG_OPTION,
				true,
				TIMEOUT_DESCRIPTION);
		timeout.setArgName("time_in_s");
		options.addOption(timeout);

		// solver option		
		Option solver =
			new Option(
				SOLVER_SHORT_OPTION,
				SOLVER_LONG_OPTION,
				true,
				SOLVER_DESCRIPTION);
		solver.setArgName("solver_class_name");
		solver.setOptionalArg(true);
		options.addOption(solver);

		// heuristic option		
		Option heur =
			new Option(
				HEURISTIC_SHORT_OPTION,
				HEURISTIC_LONG_OPTION,
				true,
				HEURISTIC_DESCRIPTION);
		heur.setArgName("heuristic_class_name");
		heur.setOptionalArg(true);
		options.addOption(heur);

		// help option
		options.addOption(
			HELP_SHORT_OPTION,
			HELP_LONG_OPTION,
			false,
			HELP_DESCRIPTION);

		// local processing option
		Option locproc =
			new Option(
				LOCAL_PROCESSING_SHORT_OPTION,
				LOCAL_PROCESSING_LONG_OPTION,
				true,
				LOCAL_PROCESSING_DESCRIPTION);
		locproc.setArgName("local_proc_class_name");
		locproc.setOptionalArg(true);
		options.addOption(locproc);

		// data structure option
		Option ds =
			new Option(DS_SHORT_OPTION, DS_LONG_OPTION, true, DS_DESCRIPTION);
		locproc.setArgName("data_structure_class_name");
		locproc.setOptionalArg(true);
		options.addOption(ds);

		// parser option
		Option parser =
			new Option(
				PARSER_SHORT_OPTION,
				PARSER_LONG_OPTION,
				true,
				PARSER_DESCRIPTION);
		parser.setArgName("parser_class_name");
		parser.setOptionalArg(true);
		options.addOption(parser);

		// verbose option
		options.addOption(
			VERBOSE_SHORT_OPTION,
			VERBOSE_LONG_OPTION,
			false,
			VERBOSE_DESCRIPTION);

		return options;
	}
}
