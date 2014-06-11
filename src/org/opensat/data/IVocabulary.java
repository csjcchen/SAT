package org.opensat.data;

import java.util.Iterator;

/**
 * @author roussel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface IVocabulary {
	/**
	 * Method setUniverse.
	 * @param nbvars the expected max variable id.
	 */
	void setUniverse(int nbvars);

	/**
	 * returns the real number of variables in the vocabulary.
	 * 
	 * @return int
	 * @see #getMaxVariableId()
	 */
	int getNumberOfVariables();

	/**
	 * @return the greatest id used for a variable (will be different 
	 * from the number of variables when some variables are not used)
	 */
	int getMaxVariableId();

	/**
	 * Get an iterator over the variables.
	 * 
	 * <p>For implementation of IVocabulary which store the variables in an array,
	 * it will be much faster to use getVariables() to iterate directly over 
	 * the array.
	 * 
	 * @return an iterator over the variables
	 * @see #getVariables()
	 */
	public Iterator variableIterator();
		
	/**
	 * Get the variables (i.e. the positive literals) in this vocabulary.
	 * 
	 * 
	 * <p>This is the most efficient way to iterate over the variables in this vocabulary
	 * provided the implementation is based on an array and we have direct access 
	 * to this array
	 * 
	 * <p>It should be used in this way :
	 * 
	 * <pre>
	 * ILiteral [] variables=theVocabulary.getVariables();
	 * 
	 * for(int i=0;i&lt;variables.length;i++)
	 *   foo(variables[i]);
	 * </pre>
	 * 
	 * <p> Each implementation must guarantee that no cell of the returned array contains null.
	 * 
	 * @return the variables (more precisely the positive literals) in the vocabulary 
	 */
	ILiteral [] getVariables();

	/**
	 * return a literal corresponding to a given DIMACS id
	 * @param id : id of the literal to obtain
	 * @return IQLiteral : corresponding literal
	 */
	ILiteral getLiteral(int id);

	/**
	 * erase all literals in this vocabulary
	 */
	void clear();
	
	/**
	 * freeze the vocabulary : no new literal can be created
	 */
	void freeze(Iterator clausesIt);
}