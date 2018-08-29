package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.operations.Operation;
import de.wwu.nwa.operations.transformation.CommonToLinearAcceptingNestedWordAutomaton;

import java.util.ArrayList;

/**
 * Operation for intersecting two Nested-Word automata
 *
 * @author Allan Grunert
 */
public class Intersection extends Operation {

    public Intersection() {
        super();
    }

    public LinearAcceptingNestedWordAutomaton Intersection(NestedWordAutomaton nwa1, NestedWordAutomaton nwa2) {
        LinearAcceptingNestedWordAutomaton lanwa1 = new LinearAcceptingNestedWordAutomaton();
        LinearAcceptingNestedWordAutomaton lanwa2 = new LinearAcceptingNestedWordAutomaton();

        CommonToLinearAcceptingNestedWordAutomaton ctlanwa = new CommonToLinearAcceptingNestedWordAutomaton();

        if (nwa1.getClass().equals(LinearAcceptingNestedWordAutomaton.class))
            lanwa1 = (LinearAcceptingNestedWordAutomaton) nwa1;
        if (nwa2.getClass().equals(LinearAcceptingNestedWordAutomaton.class))
            lanwa2 = (LinearAcceptingNestedWordAutomaton) nwa2;

        if (nwa1.getClass().equals(CommonNestedWordAutomaton.class)) {
            lanwa1 = ctlanwa.transform((CommonNestedWordAutomaton) nwa1);
        }
        if (nwa2.getClass().equals(CommonNestedWordAutomaton.class)) {
            lanwa2 = ctlanwa.transform((CommonNestedWordAutomaton) nwa1);
        }
        return this.Intersection(lanwa1, lanwa2);
    }

    /**
     * Intersect two linear-accepting linear-accepting nested-word automata
     *
     * @param lanwa1 first linear-accepting nested-word automaton to intersect
     * @param lanwa2 second linear-accepting nested-word automaton to intersect
     * @return intersection of first and second linear-accepting nested-word automaton
     */
    public LinearAcceptingNestedWordAutomaton Intersection(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2) {
        // intersection is the same as Union
        Union union = new Union();
        LinearAcceptingNestedWordAutomaton lanwa = union.Union(lanwa1, lanwa2);

        // empty Accepting states
        lanwa.setQf(new ArrayList<LinearState>());

        // only states where the pair consists of accepting states of both automata are accepting
        for (LinearState qf1 : lanwa1.getQf()
                ) {
            for (LinearState qf2 : lanwa2.getQf()
                    ) {
                lanwa.addStateToQf(lanwa.getLinearStateByName("(" + qf1.getStateName() + "," + qf2.getStateName() + ")"));
            }
        }

        return lanwa;
    }
}
