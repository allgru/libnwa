package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;

public class ReturnTransitionRelations extends ReturnTransitionFunctions {
    ArrayList<LinearState> sourceStates;
    ArrayList<String> symbols;
    ArrayList<LinearState> targetStates;
    ArrayList<HierarchyState> hierarchyStates;


    public ReturnTransitionRelations(NestedWordAutomaton nwa) {
        super(nwa);
        sourceStates = new ArrayList<LinearState>();
        symbols = new ArrayList<String>();
        targetStates = new ArrayList<LinearState>();
        hierarchyStates = new ArrayList<HierarchyState>();
    }


    public void addTransitionFunction(LinearState sourceState, String symbol, LinearState targetState, HierarchyState hierarchyState) throws NestedWordSymbolAlreadyExistsException {

        this.recalculateMaxMinWordLength(symbol);

        // deltaR.add(new ReturnTransitionFunction(sourceState, nw, targetState, hierarchyState));
        sourceStates.add(sourceState);
        targetStates.add(targetState);
        symbols.add(symbol);
        hierarchyStates.add(hierarchyState);

    }

    public boolean transitionFulfilled(LinearState q, String symbol, HierarchyState p) {
        for (ReturnTransitionFunction rtf : this.deltaR) {
            if (rtf.stateFulfilled(q, p, symbol))
                return true;
        }
        return false;
    }

    // TODO: Acceptance, set of Accepted States
}
