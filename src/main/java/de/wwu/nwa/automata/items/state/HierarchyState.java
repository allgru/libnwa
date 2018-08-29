package de.wwu.nwa.automata.items.state;

/**
 * State that is responsable for nested edges. Commonly belongs to a Nested-word automata (P,Pf,p0)
 *
 * @author Allan Grunert
 */
public class HierarchyState extends State {

    public HierarchyState(String name) {
        super(name);
    }

    public String getStateType() {
        return "HierarchyState";
    }

}
