package edu.ruc.IOS;

import java.util.ArrayList;
import java.util.Iterator;

import org.opensat.data.ICNF;
import org.opensat.data.ContradictionFoundException;
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
	
	/*
	 * construct kb_tree and do the first round search
	 * */
	void initialize(ICNF KB){
		ArrayList<LiteralBinding> listLB = extractLiteralBindings(KB); 
		DPNode root = new DPNode(listLB);
		root.setLevel(0);
		
		kb_tree = new DPTree(KB);
		kb_tree.setRoot(root);
		kb_tree.initAssignmengList();
		
		ICNF KBCopy = kb_tree.cloneKB();
			//get a copy of KB to start the solve process
		propagateUnitClauses(KBCopy, root);
			//traverse the tree in a depth-first way
	}
	
	boolean propagateUnitClauses(ICNF f, DPNode v){
		if (f.hasNullClause()) {
			v.setStatus(DPNode.UNSAT);
			return false;
		}
		try {
			boolean result;
			if (f.hasUnitClause()) {
				ILiteral lit = f.nextUnitClause().firstUnassignedLiteral();
				f.propagateAssignment(lit);
                heuristic.onSatisfyingLiteral(f,lit);
				//statistics.newUnitClause();
                
                //generate a new node
                ArrayList<LiteralBinding> listLB = extractLiteralBindings(f);
                DPNode v1 = new DPNode(listLB);
                v1.setParent(v);
                v1.setLevel(v.getLevel()+1);
				v.setLeft_child(v);
					//we assume unit clause propagation always generate a single left child
	             
                result = propagateUnitClauses(f,v1);
					//recursively perform unit propagation 
                
                f.unpropagateAssignment(lit);
                	//backtracking
                heuristic.onBacktrackingLiteral(f,lit);
				
                if (result) {
					/*assert certificate != null;
					certificate.add(lit);*/
                	recordSATNode(v1); 	
				}
                else{
                	v1.setStatus(DPNode.UNSAT);
                }
			}
			else {
				result = branch(f, v);
			}
			return result;
			
		} catch (ContradictionFoundException e) {
			return false;
		}
		
		 
	}
	
	boolean branch(ICNF f, DPNode v){
		boolean result1,result2;
		//statistics.newDecision();
		ILiteral lit = heuristic.choose(f);
		// assert lit != null;
		if (lit==null) {
			/*assert f.isSatisfied();
			certificate = new Certificate(f.getVocabulary());*/
			//v is satisfied since no clauses remain
			recordSATNode(v);
			return true;
		}
		
		//try left child
		f.propagateAssignment(lit);
		result1 = searchChild(v,f,true); 
		f.unpropagateAssignment(lit);
			//backtracking
 
		//try right child 
		f.propagateAssignment(lit.opposite());
		result2 = searchChild(v,f,false);
		f.unpropagateAssignment(lit);
			//backtracking	
		return result1 || result2;
			//a node is SAT if either its left or right child is SAT
	}
	
	//generate and search the child of the node parent
	boolean searchChild(DPNode parent, ICNF f, boolean isLeftChild){
		 ArrayList<LiteralBinding> listLB = extractLiteralBindings(f);
		 DPNode v = new DPNode(listLB);
		 v.setParent(parent);
	     v.setLevel(parent.getLevel()+1);
		 if (isLeftChild){
			 parent.setLeft_child(v);}
		 else{
			 parent.setRight_child(v);
		 }
			
		 boolean result = propagateUnitClauses(f,v);
		 	//recursively perform unit propagation
		
  		 if (result) {
				/*assert certificate != null;
				certificate.add(lit);*/
  			 recordSATNode(v); 
		 }
  		 
  		 return result;
	}
	
	
	//record that the node is SAT
	void recordSATNode(DPNode v){
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
		return false;
		//return v.getFormula().isSatisfied();
	}
	
	boolean isUNSAT(DPNode v){
		return false;
		/*if (v.getFormula().hasNullClause()){
			return true;
		}
		else
			return false;*/
	}
	
	ArrayList<LiteralBinding> extractLiteralBindings(ICNF formula){
		return null;
	}
	
	public void update (ICNF new_knowledge){
		//TODO
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	/*
	 * evaluate the status of the input node; update its attached assignments if necessary;
	 * and generate the children of this node if possible. 
	 * */
	//void evaluate (ICNF formula){
		/*if (isSAT(v)){
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
		}*/
	//}
	

}
