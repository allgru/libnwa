package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;

public class ReturnTransitionFunction {
    protected LinearState qSource;
    protected String symbol;
    protected LinearState qTarget;
    protected HierarchyState p;
    protected NestedWordAutomaton nwa;
    private String name;

    public ReturnTransitionFunction(LinearState qSource, HierarchyState p, String symbol, LinearState qTarget, NestedWordAutomaton nwa) {
        this.qSource = qSource;
        this.symbol = symbol;
        this.qTarget = qTarget;
        this.p = p;
        this.nwa = nwa;
    }

    public boolean stateFulfilled(LinearState q, HierarchyState p, String symbol) {
        return (qSource.getStateName().equals(q.getStateName()) && this.p.getStateName().equals(p.getStateName()) && this.symbol.equals(symbol));
    }

    public LinearState getFollowState(LinearState q, HierarchyState p, String symbol) {
        if (stateFulfilled(q, p, symbol))
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

