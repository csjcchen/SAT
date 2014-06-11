package org.opensat.data;
import java.util.Iterator;
/**
 *	to iterate on active clauses only (i.e. the ones which are not satisfied)
 *  
 * @author or
 *
 */
public class ActiveClauseIterator implements Iterator {
	private Iterator i;
	private Object o;
	
	public ActiveClauseIterator(Iterator i) {
		this.i=i;
		fetchNext();
	}
	
	protected boolean isActive(Object o) {
		return 	!((IClause)o).isSatisfied();
	}
	
	private void fetchNext() {
		boolean hasnext;
		
		// get next active IClause from iterator i
		while ((hasnext=i.hasNext()) && !isActive(o=i.next()));
				
		if (!hasnext)
			o=null;	
	}
	
	public boolean hasNext() {
		return o!=null;
	}
	
	public Object next() {
		Object tmp=o;
		fetchNext();
		return tmp;
	}
	
	public void remove() {
		throw new UnsupportedOperationException("unimplemented");
	}
}