package de.wwu.nwa.gui;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;

/**
 * Interface for specifying save operations
 *
 * @author Allan Grunert
 */
public interface SaveAutomata {
    void saveAutomata(CommonNestedWordAutomaton cnwa, String name);

    void saveAutomata(NondeterministicNestedWordAutomaton nnwa, String name);

    void saveAutomata(LinearAcceptingNestedWordAutomaton lanwa, String name);
}
