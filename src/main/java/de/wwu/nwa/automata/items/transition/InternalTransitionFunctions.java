package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;

public class InternalTransitionFunctions extends TransitionFunctions {
    protected ArrayList<InternalTransitionFunction> deltaI;

    public InternalTransitionFunctions(NestedWordAutomaton nwa) {
        super(nwa);
        deltaI = new ArrayList<InternalTransitionFunction>();

    }

    /**
     * Add new transition function to list of functions
     *
     * @param sourceState input state (linear)
     * @param symbol      input symbol
     * @param targetState output stat (linear)
     * @param name        name of function
     * @throws NestedWordSymbolAlreadyExistsException Exception if there already is a transition with same state using symbol
     */
    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState, String name) throws NestedWordSymbolAlreadyExistsException {
        addTransitionFunction(sourceState, symbol, targetState);

        this.deltaI.get(this.deltaI.size() - 1).setName(name);
    }

    /**
     * Add new transition function to list of functions
     *
     * @param sourceState input state (linear)
     * @param symbol      input symbol
     * @param targetState output stat (linear)
     * @throws NestedWordSymbolAlreadyExistsException Exception if there already is a transition with same state using symbol
     */
    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState) throws NestedWordSymbolAlreadyExistsException {

       /* if (this.transitionFulfilled(sourceState, symbol))
            throw new NestedWordSymbolAlreadyExistsException();*/

        this.recalculateMaxMinWordLength(symbol);

        deltaI.add(new InternalTransitionFunction(sourceState, symbol, targetState, nwa));

    }

    /**
     * Gives back if there is a function with input linear state x symbol that is met
     *
     * @param q      linear state for checking
     * @param symbol symbol
     * @return true if ther is a function that takes parameters as input
     */
    public boolean transitionFulfilled(LinearState q, String symbol) {
        for (InternalTransitionFunction itf : this.deltaI) {
            if (itf.stateFulfilled(q, symbol))
                return true;
        }
        return false;
    }

    /**
     * gives linear state back as output if input parameters are met
     *
     * @param q      linear state as input
     * @param symbol input symbol
     * @return linear state that is the output of function
     */
    public LinearState transitionState(LinearState q, String symbol) {
        for (InternalTransitionFunction itf : this.deltaI) {
            if (itf.stateFulfilled(q, symbol))
                return itf.getFollowState(q, symbol);
        }
        return null;
    }

    /**
     * Go through all transitionfunctions and find transitionfunction which have sourceState as source
     *
     * @param sourceState State from which transition eminates
     * @return List of transitionfunction sharing same source State
     */
    public ArrayList<InternalTransitionFunction> findInternalTransitionFunctions(LinearState sourceState) {
        ArrayList<InternalTransitionFunction> itf = new ArrayList<InternalTransitionFunction>();

        for (InternalTransitionFunction ifu : this.deltaI
                ) {
            if (ifu.getqSource().equals(sourceState)) {
                if (itf.indexOf(ifu) < 0)
                    itf.add(ifu);
            }

        }

        return itf;
    }


    public ArrayList<InternalTransitionFunction> getDeltaI() {
        return deltaI;
    }
}
