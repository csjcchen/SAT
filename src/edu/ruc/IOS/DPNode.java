package edu.ruc.IOS;

import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/*
 * A node generated during searching DPLL solution tree.
 * */
public class DPNode {
	public static int UNSAT = -1;
	public static int SAT = 1;
	public static int NotKnown = 0;
	
	
	private ArrayList<LiteralBinding> literalBindings;
	
	private int level;//the level this node located in
	private DPNode parent;
	private DPNode left_child;
	private DPNode right_child;
	private int status;// -1 UNSAT, 1 SAT, 0 NotKnown
	
	
	
	public DPNode(ArrayList<LiteralBinding> literalBindings) {
		this.literalBindings =  literalBindings;
	}
	
	 /*
	 * Get the literal bindings at this node.
	 * 
	 * */
	public Iterator<LiteralBinding> getLBIterator(){
		return this.literalBindings.iterator();
	}
	
	public LiteralBinding getLBByID(int id){
		for(int i=0;i<this.literalBindings.size();i++){
			if (id == this.literalBindings.get(i).getId()){
				return this.literalBindings.get(i);
			}
		}
		return null;
	}
	
	public void setLBByID(int id, LiteralBinding lb){
		assert id == lb.getId();
		
		LiteralBinding origin_lb = getLBByID(id);
		origin_lb.setvalue(lb.getValue());
	}
	
	/*getters and setters*/
		
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
