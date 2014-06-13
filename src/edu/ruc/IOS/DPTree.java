package edu.ruc.IOS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.List;

import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/*
 * The tree of solving SAT with DPLL.
 * */
public class DPTree {
	
	private DPNode root;
	private Hashtable<Integer,Assignment> assign_list;
	private ICNF KBFormula;
		

	public DPTree(ICNF formula) {
		super();
		this.KBFormula = formula;
		this.root = null;
		this.assign_list = null;
	}
	
	
	/*build assignment list from the KBFormula*/
	public void initAssignmengList(){
		//TODO
	}
	
	/*clone KB to apply solver*/
	public ICNF cloneKB(){
		ICNF f =null;
		try{
			f = (ICNF)Class.forName(KBFormula.getClass().getName()).newInstance(); 
			f.beginLoadFormula();			
			f.setUniverse(this.KBFormula.getVocabulary().getNumberOfVariables(), KBFormula.size());
			
			Iterator<IClause> clauseIterator = KBFormula.activeClauseIterator();
				//TODO all clause iterator?
			while (clauseIterator.hasNext()){
				IClause cls = clauseIterator.next();
				List<ILiteral> listLiterals = new ArrayList<ILiteral>();
				ILiteral[] lits = cls.getLiterals(); 
				for (int i=0;i<lits.length;i++){
					listLiterals.add(lits[i]);
				}			
				f.addClause(listLiterals);
			}
		}
		catch (Exception ex){
			ex.printStackTrace(System.out);
			return null;
		}
		return f;
	}
	

	/*
	 * find a node which is the closest to v among all nodes whose statuses are NotKnown;  
	 * during this process, some nodes' status may be changed.
	 * e.g. all their children are SAT or NonSAT 
	 * */
	public DPNode getClosestNonknownNode(DPNode v){
		return null;
	}
	
	//The leftmost SAT node of a tree is the un-visited node v 
	//whose status is SAT and there are no nodes whose levels are lower 
	//than v¡¯s level or  located on v¡¯s left side.

	public DPNode getLeftMostNode(){
		//TODO
		return null;
	}
	
	public Assignment getAssignmentByID(int id){
		Integer ID = new Integer(id); 	
		return this.assign_list.get(ID);
	}	
	
	public void setAssignment(Assignment assign){
		Integer ID = new Integer(assign.getId()); 
		Assignment origin_assign = this.assign_list.get(ID);
		if (origin_assign!=null)
			assign.setStatus(origin_assign.getStatus());
		else
			System.out.println("ERR: cannot find the assignemtn with id = " + assign.getId());
	}
	
	
	public Iterator<Assignment> getAssignmentIterator(){
		return this.assign_list.values().iterator();
	}
	
	/*getters and setters*/

	public DPNode getRoot() {
		return root;
	}


	public void setRoot(DPNode root) {
		this.root = root;
	}

 
	public ICNF getKBFormula() {
		return KBFormula;
	}


	public void setKBFormula(ICNF kBFormula) {
		KBFormula = kBFormula;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
