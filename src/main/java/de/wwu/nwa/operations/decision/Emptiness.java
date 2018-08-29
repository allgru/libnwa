package de.wwu.nwa.operations.decision;

import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunction;
import de.wwu.nwa.automata.items.transition.InternalTransitionFunction;
import de.wwu.nwa.automata.items.transition.ReturnTransitionFunction;

import java.util.ArrayList;

/**
 * Verifies if L(A) with A as nested-word automaton has a empty set of nested-words
 * This class has not been implemented yet
 *
 * @author Allan Grunert
 */
public class Emptiness {

    private ArrayList<LinearState> lsList;

    public boolean Emptiness(LinearAcceptingNestedWordAutomaton lanwa) {
        lsList = new ArrayList<LinearState>();

        // if there are no accepting states there is no Nested-word that can be accepted
        if (lanwa.getQf().size() == 0)
            return true;

        return !recursiveStateCheck(lanwa, lanwa.getQ0(), 0);
    }

    /**
     * Go through all Transitions
     *
     * @param lnwa         linear-accepting Nested-word automata that should be checked
     * @param ls           linear state from which should be iterated backwards
     * @param pendingCalls number of pending calls
     * @return true, if start state is reachable
     */
    public boolean recursiveStateCheck(LinearAcceptingNestedWordAutomaton lnwa, LinearState ls, int pendingCalls) {
        boolean acceptingStateReached = false;
        lsList.add(ls);

        for (CallTransitionFunction ctf : lnwa.getDeltaC().findCallTransitionFunctions(ls)
                ) {
            boolean stateProcessed = false;
            for (LinearState ls2 : this.lsList
                    ) {
                if (ls2.getStateName().equals(ctf.getqTarget().getStateName())) {
                    stateProcessed = true;
                    break;
                }
            }

            if (!stateProcessed) {
                if (lnwa.isAcceptingLinearState(ctf.getqTarget()))
                    return true;
                else {
                    acceptingStateReached = acceptingStateReached || recursiveStateCheck(lnwa, ctf.getqTarget(), pendingCalls);
                    if (acceptingStateReached) return true;
                }
            }
        }

        for (ReturnTransitionFunction rtf : lnwa.getDeltaR().findReturnTransitionFunctions(ls)
                ) {
            boolean stateProcessed = false;
            for (LinearState ls2 : this.lsList
                    ) {
                if (ls2.getStateName().equals(rtf.getqTarget().getStateName())) {
                    stateProcessed = true;
                    break;
                }
            }

            if (!stateProcessed) {
                if (lnwa.isAcceptingLinearState(rtf.getqTarget()))
                    return true;
                else {
                    acceptingStateReached = acceptingStateReached || recursiveStateCheck(lnwa, rtf.getqTarget(), pendingCalls);
                    if (acceptingStateReached) return true;
                }
            }
        }

        for (InternalTransitionFunction itf : lnwa.getDeltaI().findInternalTransitionFunctions(ls)
                ) {
            boolean stateProcessed = false;
            for (LinearState ls2 : this.lsList
                    ) {
                if (ls2.getStateName().equals(itf.getqTarget().getStateName())) {
                    stateProcessed = true;
                    break;
                }
            }

            if (!stateProcessed) {
                if (lnwa.isAcceptingLinearState(itf.getqTarget()))
                    return true;
                else {
                    acceptingStateReached = acceptingStateReached || recursiveStateCheck(lnwa, itf.getqTarget(), pendingCalls);
                    if (acceptingStateReached) return true;
                }
            }
        }

        return acceptingStateReached;
    }
}
