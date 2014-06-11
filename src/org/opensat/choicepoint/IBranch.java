/*
 * OpenSAT, the Open Satisfiability platform
 * Copyright (C) 2002 Joao Marques Silva, Ines Lynce, Daniel Le Berre 
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
 */
package org.opensat.choicepoint;

/**
 * @version 	1.0
 * @author	or
 * interface of a branch in a choice point 
 */
public interface IBranch {

//    /**
//     * we must know the choice point to which this branch belongs
//     */
//    IBranch(IChoicePoint cp);

    /**
     * return the choice point to which we belong
     */
    IChoicePoint getChoicePoint();

    /**
     * returns true iff this branch has been completely explored
     */
    boolean searchCompleted();

    /**
     * modify the state to start exploration of this branch
     *
     * For example, in a DP tree, interpret the literal held 
     * by this branch to true
     *
     * ??? Should this method go further and perform UP in such a case
     */
    void down();

    /**
     * backtrack : must restore state to what is was 
     * just before a call to down()
     *
     */
    void up();
	
	/**
	 * explore this branch
	 * @return true iff this branch is successfull
	 */
	boolean explore();
}
