package de.wwu.nwa.operations.transformation;


import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Determinise nondeterministic nested-word automata
 * Not yet implemented
 * <p>
 * Transform nondeterministic nested-word automata to deterministic automata
 *
 * @author Allan Grunert
 */
public class NondeterministicToDeterministicAutomaton {

    private Vector<LinearState> q;

    /**
     * Convert Nondeterministik Word automata to Determenistic Word Automata
     *
     * @param nnwa Nested Word Automata to be transformed into Deterministic Word Automata
     * @return Deterministic NWA that accepts all words the given Nondeterministic NWA accepts
     */
    public LinearAcceptingNestedWordAutomaton transform(NondeterministicNestedWordAutomaton nnwa) {
        LinearAcceptingNestedWordAutomaton dnwa = new LinearAcceptingNestedWordAutomaton();

        ArrayList<HierarchyState> nnwaHierarchyStates = nnwa.getP();
        ArrayList<HierarchyState> nnwaHierarchyAcceptingStates = nnwa.getPf();
        ArrayList<LinearState> nnwalinearStates = nnwa.getQ();
        ArrayList<LinearState> nnwaLinearAcceptingStates = nnwa.getQf();

       /* ArrayList<PowerSetHierarchyState> dnwaPowerSetHierarchyStates = new ArrayList<PowerSetHierarchyState>();
        ArrayList<PowerSetHierarchyState> dnwaPowerSetHierarchyAcceptingStates = new ArrayList<PowerSetHierarchyState>();
        ArrayList<PowerSetLinearState> dnwaPowerSetLinearStates = new ArrayList<PowerSetLinearState>();
        ArrayList<PowerSetLinearState> dnwaPowerSetLinearAcceptingStates = new ArrayList<PowerSetLinearState>();
*/
        for (HierarchyState hState : nnwaHierarchyStates) {

        }


        return dnwa;
    }
}
