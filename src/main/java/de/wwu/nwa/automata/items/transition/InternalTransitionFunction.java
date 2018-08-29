package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.state.LinearState;

public class InternalTransitionFunction {
    protected LinearState qSource;
    protected String symbol;
    protected LinearState qTarget;
    protected NestedWordAutomaton nwa;
    private String name;

    public InternalTransitionFunction(LinearState qSource, String symbol, LinearState qTarget, NestedWordAutomaton nwa) {
        this.qSource = qSource;
        this.symbol = symbol;
        this.qTarget = qTarget;
        this.nwa = nwa;
    }

    public boolean stateFulfilled(LinearState q, String symbol) {
        return (this.qSource.getStateName().equals(q.getStateName()) && this.symbol.equals(symbol));
    }

    public LinearState getFollowState(LinearState q, String symbol) {
        if (stateFulfilled(q, symbol))
            return this.qTarget;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
