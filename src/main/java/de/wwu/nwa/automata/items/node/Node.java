package de.wwu.nwa.automata.items.node;


import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.State;

/**
 * Node for a Nested InputWord Automata
 *
 * @author Allan Grunert
 */
public class Node {
    public static final int CallNode = 0;
    public static final int ReturnNode = 1;
    public static final int InternalNode = 2;
    protected String label;
    protected int type;
    protected int position;
    protected boolean pending = false;
    protected State state;
    protected String symbol;

    protected HierarchyState hierarchyState;

    public HierarchyState getHierarchyState() {
        return hierarchyState;
    }

    public void setHierarchyState(HierarchyState hierarchyState) {
        this.hierarchyState = hierarchyState;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getType() {
        return this.type;
    }

    /**
     * Get position of node in nested word
     *
     * @return gives back the position of the node
     */
    public int getPosition() {
        return position;
    }

    /**
     * set position of node in nested word
     *
     * @param position position of node corresponding to the position in of a symbol in the Nested-word
     */
    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
