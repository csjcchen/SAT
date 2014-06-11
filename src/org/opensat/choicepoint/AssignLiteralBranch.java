package org.opensat.choicepoint;

import org.opensat.data.ILiteral;

/**
 * @author or
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AssignLiteralBranch implements IBranch {

	private ILiteral l;
	private IChoicePoint nextCP;
	private boolean success; // ??? move up in an abstract class

	/**
	 * Constructor AssignLiteralBranch.
	 * @param l
	 */
	public AssignLiteralBranch(ILiteral l) {
		this.l = l;
	}

	/**
	 * @see IBranch#getChoicePoint()
	 */
	public IChoicePoint getChoicePoint() {
		return null;
	}

	/**
	 * @see IBranch#searchCompleted()
	 */
	public boolean searchCompleted() {
		return false;
	}

	/**
	 * @see IBranch#down()
	 */
	public void down() {
	}

	/**
	 * @see IBranch#up()
	 */
	public void up() {
	}

	/**
	 * @see IBranch#explore()
	 */
	public boolean explore() {
		try {
			down();
		
			//nextCP=mkCP();
			
			success=true;
			
		} catch (Exception
		//UNSATException 
		e) {
			success=false;
		}

		up();		

		return success;
	}
	

}
