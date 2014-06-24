package edu.ruc.IOS;

import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuffer;

/*
 * A node generated during searching DPLL solution tree.
 * */
public class DPNode {
	public static int UNSAT = -1;
	public static int SAT = 1;
	public static int NotKnown = 0;
	
	private static int AutoIncrementor = 0;
	
	private ArrayList<LiteralBinding> literalBindings;
	
	private int id;
	private int level;//the level this node located in
	private DPNode parent;
	private DPNode left_child;
	private DPNode right_child;
	private int status;// -1 UNSAT, 1 SAT, 0 NotKnown
	private int branch_lit_id; //the id of the literal according to which this node is generated  
	
	
	public DPNode(ArrayList<LiteralBinding> literalBindings, int branch_lit) {
		this.literalBindings =  literalBindings;
		branch_lit_id = branch_lit;
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
	
	/*return an auto incremented id*/
	public int getAutoID(){
		return DPNode.AutoIncrementor ++;		
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("id=");
		str.append(this.id);
		str.append("\n");		
		str.append("level=");
		str.append(this.level);
		str.append("\n");
		str.append("status=");
		str.append(this.status);
		str.append("\n");
		
		str.append("Attached Literal Bindings:\n");
		Iterator<LiteralBinding> it = this.getLBIterator();
		while(it.hasNext()){
			LiteralBinding lb = it.next();
			str.append(lb);
			str.append("\n");
		}

		return str.toString();
	}
	
	/*if a node's status is not UNKNOWN and has no children, 
	  it is a leaf node; otherwise, it is a non-leaf node
	*/
	public boolean isLeaf(){
		if (this.status != DPNode.NotKnown){
			if (this.left_child==null)
				if (this.right_child==null)
					return true;			
		}
		return false;
	}
	
	public Iterator<DPNode> getChildrenIterator(){
		ArrayList<DPNode> children = new ArrayList<DPNode>();
		if(this.left_child!=null)
			children.add(this.left_child);
		if (this.right_child!=null)
			children.add(this.right_child);
		
		return children.iterator();
	}
	
	
	/*getters and setters*/
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public int getBranchLitID(){
		return branch_lit_id;
	}
		 

}
