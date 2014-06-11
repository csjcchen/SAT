package edu.ruc.IOS;

import java.util.Iterator;
import org.opensat.data.ICNF;

public class IOSController {
	
	DPTree kb_tree; 	
	
	public IOSController(ICNF KB) {
		initialize(KB);
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
			int varID = chooseVariable(v);
			
			// left child
			DPNode l = new DPNode(v.getFormula());
			LiteralBinding lb1 = new LiteralBinding(varID, 1);
			LiteralBinding lb2 = new LiteralBinding(-1*varID, -1);
			l.setLBByID(varID, lb1);
			l.setLBByID(-1*varID, lb2);
				
			l.setParent(v);
			v.setLeft_child(l);
			
			//right child
			DPNode r =  new DPNode(v.getFormula());
			lb1 = new LiteralBinding(-1*varID, 1);
			lb2 = new LiteralBinding(varID, -1);
			r.setLBByID(-1*varID, lb1);
			r.setLBByID(varID, lb2);
			
			r.setParent(v);
			v.setRight_child(r);			
		}
	}
	
	/*
	 * choose a variable and branch the input node
	 * */
	int chooseVariable(DPNode v){
		//TODO
		return 0;
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
		//TODO
		return false;
	}
	
	boolean isUNSAT(DPNode v){
		//TODO
		return false;
	}
	
	
	public void update (ICNF new_knowledge){
		//TODO
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
