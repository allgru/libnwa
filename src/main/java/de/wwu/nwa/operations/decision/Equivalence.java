package de.wwu.nwa.operations.decision;

import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.operations.closure.Complement;
import de.wwu.nwa.operations.closure.Intersection;
import de.wwu.nwa.operations.closure.Union;

/**
 * Tests the equivalence of two nested-word automata
 *
 * @author Allan Grunert
 */
public class Equivalence {

    /**
     * Gives back if L(A) is equivalent to L(B)
     *
     * @param lanwa1 Automata A for testing if language of this Nested-Word Automata is equivalent to the other one
     * @param lanwa2 Automata B for testing if language of this Nested-Word Automata is equivalent to the other one
     * @return true if languages are equivalent, else false
     */
    public boolean Equivalence(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2) {
        Union union = new Union();
        Intersection intersection = new Intersection();
        Complement complement = new Complement();
        Emptiness emptiness = new Emptiness();

        return emptiness.Emptiness(union.Union(intersection.Intersection(lanwa1, complement.Complement(lanwa2)), intersection.Intersection(lanwa2, complement.Complement(lanwa1))));
    }

}
