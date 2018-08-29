package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;

public class CallTransitionRelations extends CallTransitionFunctions {

    ArrayList<LinearState> sourceStates;
    ArrayList<String> inputWords;
    ArrayList<LinearState> targetStates;
    ArrayList<HierarchyState> hierarchyStates;

    public CallTransitionRelations(NestedWordAutomaton nwa) {
        super(nwa);
        sourceStates = new ArrayList<LinearState>();
        inputWords = new ArrayList<String>();
        targetStates = new ArrayList<LinearState>();
        hierarchyStates = new ArrayList<HierarchyState>();
    }

    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState, HierarchyState hierarchyState) throws NestedWordSymbolAlreadyExistsException {

        this.recalculateMaxMinWordLength(symbol);

        // deltaC.add(new CallTransitionFunction(sourceState, nw, targetState, hierarchyState));
        sourceStates.add(sourceState);
        targetStates.add(targetState);
        inputWords.add(symbol);
        hierarchyStates.add(hierarchyState);

    }

    // TODO: Acceptance, set of Accepted States

}
