package edu.ruc.IOS;

/*
 * For each variable, we need to know whether its positive assignment (and negative assignment)
 * is satisfiable or unstatisfiable during searching the DPLL solution tree. 
 * An assignment is satisfiable if and only if there is at leat one DPNode where 
 * the variable get this assignment and this node is satisfied. 
 * An assignment is unsatisfiable if there are no such DPNodes where it can be satisfied. 
 * */
public class Assignment {

	public static int UNSAT = -1;
	public static int SAT = 1;
	public static int NotKnown = 0;
	
	private int id; //can be positive or negative. Equal to variable id * 1 or -1
	private Assignment opposite;//the opposite's id is equal to -this.id
	private int status;// -1 UNSAT, 1 SAT, 0 NotKnown
	
	
	public Assignment(int id) {
		this.id = id;
		this.status = NotKnown;
		this.opposite = null;
	}


	/*getters and setters*/
	public int getId(){
		return this.id;
	}	
	
	public Assignment getOpposite() {
		return opposite;
	}


	public void setOpposite(Assignment opposite) {
		this.opposite = opposite;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
