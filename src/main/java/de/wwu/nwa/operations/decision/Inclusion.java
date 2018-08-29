package de.wwu.nwa.operations.decision;

import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.operations.closure.Complement;
import de.wwu.nwa.operations.closure.Intersection;

public class Inclusion {

    /**
     * Tests if first automaton is in second one
     *
     * @param lanwa1 first automaton for testing if in second
     * @param lanwa2 automaton for letting test if first automaton is included
     * @return true, if inclusion is given, else false
     */
    public boolean Inclusion(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2) {
        Complement complement = new Complement();
        Intersection intersection = new Intersection();
        Emptiness emptiness = new Emptiness();

        return (emptiness.Emptiness(intersection.Intersection(lanwa1, complement.Complement(lanwa2))));
    }
}
