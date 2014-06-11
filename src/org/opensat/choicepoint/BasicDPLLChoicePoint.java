package org.opensat.choicepoint;

import java.util.Iterator;

import org.opensat.algs.ISatHeuristic;
import org.opensat.data.ICNF;
import org.opensat.data.ILiteral;

/**
 * @author or
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BasicDPLLChoicePoint implements IChoicePoint {
	/**
	 * Constructor BasicDPLLChoicePoint.
	 * @param heuristic
	 */
	public BasicDPLLChoicePoint(ISatHeuristic heuristic) {
	}

	private AssignLiteralBranch branches[]=new AssignLiteralBranch[2];
	private int currentBranch=-1;
	

	/**
	 * Constructor for BasicDPLLChoicePoint.
	 */
	public BasicDPLLChoicePoint(ISatHeuristic h, ICNF f) {
		ILiteral l = h.choose(f);
		branches[0] = new AssignLiteralBranch(l);
		branches[1] = new AssignLiteralBranch(l.opposite());
	}

	/**
	 * @see IChoicePoint#getParent()
	 */
	public IChoicePoint getParent() {
		return null;
	}

	/**
	 * @see IChoicePoint#searchCompleted()
	 */
	public boolean searchCompleted() {
		return false;
	}

	/**
	 * @see IChoicePoint#getCurrentBranch()
	 */
	public IBranch getCurrentBranch() {
		return null;
	}

	/**
	 * @see IChoicePoint#getCompletedBranches()
	 */
	public Iterator getCompletedBranches() {
		return null;
	}

	/**
	 * @see IChoicePoint#getUncompletedBranches()
	 */
	public Iterator getUncompletedBranches() {
		return null;
	}

	/**
	 * @see IChoicePoint#firstBranch()
	 */
	public void firstBranch() {
		currentBranch=-1;
	}

	/**
	 * @see IChoicePoint#nextBranch()
	 */
	public IBranch nextBranch() {
		currentBranch++;
		return branches[currentBranch];
	}

	/**
	 * @see IChoicePoint#hasMoreBranches()
	 */
	public boolean hasMoreBranches() {
		return currentBranch<1;
	}

	/**
	 * @see IChoicePoint#explore()
	 */
	public boolean explore() {
		firstBranch();
		while (hasMoreBranches())
			if (nextBranch().explore())
				return true;
		
		return false;
	}

}
