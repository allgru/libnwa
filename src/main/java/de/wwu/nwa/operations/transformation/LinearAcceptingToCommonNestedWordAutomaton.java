package de.wwu.nwa.operations.transformation;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunction;
import de.wwu.nwa.automata.items.transition.InternalTransitionFunction;
import de.wwu.nwa.automata.items.transition.ReturnTransitionFunction;

/**
 * Class for transforming linear-accepting automata to common nested-word automata (not implemented yet)
 */
public class LinearAcceptingToCommonNestedWordAutomaton {

    // TODO: implement in favour of new structure
    // TODO: test

    /**
     * transform linear-accepting automaton to common nested-word-automaton
     *
     * @param lanwa linear-accepting automaton to be transformed to common nested-word-automaton
     * @return new common nested-word-automaton created from nested word automata
     */
    public CommonNestedWordAutomaton transform(LinearAcceptingNestedWordAutomaton lanwa) {
        CommonNestedWordAutomaton dnwa = new CommonNestedWordAutomaton();
        dnwa.setAlphabet(lanwa.getAlphabet().clone());

        this.createLinearStates(lanwa, dnwa);
        this.createHierarchyStates(lanwa, dnwa);
        this.createCallTransitions(lanwa, dnwa);
        this.createReturnTransitions(lanwa, dnwa);
        this.createInternalTransitions(lanwa, dnwa);

        return dnwa;
    }

    /**
     * Create linear states from linear-accepting nested-word automata in deterministic nested-word automata
     *
     * @param lanwa linear-accepting nested word automata as source
     * @param dnwa  common nested-word-automata to be modified
     */
    public void createLinearStates(LinearAcceptingNestedWordAutomaton lanwa, CommonNestedWordAutomaton dnwa) {
        // Add all linear states to dnwa
        for (LinearState q : lanwa.getQ()
                ) {
/*            if (!((LinearAcceptingLinearState)q).getAcceptingEdge())
            {
                String name = q.getStateName().substring(1); // extract (
                name = name.substring(0,name.length()-3); // extract ,1)

                LinearState ls = new LinearState(name);
                dnwa.addStateToQ(ls);
            }*/
        }
        // add all accepting states to dnwa
        for (LinearState qf : lanwa.getQf()) {
            String name = qf.getStateName().substring(1); // extract (
            name = name.substring(0, name.length() - 3); // extract ,1)
            dnwa.addStateToQf(dnwa.getLinearStateByName(name));
        }
    }

    public void createHierarchyStates(LinearAcceptingNestedWordAutomaton lanwa, CommonNestedWordAutomaton dnwa) {


        // TODO: hier über Transitionen gehen
        // Add all linear states to dnwa
        for (HierarchyState p : lanwa.getP()
                ) {

            // if there is a call transition from state to accepting state then this is an accepting state

/*            if (!((LinearAcceptingHierarchyState) p).getAcceptingEdge()) {
                String name = p.getStateName().substring(1); // extract (
                name = name.substring(0, name.length() - 3); // extract ,1)

                HierarchyState hs = new HierarchyState(name);
                dnwa.addHierarchyStateToP(hs);
            }*/
        }

        // add all accepting states to dnwa, use call transitions
        for (CallTransitionFunction ctf : lanwa.getDeltaC().getDeltaC()) {
/*            if (((LinearAcceptingHierarchyState) ctf.getP()).getAcceptingEdge()) {
                String name = ctf.getP().getStateName().substring(1); // extract (
                name = name.substring(0, name.length() - 3); // extract ,1)

                dnwa.addHierarchyStateToPf(dnwa.getHierarchyStateByName(name));
            }*/
        }
    }

    public void createCallTransitions(LinearAcceptingNestedWordAutomaton lanwa, CommonNestedWordAutomaton dnwa) {
        for (CallTransitionFunction ctf : lanwa.getDeltaC().getDeltaC()
                ) {
/*            if (!((LinearAcceptingLinearState)ctf.getqSource()).getAcceptingEdge()
                    && !((LinearAcceptingLinearState)ctf.getqTarget()).getAcceptingEdge())
            {
                try {
                    dnwa.getDeltaC().addTransitionFunction(
                            dnwa.getLinearStateByName(getNameLinearAcceptingState(ctf.getqSource().getStateName())),
                            ctf.getSymbol(),
                            dnwa.getLinearStateByName(getNameLinearAcceptingState(ctf.getqTarget().getStateName())),
                            dnwa.getHierarchyStateByName(getNameLinearAcceptingState(ctf.getP().getStateName()))
                    );
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }
*/
        }

    }

    public void createReturnTransitions(LinearAcceptingNestedWordAutomaton lanwa, CommonNestedWordAutomaton dnwa) {
        for (ReturnTransitionFunction rtf : lanwa.getDeltaR().getDeltaR()
                ) {
/*            if (!((LinearAcceptingLinearState)rtf.getqSource()).getAcceptingEdge()
                    && !((LinearAcceptingLinearState)rtf.getqTarget()).getAcceptingEdge())
            {
                try {
                    dnwa.getDeltaR().addTransitionFunction(
                            dnwa.getLinearStateByName(getNameLinearAcceptingState(rtf.getqSource().getStateName())),
                            dnwa.getHierarchyStateByName(getNameLinearAcceptingState(rtf.getP().getStateName())),
                            rtf.getSymbol(),
                            dnwa.getLinearStateByName(getNameLinearAcceptingState(rtf.getqTarget().getStateName()))
                    );
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }
*/
        }

    }

    public void createInternalTransitions(LinearAcceptingNestedWordAutomaton lanwa, CommonNestedWordAutomaton dnwa) {
        for (InternalTransitionFunction itf : lanwa.getDeltaI().getDeltaI()
                ) {
/*            if (!((LinearAcceptingLinearState)itf.getqSource()).getAcceptingEdge()
                    && !((LinearAcceptingLinearState)itf.getqTarget()).getAcceptingEdge())
            {
                try {
                    dnwa.getDeltaI().addTransitionFunction(
                            dnwa.getLinearStateByName(getNameLinearAcceptingState(itf.getqSource().getStateName())),
                            itf.getSymbol(),
                            dnwa.getLinearStateByName(getNameLinearAcceptingState(itf.getqTarget().getStateName()))
                    );
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }
*/
        }

    }

    public String getNameLinearAcceptingState(String name) {
        name = name.substring(1); // extract (
        name = name.substring(0, name.length() - 3); // extract ,1)
        return name;
    }
}

