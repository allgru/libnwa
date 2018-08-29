package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;

/**
 * Create Nested-word automaton from a Nested-word automaton subtraction of the second one
 *
 * @author Allan Grunert
 */
public class Subtraction {

    /**
     * get a Nested-word automata of one automata subtracted by another
     *
     * @param lanwa1 Nested-word automata the second one is subtracted from
     * @param lanwa2 Nested-word automata for subtracting
     * @return Nested-word automata that consists of the first one subtracted by the second one given in the parameters
     */
    public LinearAcceptingNestedWordAutomaton Subtract(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2) {
        Intersection intersection = new Intersection();
        Complement complement = new Complement();

        return intersection.Intersection(lanwa1, complement.Complement(lanwa2));
    }
}