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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.opensat.data.AbstractClause;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.data.ILiteralIterator;

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
public class ClauseSimpleImplArray extends AbstractClause {

	class GenericLiteralIterator implements Iterator {

		private int pos = 0;

		public void clear() {
			pos = 0;
		}

		public boolean hasNext() {
			return pos < literals.length;
		}

		public Object next() {
			return literals[pos++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	class SpecificLiteralIterator implements ILiteralIterator {

		private int pos = 0;

		public void clear() {
			pos = 0;
		}

		public boolean hasNext() {
			return pos < literals.length;
		}

		public ILiteral next() {
			return literals[pos++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
    
	private static final ClauseSimpleImplArray TAUTOLOGY =
		new ClauseSimpleImplArray(new ArrayList());
	//private final ILiteralIterator iterator = new SpecificLiteralIterator();
	private final GenericLiteralIterator iterator =
		new GenericLiteralIterator();

	protected final ILiteral[] literals;

    /**
     * Constructor for ClauseSimpleImpl.
     */
    public ClauseSimpleImplArray(ILiteral [] lits, int size) {
        this(lits,size,false);
    }

    /**
     * Constructor for ClauseSimpleImpl.
     * The array of literals is supposed to be sorted!!!!
     */
    public ClauseSimpleImplArray(ILiteral [] lits, int size, boolean learned) {
        super(size,learned);
        literals = new ILiteral[size];
        System.arraycopy(lits,0,literals,0,size);
    }

	/**
	 * Constructor for ClauseSimpleImpl.
	 */
	public ClauseSimpleImplArray(List l) {
		this(l, new ILiteral[l.size()], false);
	}

	/**
	 * Constructor for ClauseSimpleImpl.
	 */
	public ClauseSimpleImplArray(List l, boolean learned) {
		this(l, new ILiteral[l.size()], learned);
	}

	protected ClauseSimpleImplArray(List l, ILiteral[] lits, boolean learned) {
		super(l.size(), learned);
		literals = (ILiteral[]) l.toArray(lits);
		Arrays.sort(literals);
	}

	/**
	 * @see org.opensat.IClause#contains(ILiteral)
	 */
	public boolean contains(ILiteral l) {
		return Arrays.binarySearch(literals, l) >= 0;
	}

	/**
	 * @see org.opensat.data.AbstractClause#createClause(java.util.List)
	 */
	protected IClause createClause(List l) {
		return new ClauseSimpleImplArray(l, true);
	}

	/**
	 * @see org.opensat.data.IClause#getLiteral(int)
	 */
	public ILiteral getLiteral(int i) {
		return literals[i];
	}

	/**
	 * @see org.opensat.data.IClause#getLiterals()
	 */
	public ILiteral[] getLiterals() {
		return literals;
	}

	/**
	 * @see org.opensat.data.IClause#getLiteralsSize()
	 */
	public int getLiteralsSize() {
		return literals.length;
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
		iterator.clear();
		return iterator; // literals.iterator();
	}
	
	/**
	 * @see org.opensat.data.IClause#originalSize()
	 */
	public int originalSize() {
		return literals.length;
	}

    /**
     * @see org.opensat.data.IClause#registerToLiterals()
     */
    public void registerToLiterals() {
        super.registerToLiterals();
		
		for(int i=0;i<literals.length;i++) {
			literals[i].watch(this);
		}

    }

}
