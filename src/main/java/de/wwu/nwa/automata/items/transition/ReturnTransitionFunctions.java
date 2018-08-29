package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;

public class ReturnTransitionFunctions extends TransitionFunctions {
    protected ArrayList<ReturnTransitionFunction> deltaR;

    public ReturnTransitionFunctions(NestedWordAutomaton nwa) {
        super(nwa);
        deltaR = new ArrayList<ReturnTransitionFunction>();

    }

    /**
     * Add Transtion to list of functions
     *
     * @param sourceState    input linear state
     * @param hierarchyState input hierarchy state
     * @param symbol         input symbol
     * @param targetState    output liner state
     * @param name           name of funtion
     * @throws NestedWordSymbolAlreadyExistsException Exception if there already is a transition with same state using symbol
     */
    public void addTransitionFunction(LinearState sourceState, HierarchyState hierarchyState, String symbol, LinearState targetState, String name) throws NestedWordSymbolAlreadyExistsException {
        addTransitionFunction(sourceState, hierarchyState, symbol, targetState);

        this.deltaR.get(this.deltaR.size() - 1).setName(name);
    }

    /**
     * Add Transtion to list of functions
     *
     * @param sourceState    input linear state
     * @param hierarchyState input hierarchy state
     * @param symbol         input symbol
     * @param targetState    output liner state
     * @throws NestedWordSymbolAlreadyExistsException Exception if there already is a transition with same state using symbol
     */
    public void addTransitionFunction(LinearState sourceState, HierarchyState hierarchyState, String symbol, LinearState targetState) throws NestedWordSymbolAlreadyExistsException {

        if (this.transitionFulfilled(sourceState, symbol, hierarchyState)) {
            throw new NestedWordSymbolAlreadyExistsException();
        }

        this.recalculateMaxMinWordLength(symbol);

        deltaR.add(new ReturnTransitionFunction(sourceState, hierarchyState, symbol, targetState, nwa));

    }

    /**
     * Gives back if transition is fullfilled
     *
     * @param q      linear state as input
     * @param symbol input symbol
     * @param p      hierarchy state as input
     * @return true if transition is fullfilled
     */
    public boolean transitionFulfilled(LinearState q, String symbol, HierarchyState p) {
        for (ReturnTransitionFunction rtf : this.deltaR) {
            if (rtf.stateFulfilled(q, p, symbol))
                return true;
        }
        return false;
    }

    /**
     * give back linear state if transition is fullfiled
     *
     * @param q      input linear state
     * @param symbol input symbol
     * @param p      input hierarchy state
     * @return linear state as output of the funciton if input requirements are met
     */
    public LinearState transitionState(LinearState q, String symbol, HierarchyState p) {
        for (ReturnTransitionFunction rtf : this.deltaR) {
            if (rtf.stateFulfilled(q, p, symbol))
                return rtf.getFollowState(q, p, symbol);
        }
        return null;
    }

    /**
     * Go through all transitionfunctions and find transitionfunction which have sourceState as source
     *
     * @param sourceState State from which transition eminates
     * @return List of transitionfunction sharing same source State
     */
    public ArrayList<ReturnTransitionFunction> findReturnTransitionFunctions(LinearState sourceState) {
        ArrayList<ReturnTransitionFunction> rtf = new ArrayList<ReturnTransitionFunction>();

        for (ReturnTransitionFunction rfu : this.deltaR
                ) {
            if (rfu.getqSource().equals(sourceState)) {
                if (rtf.indexOf(rfu) == -1)
                    rtf.add(rfu);
            }

        }

        return rtf;
    }


    public ArrayList<ReturnTransitionFunction> getDeltaR() {
        return deltaR;
    }

    /**
     * Print information of function
     */
    public void printTransitions() {
        for (ReturnTransitionFunction rtf : this.deltaR
                ) {
            System.out.println("qsource: " + rtf.getqSource().getStateName());
            System.out.println("psource: " + rtf.getP().getStateName());
            System.out.println("symbol: " + rtf.getSymbol());
            System.out.println("qtarget " + rtf.getqTarget().getStateName());
        }
    }
}
