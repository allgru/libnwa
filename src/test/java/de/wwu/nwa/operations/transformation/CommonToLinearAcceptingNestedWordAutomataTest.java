package de.wwu.nwa.operations.transformation;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import org.junit.Test;

public class CommonToLinearAcceptingNestedWordAutomataTest {

    public Alphabet getAlphabet() {
        Alphabet alpha = new Alphabet();

        try {
            alpha.addSymbol("<a", Alphabet.CALL_POSITION);
            alpha.addSymbol("<b", Alphabet.CALL_POSITION);
            alpha.addSymbol("<c", Alphabet.CALL_POSITION);
            alpha.addSymbol("a>", Alphabet.RETURN_POSITION);
            alpha.addSymbol("b>", Alphabet.RETURN_POSITION);
            alpha.addSymbol("c>", Alphabet.RETURN_POSITION);

            alpha.addSymbol("a", Alphabet.INTERNAL_POSITION);
            alpha.addSymbol("b", Alphabet.INTERNAL_POSITION);
            alpha.addSymbol("c", Alphabet.INTERNAL_POSITION);

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        return alpha;
    }

    public CommonNestedWordAutomaton getCNWA() {
        CommonNestedWordAutomaton cnwa = new CommonNestedWordAutomaton();

        HierarchyState p10 = new HierarchyState("p_0");
        HierarchyState p110 = new HierarchyState("p_1");
        HierarchyState p121 = new HierarchyState("p_2");

        LinearState q100 = new LinearState("q_0");
        LinearState q110 = new LinearState("q_1");
        LinearState q120 = new LinearState("q_2");

        cnwa.addHierarchyStateToP(p10);
        cnwa.addHierarchyStateToP(p110);
        cnwa.addHierarchyStateToP(p121);

        cnwa.addHierarchyStateToPf(p10);
        cnwa.addHierarchyStateToPf(p110);

        cnwa.addStateToQ(q100);
        cnwa.addStateToQ(q110);
        cnwa.addStateToQ(q120);

        cnwa.addStateToQf(q120);
        cnwa.setQ0(q100);
        cnwa.setP0(p10);


        return cnwa;
    }


    @Test
    public void testCommonToLinearAcceptingNestedWordAutomatonTest() {
        CommonNestedWordAutomaton cnwa = this.getCNWA();
        cnwa.setAlphabet(this.getAlphabet());

        // Internal Transition
        try {
            cnwa.getDeltaI().addTransitionFunction(cnwa.getLinearStateByName("q_0"), "a", cnwa.getLinearStateByName("q_1"));
            cnwa.getDeltaI().addTransitionFunction(cnwa.getLinearStateByName("q_1"), "b", cnwa.getLinearStateByName("q_2"));
            cnwa.getDeltaI().addTransitionFunction(cnwa.getLinearStateByName("q_2"), "c", cnwa.getLinearStateByName("q_1"));
            cnwa.getDeltaI().addTransitionFunction(cnwa.getLinearStateByName("q_1"), "a", cnwa.getLinearStateByName("q_2"));
            cnwa.getDeltaI().addTransitionFunction(cnwa.getLinearStateByName("q_2"), "b", cnwa.getLinearStateByName("q_1"));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }


        try {
            cnwa.getDeltaC().addTransitionFunction(cnwa.getLinearStateByName("q_0"), "<a", cnwa.getLinearStateByName("q_1"), cnwa.getHierarchyStateByName("p_1"));
            cnwa.getDeltaC().addTransitionFunction(cnwa.getLinearStateByName("q_2"), "<c", cnwa.getLinearStateByName("q_1"), cnwa.getHierarchyStateByName("p_2"));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }


        try {
            cnwa.getDeltaR().addTransitionFunction(cnwa.getLinearStateByName("q_0"), cnwa.getHierarchyStateByName("p_1"), "a>", cnwa.getLinearStateByName("q_2"));
            cnwa.getDeltaR().addTransitionFunction(cnwa.getLinearStateByName("q_2"), cnwa.getHierarchyStateByName("p_2"), "c>", cnwa.getLinearStateByName("q_1"));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        CommonToLinearAcceptingNestedWordAutomaton ctlanwa = new CommonToLinearAcceptingNestedWordAutomaton();
        LinearAcceptingNestedWordAutomaton lanwa = ctlanwa.transform(cnwa);

        cnwa.printAll();
        lanwa.printAll();
    }
}