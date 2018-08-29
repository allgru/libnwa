package de.wwu.nwa.operations;

import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

/**
 * Main class for operations
 *
 * @author Allan Grunert
 */
public class Operation {
    protected LinearState discardingLinearState;
    protected HierarchyState discardingHierarchyState;

    public Operation() {
        this.discardingLinearState = new LinearState("q_v");
        this.discardingHierarchyState = new HierarchyState("p_v");

    }
}
