package de.wwu.nwa.automata.items.state;

/**
 * Superclassfor all types of States
 *
 * @author Allan Grunert
 */
public abstract class State {
    protected String stateName;

    public State(String name) {
        this.stateName = name;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    abstract public String getStateType();
}
