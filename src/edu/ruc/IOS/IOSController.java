package edu.ruc.IOS;

import java.util.ArrayList;
import java.util.Iterator;

import org.opensat.data.ICNF;
import org.opensat.data.ContradictionFoundException;
import org.opensat.algs.ISatHeuristic;
import org.opensat.heuristics.TwoSidedJW;
import org.opensat.data.ILiteral;
import org.opensat.data.simple.CNFSimpleImplAltWL;
import org.opensat.data.IVocabulary;
import org.opensat.data.IClause;

public class IOSController {
	
	DPTree kb_tree; 	
	ISatHeuristic heuristic; 
	
	public IOSController(ICNF KB) {
		heuristic = new TwoSidedJW();
		
		initialize(KB);		
		this.kb_tree.showTree();
	}
	
	/*
	 * construct kb_tree and do the first round search
	 * */
	private void initialize(ICNF KB){
		ArrayList<LiteralBinding> listLB = extractLiteralBindings(KB); 
		DPNode root = new DPNode(listLB, 0); // set branch_lit_id to zero for the root node 
		root.setId(root.getAutoID());
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
                DPNode v1 = new DPNode(listLB, lit.getId());
                v1.setId(v1.getAutoID());
                v1.setParent(v);
                v1.setLevel(v.getLevel()+1);
				v.setLeft_child(v1);
			  		//unit clause propagation always generate a left child
	             
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
			e.printStackTrace(System.out);
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
		result1 = searchChild(v,f,true, lit); 
		f.unpropagateAssignment(lit);
			//backtracking
 
		//try right child 
		f.propagateAssignment(lit.opposite());
		result2 = searchChild(v,f,false, lit.opposite());
		f.unpropagateAssignment(lit);
			//backtracking	
		return result1 || result2;
			//a node is SAT if either its left or right child is SAT
	}
	
	//generate and search the child of the node parent
	boolean searchChild(DPNode parent, ICNF f, boolean isLeftChild, ILiteral branch_lit){
		 ArrayList<LiteralBinding> listLB = extractLiteralBindings(f);
		 DPNode v = new DPNode(listLB, branch_lit.getId());
		 v.setId(v.getAutoID());
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
  		 else{
  			 v.setStatus(DPNode.UNSAT);
  		 }
  		 
  		 return result;
	}
	
	
	//record that the node is SAT and update the assignment list
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
	
	//only support CNFSimpleImplAltWL 
	ArrayList<LiteralBinding> extractLiteralBindings(ICNF formula){
		ArrayList<LiteralBinding> listLB = new ArrayList<LiteralBinding>();
		try{
			CNFSimpleImplAltWL f = (CNFSimpleImplAltWL)formula;
			int[] assign = f.getAllAssignments();
			for (int i=1;i<assign.length;i++){
				int id = i;
				if (i>assign.length/2) 
					id = assign.length/2 - i;
				
				/*if a LB's value is zero, we may change its value according to its opposite's value
				 * Note that a LB's value always equals to -1*its opposite's value
				 * */
				if (assign[i] == 0){
					assign[i] = -1* assign[(i + assign.length/2) % (assign.length-1)];
				}
				
				LiteralBinding lb = new LiteralBinding(id,assign[i]);
				listLB.add(lb);
			}
		}
		catch (Exception ex){
			ex.printStackTrace(System.out);
			return null;
		}		
		return listLB;
	}
	
	/*
	 * merge the new appended knowledge into the existing KB.
	 * 
	 * */
	void merge(ICNF new_klg){
		ICNF KB = this.kb_tree.getKBFormula();
		
		/*
		 * voc = KB.vocabulary;
		 * for each clause in new_klg
		 *   for each literal in this clause
		 *      newlit = voc.getLiteral(literal.getID);
		 *      listLiterals.add(newlit);
		 *   KB.addClause(listLiterals);
		 * */
		IVocabulary voc = KB.getVocabulary();
		Iterator cls_it = new_klg.activeClauseIterator();
		while(cls_it.hasNext()){
			IClause cls = (IClause)cls_it.next();
			Iterator lit_it = cls.literalIterator();
			ArrayList<ILiteral> listLiterals = new ArrayList<ILiteral>();
			while(lit_it.hasNext()){
				ILiteral old_lit = (ILiteral)lit_it.next();
				ILiteral new_lit = voc.getLiteral(old_lit.getId());
				listLiterals.add(new_lit);
			}
			KB.addClause(listLiterals);
		}		
	}
	
	
	/*
	 * starting from a node v, re-examine a new formula. 
	 * v locates in a DPTree built with previous KB. 
	 * formula is the the combination of the previous KB and new knowledge. 
	 * each node in the sub-tree rooted in v will be marked as SAT or UNSAT.
	 * new nodes may be also generated. 
	 * */
	
	boolean reExamine(DPNode v, ICNF formula){
		if (formula.hasNullClause()){
			v.setStatus(DPNode.UNSAT);
			return false;
		}
		
		if (v.isLeaf()){
			return propagateUnitClauses(formula,v);
		}
		else{			
			Iterator<DPNode> it = v.getChildrenIterator();
			boolean result = false;
			while(it.hasNext()){
				DPNode child = it.next();
				if(child.getStatus()!=DPNode.UNSAT){
					int branch_id = child.getBranchLitID();
					ILiteral lit = formula.getVocabulary().getLiteral(branch_id);
					formula.propagateAssignment(lit);
					boolean r = reExamine(child,formula);
					result = result || r;
					formula.unpropagateAssignment(lit);	
				}
			}
			
			return result;
		}
	
	}
	
	/*
	 * update the KB tree when new knowledge comes
	 * */
	public void update (ICNF new_knowledge){
		merge(new_knowledge);//add the new_knowledge into the existing KB
		DPNode root = this.kb_tree.getRoot();
		reExamine(root, this.kb_tree.getKBFormula());
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
