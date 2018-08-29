package de.wwu.nwa.automata.items.node;

import de.wwu.nwa.automata.items.state.HierarchyState;

/**
 * Defines a node which is set if the Nested-Word automata has a return transition
 * on a Nested-word symbol
 *
 * @author Allan Grunert
 */
public class ReturnNode extends EdgeNode {

    private HierarchyState lastHierarchyState;

    public ReturnNode() {
        this.type = Node.ReturnNode;
    }

    public HierarchyState getLastHierarchyState() {
        return lastHierarchyState;
    }

    public void setLastHierarchyState(HierarchyState lastHierarchyState) {
        this.lastHierarchyState = lastHierarchyState;
    }


}
