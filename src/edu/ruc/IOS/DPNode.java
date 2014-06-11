package edu.ruc.IOS;

import org.opensat.data.ICNF;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * A node generated during searching DPLL solution tree.
 * */
public class DPNode {
	public static int UNSAT = -1;
	public static int SAT = 1;
	public static int NotKnown = 0;
	
	
	private ICNF formula;//the cnf formula attached to this node;
	private int level;//the level this node located in
	private DPNode parent;
	private DPNode left_child;
	private DPNode right_child;
	private int status;// -1 UNSAT, 1 SAT, 0 NotKnown
	
	
	public DPNode(ICNF formula) {
		this.formula = formula;
	}


	/*
	 * Get the literal bindings at this node.
	 * 
	 * */
	public Iterator<LiteralBinding> getLBIterator(){
		//TODO
		return null;
	}
	
	public LiteralBinding getLBByID(int id){
		//TODO
		return null;
	}
	
	public void setLBByID(int id, LiteralBinding lb){
		//TODO
	}
	
	/*getters and setters*/
		
	public ICNF getFormula() {
		return formula;
	}


	public void setFormula(ICNF formula) {
		this.formula = formula;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public DPNode getParent() {
		return parent;
	}


	public void setParent(DPNode parent) {
		this.parent = parent;
	}


	public DPNode getLeft_child() {
		return left_child;
	}


	public void setLeft_child(DPNode left_child) {
		this.left_child = left_child;
	}


	public DPNode getRight_child() {
		return right_child;
	}


	public void setRight_child(DPNode right_child) {
		this.right_child = right_child;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	/*for unit testing*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
