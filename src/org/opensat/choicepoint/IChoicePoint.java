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

import java.util.Iterator;

/**
 * @version 1.0
 * @author  or
 * interface of generic choice points
 */
public interface IChoicePoint {

//    /**
//     * create a new choice point and attach it to branch
//     */
//    IChoicePoint(IBranch branch);

    /**
     * To get the parent of this choice point in the search tree
     */
    IChoicePoint getParent();
    
    /**
     * returns true iff every branch of this choice point has been
     * completely explored
     */
    boolean searchCompleted();
    
    /**
     * get the branch which is currently explored
     * returns null is this choice point is not active
     */
    IBranch getCurrentBranch();


    /**
     * return a list (?) of branches which are already explored
     * DLB : provide an iterator over branches already explored?
     */
    Iterator getCompletedBranches();


    /**
     * return a list (?) of branches which must still be explored
     * DLB : provide an iterator over branches which must still be explored
     */
    Iterator getUncompletedBranches();


    /**
     *
     */
    void firstBranch();

    /**
     * select next branch as the current branch
     *
     * @exception noBranchLeft
     */
    IBranch nextBranch();

    /**
     * return true iff nextBranch() will succeed
     */
     boolean hasMoreBranches();

    /**
     * explore the branches of this choicepoint
     * 
     * @return true iff if one branch was successful
     */
    boolean explore();
}
