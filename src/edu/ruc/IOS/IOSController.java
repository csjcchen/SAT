package edu.ruc.IOS;

import java.util.Iterator;
import org.opensat.data.ICNF;
import org.opensat.algs.ISatHeuristic;
import org.opensat.heuristics.TwoSidedJW;
import org.opensat.data.ILiteral;

public class IOSController {
	
	DPTree kb_tree; 	
	ISatHeuristic heuristic; 
	
	public IOSController(ICNF KB) {
		initialize(KB);
		heuristic = new TwoSidedJW();
			//TODO need to learn how to use heuristic
	}
	
	public ICNF cloneKB(){		
		return this.kb_tree.getRoot().cloneAttachedFormula();
	}

	/*
	 * construct kb_tree and do the first round search
	 * */
	void initialize(ICNF KB){
		DPNode root = new DPNode(KB);
		kb_tree = new DPTree(KB);
		kb_tree.setRoot(root);
		kb_tree.initAssignmengList();//this call will initialize the assignment list
		
		DPNode v = root;
		while (!terminate() && v!=null){
			evaluate(v);//new nodes will be inserted in this step
			if (v.getStatus()== DPNode.NotKnown){
				v = v.getLeft_child();
			}
			else{				
				DPNode u = this.kb_tree.getClosestNonknownNode(v);
				v = u;
			}
		}
	}
	
	/*
	 * evaluate the status of the input node; update its attached assignments if necessary;
	 * and generate the children of this node if possible. 
	 * */
	void evaluate (DPNode v){
		if (isSAT(v)){
			v.setStatus(DPNode.SAT);
			//set the statuses of all un-fixed variables' assignments at v as S
		    Iterator<LiteralBinding> iter = v.getLBIterator();
		    while(iter.hasNext()){
		    	LiteralBinding lb = iter.next();
		    	if (lb.getValue()!=0){
		    		//if this literal gets 1 or -1 
		    		Assignment assign = this.kb_tree.getAssignmentByID(lb.getId());
		    		assign.setStatus(Assignment.SAT);
		    	}
		    }		 
			 
		}
		else if (isUNSAT(v)){
			v.setStatus(DPNode.UNSAT);
		}
		else{
			//generate the left- and right- child of the current node 
			ILiteral lit = heuristic.choose(v.getFormula()); 

			// left child
			DPNode l = new DPNode(v.getFormula());
			LiteralBinding lb1 = new LiteralBinding(lit.getId(), 1);
			LiteralBinding lb2 = new LiteralBinding(-1*lit.getId(), -1);
			l.setLBByID(lit.getId(), lb1);
			l.setLBByID(-1*lit.getId(), lb2);
				
			l.setParent(v);
			v.setLeft_child(l);
			
			//right child
			DPNode r =  new DPNode(v.getFormula());
			lb1 = new LiteralBinding(-1*lit.getId(), 1);
			lb2 = new LiteralBinding(lit.getId(), -1);
			r.setLBByID(-1*lit.getId(), lb1);
			r.setLBByID(lit.getId(), lb2);
			
			r.setParent(v);
			v.setRight_child(r);			
		}
	}
	
 	
	/*
	 * to test whether the search process can be terminated
	 * */
	boolean terminate(){
		/*
		 * If there are no assignments whose statuses are N
        	return 1;
    	   Else
        	return 0;
		 * */

		boolean result  = true;
		Iterator<Assignment> iter  = this.kb_tree.getAssignmentIterator();
		 
		while(iter.hasNext()){
			Assignment assign = iter.next();
			if (assign.getStatus()==Assignment.NotKnown){
				result = false;
				break;
			}
		}
	
		return result;
	}
	
	boolean isSAT(DPNode v){
		return v.getFormula().isSatisfied();
	}
	
	boolean isUNSAT(DPNode v){
		if (v.getFormula().hasNullClause()){
			return true;
		}
		else
			return false;
	}
	
	
	public void update (ICNF new_knowledge){
		//TODO
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
