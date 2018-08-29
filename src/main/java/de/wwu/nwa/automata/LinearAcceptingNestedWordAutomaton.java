package de.wwu.nwa.automata;

import de.wwu.nwa.automata.items.state.HierarchyState;

import java.util.ArrayList;

/**
 * Class that represents a linear-accepting Nested-word automaton
 * All pending calls are accepted (i.e. the set of hierarchical states are all accepted)
 *
 * @author Allan Grunert
 */
public class LinearAcceptingNestedWordAutomaton extends CommonNestedWordAutomaton {

    public LinearAcceptingNestedWordAutomaton() {
        super();
    }

    /**
     * add hierarchical state to set of hierarchical states an set of accepting hierarchical states
     * as all pending calls are accepting, the propagated states have to all be acceping
     *
     * @param p hierarchical state that is added to the automatas set of hierarchical and accepting hierarchical states
     */
    @Override
    public void addHierarchyStateToP(HierarchyState p) {
        // super().addHierarchyStateToP(p);
        // super.addHierarchyStateToPf(p); // Hierarchy states are automatically accepted
        this.P.add(p);
        if (!this.P.equals(this.Pf))
            this.Pf.add(p);
    }

    /**
     * accepting hierarchical states have not got to be added in this automata
     *
     * @param p hierarchical state (dummy)
     */
    @Override
    public void addHierarchyStateToPf(HierarchyState p) {
        // All hierarchy states are automatically accepted, so no need for adding them here
    }

    /**
     * Remove hierarchy state from set of hierarchy states from automata
     *
     * @param pSrc hirtstchy state which should be removed from automata
     */
    @Override
    public void removeHierarchyStateFromP(HierarchyState pSrc) {
        super.removeHierarchyStateFromP(pSrc);
        if (!this.P.equals(this.Pf))
            super.removeHierarchyStateFromPf(pSrc);
    }

    /**
     * Remove hierarchy state from set of accepting pending hierarchy states from automata
     *
     * @param pSrc state that should be deleted
     */
    @Override
    public void removeHierarchyStateFromPf(HierarchyState pSrc) {
    }

    @Override
    public void setPf(ArrayList pf) {
    }

    @Override
    public void setP(ArrayList<HierarchyState> p) {
        P = p;
        Pf = p;
    }

}
