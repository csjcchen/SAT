package org.opensat.choicepoint;
import org.opensat.TimeoutException;
import org.opensat.algs.ISatHeuristic;
import org.opensat.algs.SatSolverAdapter;
import org.opensat.data.ContradictionFoundException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * @author artois
 *
 * This is a recursive version of the basic DPLL
 * original algorithm.
 */
public class DPLL_CP extends SatSolverAdapter {

	
	public DPLL_CP() {
		super();
	}

	public DPLL_CP(ISatHeuristic h) {
		super(h);
	}


	/**
	 * Unit propagation.
	 * 
	 * @param f
	 * @return boolean
	 */
	protected boolean unitPropagate(ICNF f) throws TimeoutException {
		if (f.hasNullClause()) {
			return false;
		}
		try {
			boolean result;
			if (f.hasUnitClause()) {
				ILiteral lit = f.nextUnitClause().firstUnassignedLiteral();
				f.propagateAssignment(lit);
				result = unitPropagate(f);
				f.unpropagateAssignment(lit);
			} else {
				result = propagatePureLiterals(f);
			}
			return result;
		} catch (ContradictionFoundException e) {
			return false;
		}
	}

	/**
	 * Method propagatePureLiterals.
	 * @param f
	 * @return boolean
	 */
	protected boolean propagatePureLiterals(ICNF f) throws TimeoutException {
		if (f.isSatisfied()) {
			return true;
		}

		// boolean result;
		//		Iterator it = f.pureLiteralIterator();
		//		if (it.hasNext()) {
		//			ILiteral lit = (ILiteral) it.next();
		//			f.propagateAssignment(lit);
		//			result = propagatePureLiterals(f);
		//			f.unpropagateAssignment(lit);
		//		} else {
		//			result = branch(f);
		//		}
		//		return result;
		return branch(f);
	}

	public boolean branch(ICNF f) throws TimeoutException {
		boolean result;
		ILiteral lit = heuristic.choose(f);
		assert lit!=null;
		f.propagateAssignment(lit);
		if (startSearch(f)) {
			f.unpropagateAssignment(lit);
			result = true;
		} else {
			f.unpropagateAssignment(lit);
			f.propagateAssignment(lit.opposite());
			result = startSearch(f);
			f.unpropagateAssignment(lit.opposite());
		}
		return result;
	}

	protected boolean startSearch(ICNF f) throws TimeoutException {
	
		/*
		unitSimplify(f);
		assignPureLiterals(f);
		*/
		
		IChoicePoint root=new BasicDPLLChoicePoint(heuristic);
		
		root.explore(); // recursive version
		
		if (stopASAP) {
				throw new TimeoutException();
		}
		return unitPropagate(f);
	}
	
	
	/**
	 * @see org.opensat.algs.SatSolverAdapter#fix(org.opensat.data.ICNF, org.opensat.data.ILiteral, org.opensat.data.IClause)
	 */
	public void fix(ICNF f, ILiteral l, IClause reason) {
		fix(f,l);
	}

	/**
	 * @see org.opensat.algs.SatSolverAdapter#fix(org.opensat.data.ICNF, org.opensat.data.ILiteral)
	 */
	public void fix(ICNF f, ILiteral l) {
		f.propagateAssignment(l);
	}

	/**
	 * @see org.opensat.algs.SatSolverAdapter#propagateUnitClauses(org.opensat.data.ICNF)
	 */
	public boolean propagateUnitClauses(ICNF f) throws TimeoutException {
		return unitPropagate(f);
	}

}
