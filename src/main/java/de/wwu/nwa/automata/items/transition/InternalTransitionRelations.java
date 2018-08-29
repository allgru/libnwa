package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;

public class InternalTransitionRelations extends InternalTransitionFunctions {
    ArrayList<LinearState> sourceStates;
    ArrayList<String> symbols;
    ArrayList<LinearState> targetStates;

    public InternalTransitionRelations(NestedWordAutomaton nwa) {
        super(nwa);
        sourceStates = new ArrayList<LinearState>();
        symbols = new ArrayList<String>();
        targetStates = new ArrayList<LinearState>();
    }

    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState) throws NestedWordSymbolAlreadyExistsException {

        this.recalculateMaxMinWordLength(symbol);

        sourceStates.add(sourceState);
        targetStates.add(targetState);
        symbols.add(symbol);

        /// deltaI.add(new InternalTransitionFunction(sourceState, nw, targetState));

    }

    // TODO: Acceptance, set of Accepted States
}
