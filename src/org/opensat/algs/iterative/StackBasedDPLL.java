/*
 * The OpenSAT project
 * Copyright (c) 2002, Joao Marques-Silva and Daniel Le Berre
 * 
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Created on 29 nov. 2002
 * 
 */
package org.opensat.algs.iterative;

import org.opensat.TimeoutException;
import org.opensat.algs.Certificate;
import org.opensat.algs.DPLLStats;
import org.opensat.algs.ISatHeuristic;
import org.opensat.algs.SatSolverAdapter;
import org.opensat.data.ContradictionFoundException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.heuristics.Fixed;
import org.opensat.localprocessor.ILocalProcessor;
import org.opensat.localprocessor.LookAheadAval;
import org.opensat.localprocessor.PropagateEngine;

/**
 * This class is represents a standard DPLL based on a stack instead 
 * of two recursive calls. It should allows people to implement lookahead 
 * and non chronological backtracking.
 * 
 * @author leberre
 *
 */
public class StackBasedDPLL extends SatSolverAdapter {

	/* Luby's series taken from SATZ_rand maintained by Henry Kautz */
	public static long luby_super(long i) {
		assert i > 0;
		/* let 2^k be the least power of 2 >= (i+1) */
		int k = 1;
		long power = 2;
		while (power < (i + 1)) {
			k += 1;
			power = (power << 1); // power *=2;
		}
		if (power == (i + 1))
			return (power >> 1);
		return (luby_super(i - (power >> 1) + 1));
	}
	protected IBacktracker backtracker;

	protected ILiteral[] current;
	protected int decisionLevel;
	protected ILocalProcessor locproc;
	protected ILiteral[] next;

	/**
	 * Constructor for StackBasedDPLL.
	 */
	public StackBasedDPLL() {
		this(new ChronologicalBacktracking());
		//locproc = new LookAheadAval(this, new PropagateEngine(), null);
	}

	public StackBasedDPLL(IBacktracker back) {
		this(new Fixed(), null, back);
	}

	public StackBasedDPLL(ILocalProcessor l) {
		this(new Fixed(), l, new ChronologicalBacktracking());
	}

	public StackBasedDPLL(ISatHeuristic h) {
		this(h, null, new ChronologicalBacktracking());
		// locproc = new LookAhead(this, new PureLiteralsSimplification(this));
		// locproc = new PureLiteralsSimplification(this);		
		locproc = new LookAheadAval(this, new PropagateEngine(), null);
	}

	public StackBasedDPLL(ISatHeuristic h, ILocalProcessor l) {
		this(h, l, new ChronologicalBacktracking());
	}

	public StackBasedDPLL(ISatHeuristic h, ILocalProcessor l, IBacktracker back) {
		super(h);
		locproc = l;
		backtracker = back;
	}

	protected void backtrackAll(ICNF f) {
		while (decisionLevel >= 0) {
			unfix(f);
			current[decisionLevel] = null;
			next[decisionLevel] = null;
			decisionLevel--;
		}
		assert decisionLevel == -1;
		f.flush();
	}

	/**
	 * Method branch.
	 * @param f
	 * @param l
	 */
	protected void branch(ICNF f, ILiteral l) {
		branch(f, l, l.opposite(), null);
	}

	/**
	 * Method branch.
	 * @param f
	 * @param left
	 * @param right
	 * @param reason
	 */
	protected void branch(
		ICNF f,
		ILiteral left,
		ILiteral right,
		IClause reason) {
		assert left != null;
		current[++decisionLevel] = left;
		next[decisionLevel] = right;
		f.propagateAssignment(left);
		heuristic.onSatisfyingLiteral(f, left);
		left.setDecisionLevel(decisionLevel);
		left.setReason(reason);
	}

	/**
	 * Method change.
	 * @param f
	 */
	public void change(ICNF f, IClause reason) {
		current[decisionLevel] = next[decisionLevel];
		next[decisionLevel] = null;
		f.propagateAssignment(current[decisionLevel]);
		heuristic.onSatisfyingLiteral(f, current[decisionLevel]);
		current[decisionLevel].setDecisionLevel(decisionLevel);
		current[decisionLevel].setReason(reason);
	}

	public ILiteral current() {
		return current[decisionLevel];
	}

	/**
	 * Go one step down to the stack..
	 */
	public void down() {
		current[decisionLevel--] = null;
	}

	/**
	 * Method fix.
	 * @param f
	 * @param unit
	 */
	protected void fix(ICNF f, IClause unit) {
		assert unit.isUnit();
		ILiteral l = unit.firstUnassignedLiteral();
		//System.out.println("   Propagate " + l); 
		fix(f, l, unit);
	}

	/**
	 * @see org.opensat.SolverAdapter#fix(ICNF, ILiteral)
	 */
	public void fix(ICNF f, ILiteral l) {
		branch(f, l, null, null);
	}

	/**
	 * @see org.opensat.algs.SatSolverAdapter#fix(ICNF, ILiteral, IClause)
	 */
	public void fix(ICNF f, ILiteral l, IClause reason) {
		branch(f, l, null, reason);
	}
	/**
	 * Returns the decisionLevel.
	 * @return int
	 */
	public int getDecisionLevel() {
		return decisionLevel;
	}

	protected void initStacks(ICNF f) {
		decisionLevel = -1;
		int nbvar = f.getVocabulary().getNumberOfVariables();
		current = new ILiteral[nbvar];
		next = new ILiteral[nbvar];
	}

	public void learn(ICNF f, IClause cl) {
		if (cl.size() < 5) {
			f.learn(cl);
			statistics.newLearnedClause();
		}
		heuristic.onLearnedClause(f, cl);
	}

	/**
	 * CAUTION: This does not work with Pure Literals local processing.
	 * 
	 * @return boolean
	 */
	protected boolean mysteriousSimplification(ICNF f) {
		//		for (int i = 0; i < decisionLevel; i++) {
		//			if (!current[i].uniquelySatisfiesAClause()) {
		//				return true;
		//			}
		//		}
		return false;
	}

	public ILiteral next() {
		return next[decisionLevel];
	}

	public boolean propagateUnitClauses(ICNF f) {
		try {
			while (!f.hasNullClause() && f.hasUnitClause()) {
				IClause unit = f.nextUnitClause();
				statistics.newUnitClause();
				fix(f, unit);
			}
		} catch (ContradictionFoundException e) {
			// we stop unit propagation
			return false;
		}
		return (!f.hasNullClause());
	}

	/**
	 * @see org.opensat.algs.ISatSolver#setTimeout(int)
	 */
	public void setTimeout(int seconds) {
		timeout = seconds;
	}

	public void skip() {
		next[decisionLevel] = null;
		statistics.newSkipping();
	}

	public boolean startSearch(ICNF f) throws TimeoutException {
		initStacks(f);
		backtracker.init(f);

		int restart = 100;
		while (!stopASAP) {
			int conflicts = 0;
			assert decisionLevel == -1;
			while (!stopASAP && (conflicts < restart)) {
				propagateUnitClauses(f);
				if (f.hasNullClause()) {
					conflicts++;
					ILiteral l = backtracker.backtrack(this, f);
					if (l == null) {
						break;
					} else {
						//System.out.println("Now Assign " + l);
						continue;
					}
				}
				if (mysteriousSimplification(f)) {
					ILiteral l = backtracker.backtrack(this, f);
					if (l == null) {
						break;
					} else {
						statistics.newSkipping();
						continue;
					}
				}

				//			if (f.isSatisfied()) {
				//				storeCertificate(f);
				//				return true;
				//			}

				if (locproc != null) {
					if (locproc.process(f)) {
						continue;
					}
				}

				ILiteral lit = heuristic.choose(f);
				if (lit == null) {
					storeCertificate(f);
					backtrackAll(f);
					return true;
				}
				branch(f, lit);
				((DPLLStats) statistics).newDecision();
			}
			if (conflicts == restart) {
				backtrackAll(f);
				restart = restart * 15 / 10;
				((DPLLStats) statistics).newRestart();
			} else {
				break;
			}
		}
		if (stopASAP) {
			throw new TimeoutException();
		}
		return false;
	}

	protected void storeCertificate(ICNF f) {
		certificate = new Certificate(f.getVocabulary());
		for (int i = 0; i <= decisionLevel; i++) {
			certificate.add(current[i]);
		}
	}

	/**
	 * Method unfix.
	 * @param f
	 */
	public void unfix(ICNF f) {
		f.unpropagateAssignment(current[decisionLevel]);
		heuristic.onBacktrackingLiteral(f, current[decisionLevel]);
		current[decisionLevel].setDecisionLevel(-1);
		// current[decisionLevel].setReason(null);
	}

}
