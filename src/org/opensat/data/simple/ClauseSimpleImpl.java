/*
 * The OpenSAT project
 * Copyright (c) 2002, Joao Marques-Silva and Daniel Le Berre
 * 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Created on 26 nov. 2002
 * 
 */
package org.opensat.data.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opensat.data.AbstractClause;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * Implementation of a clause.
 * NOTE: 18/1/2003 Clauses created need now to be registered 
 * to the literals, it is no longer done automatically, in order to build
 * clauses that do not belong to the formula (reasons for instance).
 * Use registerToLiterals() for that purpose.
 * 
 * @author leberre
 *
 */
public class ClauseSimpleImpl extends AbstractClause {

	private static final ClauseSimpleImpl TAUTOLOGY = new ClauseSimpleImpl(new ArrayList());

	private final ArrayList literals;

    /**
     * Constructor for ClauseSimpleImpl.
     */
    public ClauseSimpleImpl(ILiteral [] lits, int size) {
        this(lits,size,false);
    }

    /**
     * Constructor for ClauseSimpleImpl.
     */
    public ClauseSimpleImpl(ILiteral [] lits, int size, boolean learned) {
        super(size,learned);
        literals = new ArrayList();
        for (int i=0;i<size;i++) {
            literals.add(lits[i]);
        }
    }

	/**
	 * Constructor for ClauseSimpleImpl.
	 */
	public ClauseSimpleImpl(List l) {
		this(l,false);
	}	
	
	/**
	 * Constructor for ClauseSimpleImpl.
	 */
	public ClauseSimpleImpl(List l,boolean learned) {
		super(l.size(),learned);
		literals = new ArrayList(l);
	}

	/**
	 * @see org.opensat.IClause#contains(ILiteral)
	 */
	public boolean contains(ILiteral l) {
		return literals.contains(l);
	}

    /**
	 * @see org.opensat.data.AbstractClause#createClause(java.util.List)
	 */
	protected IClause createClause(List l) {
        return   new ClauseSimpleImpl(l,true); 
    }


	/**
	 * @see org.opensat.data.IClause#getLiterals()
	 */
	public ILiteral [] getLiterals() {
		// FIXME (OR) : this is highly inefficient !
		// (allocation+copy)
		// this interface is not suited to ArrayList storage
		return (ILiteral [])literals.toArray(new ILiteral[literals.size()]);	
	}
	
	/**
	 * @see org.opensat.data.IClause#getLiteralsSize()
	 */
	public int getLiteralsSize() {
		return literals.size();
	}
	
	/**
	 * @see org.opensat.IClause#getTautology
	 */
	public IClause getTautology() {
			return TAUTOLOGY; 
	}
	
	/**
	 * @see org.opensat.IClause#literalIterator()
	 */
	public Iterator literalIterator() {
		return literals.iterator();
	}
	
	/**
	 * @see org.opensat.data.IClause#originalSize()
	 */
	public int originalSize() {
		return literals.size();
	}

	/**
	 * @see org.opensat.data.IClause#registerToLiterals()
	 */
	public void registerToLiterals() {
		super.registerToLiterals();
		// By default, watching all the literals.
		Iterator it = literalIterator();
		while (it.hasNext()) {
			ILiteral lit = (ILiteral)it.next();
			lit.watch(this);
		}
	}

}
