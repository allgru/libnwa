package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;

/**
 * Defines set of Callfunctions
 */
public class CallTransitionFunctions extends TransitionFunctions {
    protected ArrayList<CallTransitionFunction> deltaC;

    public CallTransitionFunctions(NestedWordAutomaton nwa) {
        super(nwa);
        deltaC = new ArrayList<CallTransitionFunction>();

    }


    /**
     * Add transition function to function list
     *
     * @param sourceState    input linear state
     * @param symbol         input symbol
     * @param targetState    output linear state
     * @param hierarchyState output hierchy state
     * @param name           name of function
     * @throws NestedWordSymbolAlreadyExistsException Exception if there already is a transition with same state using symbol
     */
    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState, HierarchyState hierarchyState, String name) throws NestedWordSymbolAlreadyExistsException {
        addTransitionFunction(sourceState, symbol, targetState, hierarchyState);
        deltaC.get(deltaC.size() - 1).setName(name);
    }

    /**
     * Add transition function to function list
     *
     * @param sourceState    input linear state
     * @param symbol         input symbol
     * @param targetState    output linear state
     * @param hierarchyState output hierchy state
     * @throws NestedWordSymbolAlreadyExistsException Exception if there already is a transition with same state using symbol
     */
    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState, HierarchyState hierarchyState) throws NestedWordSymbolAlreadyExistsException {
        if (this.transitionFulfilled(sourceState, symbol))
            throw new NestedWordSymbolAlreadyExistsException();

        this.recalculateMaxMinWordLength(symbol);

        deltaC.add(new CallTransitionFunction(sourceState, symbol, targetState, hierarchyState, nwa));

    }

    /**
     * Searches in list for call transition with given linear state
     *
     * @param sourceState linear state for which should be searched
     * @return List for functions containing the linear state as input parameter
     */
    public ArrayList<CallTransitionFunction> findCallTransitionFunctions(LinearState sourceState) {
        ArrayList<CallTransitionFunction> ctf = new ArrayList<CallTransitionFunction>();

        for (CallTransitionFunction cf : this.deltaC
                ) {
            if (cf.getqSource().getStateName().equals(sourceState.getStateName())) {
                if (ctf.indexOf(cf) < 0)
                    ctf.add(cf);
            }

        }

        return ctf;
    }


    /**
     * Checks if a function is fulfilled
     *
     * @param q      State which has to be given for transition
     * @param symbol Input word which has to be given for transition
     * @return true if a function is found which accepts both state and nested word
     */
    public boolean transitionFulfilled(LinearState q, String symbol) {
        for (CallTransitionFunction ctf : this.deltaC) {
            if (ctf.stateFulfilled(q, symbol))
                return true;
        }
        return false;
    }


    /**
     * Give back linear state if requirements of input of the function are met
     *
     * @param q      linear state as input
     * @param symbol input symbol
     * @return linear state if input requirements are met
     */
    public LinearState transitionState(LinearState q, String symbol) {
        for (CallTransitionFunction ctf : this.deltaC) {
            if (ctf.stateFulfilled(q, symbol))
                return ctf.getFollowState(q, symbol);
        }
        return null;
    }

    /**
     * Give back hierarchy state if requirements of input of the function are met
     *
     * @param q      linear state as input
     * @param symbol input symbol
     * @return hierarchy state if input requirements are met
     */
    public HierarchyState transitionHierarchyState(LinearState q, String symbol) {
        for (CallTransitionFunction ctf : this.deltaC) {
            if (ctf.stateFulfilled(q, symbol))
                return ctf.getFollowHierarchyState(q, symbol);
        }
        return null;
    }

    public ArrayList<CallTransitionFunction> getDeltaC() {
        return deltaC;
    }
}
