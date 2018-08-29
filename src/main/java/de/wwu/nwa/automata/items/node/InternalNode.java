package de.wwu.nwa.automata.items.node;

import de.wwu.nwa.automata.items.state.LinearState;

/**
 * This class represents a node which are between nested edges (call node and return nodes).
 * Nodes are created for a run if a Nested-word automaton has a interenal transition on a
 * Nested-words symbol
 *
 * @author Allan Grunert
 */
public class InternalNode extends Node {

    public InternalNode() {
        this.type = Node.InternalNode;
    }

    public LinearState getLinearState() {
        return (LinearState) state;
    }

    /**
     * Defines which state was activated
     *
     * @param linearState linear state that represents the edge leading to the node
     */
    public void setLinearState(LinearState linearState) {
        this.state = linearState;
    }
}
