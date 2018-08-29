package de.wwu.nwa.operations.transformation;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunction;
import de.wwu.nwa.automata.items.transition.InternalTransitionFunction;
import de.wwu.nwa.automata.items.transition.ReturnTransitionFunction;

/**
 * Class for creating linear-accepting nested-word-automata of common nested-word-automata
 *
 * @author Allan Grunert
 */
public class CommonToLinearAcceptingNestedWordAutomaton {

    /**
     * Give back a linear-accepting nested-word-automata
     *
     * @param cnwa commpn nested word automaton to be created
     * @return new linear-accepting nested-word automaton
     */
    public LinearAcceptingNestedWordAutomaton transform(CommonNestedWordAutomaton cnwa) {
        LinearAcceptingNestedWordAutomaton lanwa = new LinearAcceptingNestedWordAutomaton();
        lanwa.setAlphabet(cnwa.getAlphabet().clone());

        addLinearStates(cnwa, lanwa);
        addHierarchyStates(cnwa, lanwa);
        addCallTransitions(cnwa, lanwa);
        addInternalTransitions(cnwa, lanwa);
        addReturnTransitions(cnwa, lanwa);

        return lanwa;
    }

    /**
     * add all linear states to linear accepting nested-word automata as cross product q x {0,1} and add all accepting
     * states q x {0}
     * <p>
     * O(n^2) as we search for state (getStateByName) by name in second loop iterating over all states
     *
     * @param cnwa  common nested-word-automaton of which the states should be generated
     * @param lanwa linear-accepting nested-word-automata the states are generated for
     */
    public void addLinearStates(CommonNestedWordAutomaton cnwa, LinearAcceptingNestedWordAutomaton lanwa) {
        // create cross product of q and (0,1) with 0 = accepting, 1=not accepting
        for (LinearState q : cnwa.getQ()
                ) {
            LinearState lals0 = new LinearState("(" + q.getStateName() + ",0)");
            lanwa.addStateToQ(lals0);
            LinearState lals1 = new LinearState("(" + q.getStateName() + ",1)");
            lanwa.addStateToQ(lals1);
        }

        for (LinearState qf : cnwa.getQf()
                ) {
            LinearState lals0 = lanwa.getLinearStateByName("(" + qf.getStateName() + ",0)");
            lanwa.addStateToQf(lals0);
        }

        LinearState q0 = lanwa.getLinearStateByName("(" + cnwa.getQ0().getStateName() + ",0)");
        lanwa.setQ0(q0);


    }

    /**
     * add all hierarchy states to automata as cross product p x {0,1}
     * <p>
     * O(n)
     *
     * @param cnwa  the common nested-word-automata the hierarchy states are created of
     * @param lanwa the linear nested-word-automata the hierarchy states are created for
     */
    public void addHierarchyStates(CommonNestedWordAutomaton cnwa, LinearAcceptingNestedWordAutomaton lanwa) {
        for (HierarchyState p : cnwa.getP()) {
            HierarchyState lahs0 = new HierarchyState("(" + p.getStateName() + ",0)");
            lanwa.addHierarchyStateToP(lahs0);
            HierarchyState lahs1 = new HierarchyState("(" + p.getStateName() + ",1)");
            lanwa.addHierarchyStateToP(lahs1);
        }

        lanwa.setP0(lanwa.getHierarchyStateByName("(" + cnwa.getP0().getStateName() + ",0)"));

    }

    /**
     * Add all call transitions to linear accepting nested-word automata
     * <p>
     * O(n^2) as we search (getStateByName) in loop for state with certain name iterating through all states
     * and check in the loop if the hierachical state is contained in the set of accepting hierachical states.
     *
     * @param cnwa  deterministic nested-word automata the call transitions are created for
     * @param lanwa linear-accepting nested-word automata the call transitions are created of
     */
    public void addCallTransitions(CommonNestedWordAutomaton cnwa, LinearAcceptingNestedWordAutomaton lanwa) {
        for (CallTransitionFunction ctf : cnwa.getDeltaC().getDeltaC()
                ) {
            if (cnwa.isAcceptingHierarchyState(ctf.getP())) {
                try { // if hierarchy state p is linear accepting then flag stays 0 if input state is (q,0)
                    lanwa.getDeltaC().addTransitionFunction(lanwa.getLinearStateByName("(" + ctf.getqSource().getStateName() + ",0)"),
                            ctf.getSymbol(), lanwa.getLinearStateByName("(" + ctf.getqTarget().getStateName() + ",0)"),
                            lanwa.getHierarchyStateByName("(" + ctf.getP().getStateName() + ",0)"),
                            "(" + ctf.getName() + ",0)");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            } else { // special case: as p is not in set of accepting hierarchy states this leads
                // to output states ((q,1)(p,1)), if input is (q,0),a
                try {
                    lanwa.getDeltaC().addTransitionFunction(lanwa.getLinearStateByName("(" + ctf.getqSource().getStateName() + ",0)"),
                            ctf.getSymbol(), lanwa.getLinearStateByName("(" + ctf.getqTarget().getStateName() + ",1)"),
                            lanwa.getHierarchyStateByName("(" + ctf.getP().getStateName() + ",1)"),
                            "(" + ctf.getName() + ",0)");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }
            try { // if there is flag 1 propagate it further
                lanwa.getDeltaC().addTransitionFunction(lanwa.getLinearStateByName("(" + ctf.getqSource().getStateName() + ",1)"),
                        ctf.getSymbol(), lanwa.getLinearStateByName("(" + ctf.getqTarget().getStateName() + ",1)"),
                        lanwa.getHierarchyStateByName("(" + ctf.getP().getStateName() + ",1)"),
                        "(" + ctf.getName() + ",1)");
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Add all return transitions to linear accepting nested-word-automata
     * <p>
     * O(n^2) as we search (getStateByName) in loop for state with certain name iterating through all states
     *
     * @param cnwa  deterministic nested-word automata the return transitions are created of
     * @param lanwa linear-accepting nested-word automata the return transitions are created for
     */
    public void addReturnTransitions(CommonNestedWordAutomaton cnwa, LinearAcceptingNestedWordAutomaton lanwa) {
        for (ReturnTransitionFunction rtf : cnwa.getDeltaR().getDeltaR()
                ) {
            try {
                lanwa.getDeltaR().addTransitionFunction(lanwa.getLinearStateByName("(" + rtf.getqSource().getStateName() + ",0)"),
                        lanwa.getHierarchyStateByName("(" + rtf.getP().getStateName() + ",0)"),
                        rtf.getSymbol(), lanwa.getLinearStateByName("(" + rtf.getqTarget().getStateName() + ",0)")
                        , "(" + rtf.getName() + ",0)"
                );
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

            try {
                lanwa.getDeltaR().addTransitionFunction(lanwa.getLinearStateByName("(" + rtf.getqSource().getStateName() + ",1)"),
                        lanwa.getHierarchyStateByName("(" + rtf.getP().getStateName() + ",1)"),
                        rtf.getSymbol(), lanwa.getLinearStateByName("(" + rtf.getqTarget().getStateName() + ",1)")
                        , "(" + rtf.getName() + ",1)"
                );
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Add all internal transitions to linear accepting nested-word automata
     * <p>
     * O(n^2) as we srarch (getStateByName) in loop for state with certain name iterating through all states
     *
     * @param cnwa  deterministic nested-word automata the internal transitions are created of
     * @param lanwa linear-accepting nested-word automata the internal transitions are created for
     */
    public void addInternalTransitions(CommonNestedWordAutomaton cnwa, LinearAcceptingNestedWordAutomaton lanwa) {
        for (InternalTransitionFunction itf : cnwa.getDeltaI().getDeltaI()
                ) {
            try {
                lanwa.getDeltaI().addTransitionFunction(lanwa.getLinearStateByName("(" + itf.getqSource().getStateName() + ",0)"),

                        itf.getSymbol(), lanwa.getLinearStateByName("(" + itf.getqTarget().getStateName() + ",0)")
                        , "(" + itf.getName() + ",0)"
                );
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

            try {
                lanwa.getDeltaI().addTransitionFunction(lanwa.getLinearStateByName("(" + itf.getqSource().getStateName() + ",1)"),

                        itf.getSymbol(), lanwa.getLinearStateByName("(" + itf.getqTarget().getStateName() + ",1)"),
                        "(" + itf.getName() + ",1)"
                );
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }
    }
}
