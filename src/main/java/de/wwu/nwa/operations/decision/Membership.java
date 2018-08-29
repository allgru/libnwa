package de.wwu.nwa.operations.decision;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.NestedWord;
import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.exceptions.NestedEdgeSharePositionException;
import de.wwu.nwa.automata.items.exceptions.NestedEdgesCrossedException;

/**
 * Tests if Nested-word is accepted by Nested-word-automata
 * n in L(A)?
 *
 * @author Allan Grunert
 */
public class Membership {

    /**
     * Test if word is accepted by Nested-word automata
     *
     * @param nwa Nested-word automata for testing for accepting a word
     * @param nw  Nested-word for testing for acceptance
     * @return true if Nested-word is accepted by automata
     */
    public boolean Membership(NestedWordAutomaton nwa, NestedWord nw) {
        if (nwa.getClass().equals(CommonNestedWordAutomaton.class) || nwa.getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
            Run run = new Run(nw);
            run.setNwa(nwa);
            try {
                return (run.getInputWordAccepted());
            } catch (NestedEdgeSharePositionException e) {
                e.printStackTrace();
            } catch (NestedEdgesCrossedException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

}
