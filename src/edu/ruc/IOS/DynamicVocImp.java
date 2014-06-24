package edu.ruc.IOS;

/*
 * A dynamic implementation of org.opensat.data.IVocabulary
 * In our problem, a CNF will increase, so will do the corresponding vocabulary.
 * */

import org.opensat.data.IClause;
import org.opensat.data.IVocabulary;
import org.opensat.data.ILiteral;
import org.opensat.data.simple.LiteralSimpleImpl;
 

import java.util.ArrayList;
import java.util.Iterator;

public class DynamicVocImp implements IVocabulary{
	
	private ArrayList<ILiteral> listVariables; 
	private int maxVarID;
	private boolean frozen;
	private int varNum; 
	
	public DynamicVocImp() {
		clear();
	}

	public void clear() {
		listVariables = new ArrayList<ILiteral>();
		frozen = false;
	}
	
	protected ILiteral createLiteral(int i) {
		LiteralSimpleImpl lit = new LiteralSimpleImpl(i);		
		LiteralSimpleImpl opposite = new LiteralSimpleImpl(lit);
		return lit;
	}
	
	public void freeze(Iterator clausesIt) {
		frozen = true;
	}
	
	//the input i is literal's id
	public ILiteral getLiteral(int i) {

		maxVarID = Math.max(maxVarID, Math.abs(i));

		ILiteral lit=null;
		int id = Math.abs(i) - 1; // maps from 1..n to 0..n-1
		lit = getLitByID(i);
		if (lit == null) {
			if (frozen) {
				throw new UnsupportedOperationException("Not constructing formula");
			}
			lit =  createLiteral(id + 1);
			listVariables.add(lit);
		}
		
		if (i == lit.getId()) {
			return lit;
		} else {
			return lit.opposite();
		}
	}

	ILiteral getLitByID(int id){
		for(int i=0;i<this.listVariables.size();i++){
			if (listVariables.get(i).getId() == id){
				return listVariables.get(i);
			}
		}
		
		return null;
	}
	
	public int getMaxVariableId() {
		return maxVarID;
	}
	
	public int getNumberOfVariables() {
		return varNum;
	}
	
	public ILiteral[] getVariables() {
		return listVariables.toArray(new ILiteral[0]);
	}
	
	public void setUniverse(int nbvars) {
		listVariables = new ArrayList<ILiteral>(nbvars);
		maxVarID = nbvars;
		varNum = nbvars;
	}
	
	public Iterator variableIterator() {
		return listVariables.iterator();
	}
	

}
