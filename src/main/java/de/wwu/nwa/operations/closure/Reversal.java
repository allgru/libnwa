package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.state.LinearState;

/**
 * Create an Automata which is able to read the reverse Nested-word of a given Nested-word automaton
 * <p>
 * This class has not been implemented yet
 *
 * @author Allan Grunert
 */
public class Reversal {

    public static CommonNestedWordAutomaton Reversal(CommonNestedWordAutomaton dnwaa) {
        CommonNestedWordAutomaton dnwab = new CommonNestedWordAutomaton();

        LinearState acceptingState = new LinearState("accepting");

        // Alle Zustände durchgehen
        // Kompletes Alphabet durchgehen
        for (String symbol : dnwaa.getAlphabet().getAlphabet(Alphabet.INTERNAL_POSITION)
                ) {
            for (LinearState linearState : dnwaa.getQ()
                    ) {
                // go through all Linear States
            }

        }

        // all Call Transition become Return transitions
        // Wenn Keine Transition für das Alphabet dann hinzufügen Internal auf einen einzelnen Akzeptierenden Zustand

        return dnwab;
    }

}
