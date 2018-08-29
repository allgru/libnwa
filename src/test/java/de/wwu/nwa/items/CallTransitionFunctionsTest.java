package de.wwu.nwa.items;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunctions;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class CallTransitionFunctionsTest {

    @Test
    public void getFollowStatesTest() {

        CommonNestedWordAutomaton dnwa = new CommonNestedWordAutomaton();


        String nw = "test";

        LinearState q0 = new LinearState("test");
        LinearState q0_1 = new LinearState("test");

        LinearState q1 = new LinearState("test2");

        HierarchyState hq0 = new HierarchyState("test");

        CallTransitionFunctions ctf = new CallTransitionFunctions(dnwa);

        try {
            ctf.addTransitionFunction(q0, nw, q1, hq0);

            assertTrue(ctf.transitionState(q0, nw).getStateName().equals(q1.getStateName()));
            assertTrue(ctf.transitionHierarchyState(q0_1, nw).getStateName().equals(hq0.getStateName()));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }


    }
}
