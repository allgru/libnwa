package de.wwu.nwa.automata.items.node;

import java.util.ArrayList;

/**
 * This class is intended for a run over a nested word. If there is a call symbol
 * and transition this node is added to the run.
 *
 * @author Allan Grunert
 */
public class CallNode extends EdgeNode {
    private ArrayList<InternalNode> internalNodes;
    private ReturnNode returnNode;

    public CallNode() {
        this.type = Node.CallNode;
        internalNodes = new ArrayList<InternalNode>();
    }

    /**
     * Add internal nodes that are added to this nested edge
     *
     * @param internalNode Add an internal node that follows this call
     *                     that is propagated from the call transition
     */
    public void addInternalNode(InternalNode internalNode) {
        internalNodes.add(internalNode);
    }

    public ReturnNode getReturnNode() {
        return returnNode;
    }

    /**
     * Set the node which completes the nested edge
     *
     * @param returnNode add the return node that follows this call (matched call)
     */
    public void setReturnNode(ReturnNode returnNode) {
        this.returnNode = returnNode;
    }


    /**
     * Give information on weather the node is matched
     *
     * @return true if there exists a matching return, else false
     */
    public boolean isMatched() {
        if (this.returnNode != null)
            return true;
        else
            return false;
    }
}
