package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.state.State;

/**
 * Function that is called if a call-symbol has been recognized
 *
 * @author Allan Grunert
 */
public class CallTransitionFunction {
    protected LinearState qSource;
    protected String symbol;
    protected LinearState qTarget;
    protected HierarchyState p;
    protected NestedWordAutomaton nwa;
    private String name;

    public CallTransitionFunction(LinearState qSource, String symbol, LinearState qTarget, HierarchyState p, NestedWordAutomaton nwa) {
        this.qSource = qSource;
        this.symbol = symbol;
        this.qTarget = qTarget;
        this.p = p;
        this.nwa = nwa;
    }

    /**
     * Checks if linear state as input with input symbol is accepting
     *
     * @param q      state to be checked
     * @param symbol input symbol for transition function
     * @return true if linear state is accepting, else false
     */
    public boolean stateFulfilled(State q, String symbol) {
        return (qSource.getStateName().equals(q.getStateName()) && this.symbol.equals(symbol));
    }

    /**
     * Gives back the output lineare state if input parameters are equal to the ones of the function
     *
     * @param q      input linear state
     * @param symbol input symbol
     * @return linear state that is the output of the function
     */
    public LinearState getFollowState(LinearState q, String symbol) {
        if (stateFulfilled(q, symbol))
            return this.qTarget;
        else
            return null;
    }

    /**
     * Gives back the output hierarchy state if input parameters are equal to the ones of the function
     *
     * @param q      input linear state
     * @param symbol input symbol
     * @return hierarchy state that is the output of the function
     */
    public HierarchyState getFollowHierarchyState(State q, String symbol) {
        if (stateFulfilled(q, symbol))
            return this.p;
        else
            return null;
    }

    public LinearState getqSource() {
        return qSource;
    }

    public void setqSource(LinearState qSource) {
        this.qSource = qSource;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LinearState getqTarget() {
        return qTarget;
    }

    public void setqTarget(LinearState qTarget) {
        this.qTarget = qTarget;
    }

    public HierarchyState getP() {
        return p;
    }

    public void setP(HierarchyState p) {
        this.p = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
