package de.wwu.nwa.automata.items.state;

/**
 * Sate that is commonly propagated by a Nested-word automata along the linear edge
 * and is part of such an automata (Q,Qf,q0)
 *
 * @author Allan Grunert
 */
public class LinearState extends State {
    public LinearState(String name) {
        super(name);
    }

    public String getStateType() {
        return "LinearState";
    }

}
