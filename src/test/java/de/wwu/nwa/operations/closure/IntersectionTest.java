package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import org.junit.Test;

public class IntersectionTest {

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

    public LinearAcceptingNestedWordAutomaton getLanwa1() {
        LinearAcceptingNestedWordAutomaton lanwa1 = new LinearAcceptingNestedWordAutomaton();

        HierarchyState p10 = new HierarchyState("(p^1_0,0)");
        HierarchyState p110 = new HierarchyState("(p^1_1,0)");
        HierarchyState p111 = new HierarchyState("(p^1_1,1)");
        HierarchyState p120 = new HierarchyState("(p^1_2,0)");
        HierarchyState p121 = new HierarchyState("(p^1_2,1)");

        LinearState q100 = new LinearState("(q^1_0,0)");
        LinearState q110 = new LinearState("(q^1_1,0)");
        LinearState q111 = new LinearState("(q^1_1,1)");
        LinearState q120 = new LinearState("(q^1_2,0)");
        LinearState q121 = new LinearState("(q^1_2,1)");
        LinearState q130 = new LinearState("(q^1_3,0)");
        LinearState q131 = new LinearState("(q^1_3,1)");
        LinearState q140 = new LinearState("(q^1_4,0)");
        LinearState q141 = new LinearState("(q^1_4,1)");

        lanwa1.addHierarchyStateToP(p10);
        lanwa1.addHierarchyStateToP(p110);
        lanwa1.addHierarchyStateToP(p111);
        lanwa1.addHierarchyStateToP(p120);
        lanwa1.addHierarchyStateToP(p121);

        lanwa1.addStateToQ(q100);
        lanwa1.addStateToQ(q110);
        lanwa1.addStateToQ(q111);
        lanwa1.addStateToQ(q120);
        lanwa1.addStateToQ(q121);
        lanwa1.addStateToQ(q130);
        lanwa1.addStateToQ(q131);
        lanwa1.addStateToQ(q140);
        lanwa1.addStateToQ(q141);

        lanwa1.addStateToQf(q120);
        lanwa1.setQ0(q100);
        lanwa1.setP0(p10);

        return lanwa1;
    }

    public LinearAcceptingNestedWordAutomaton getLanwa2() {
        LinearAcceptingNestedWordAutomaton lanwa1 = new LinearAcceptingNestedWordAutomaton();

        HierarchyState p10 = new HierarchyState("(p^2_0,0)");
        HierarchyState p110 = new HierarchyState("(p^2_1,0)");
        HierarchyState p111 = new HierarchyState("(p^2_1,1)");
        HierarchyState p120 = new HierarchyState("(p^2_2,0)");
        HierarchyState p121 = new HierarchyState("(p^2_2,1)");

        LinearState q100 = new LinearState("(q^2_0,0)");
        LinearState q110 = new LinearState("(q^2_1,0)");
        LinearState q111 = new LinearState("(q^2_1,1)");

        LinearState q120 = new LinearState("(q^2_2,0)");
        LinearState q121 = new LinearState("(q^2_2,1)");
        LinearState q130 = new LinearState("(q^2_3,0)");
        LinearState q131 = new LinearState("(q^2_3,1)");
        LinearState q140 = new LinearState("(q^2_4,0)");
        LinearState q141 = new LinearState("(q^2_4,1)");

        lanwa1.addHierarchyStateToP(p10);
        lanwa1.addHierarchyStateToP(p110);
        lanwa1.addHierarchyStateToP(p111);
        lanwa1.addHierarchyStateToP(p120);
        lanwa1.addHierarchyStateToP(p121);

        lanwa1.addStateToQ(q100);
        lanwa1.addStateToQ(q110);
        lanwa1.addStateToQ(q111);
        lanwa1.addStateToQ(q120);
        lanwa1.addStateToQ(q121);
        lanwa1.addStateToQ(q130);
        lanwa1.addStateToQ(q131);
        lanwa1.addStateToQ(q140);
        lanwa1.addStateToQ(q141);

        lanwa1.addStateToQf(q120);
        lanwa1.setQ0(q100);
        lanwa1.setP0(p10);

        return lanwa1;
    }

    @Test
    public void testUnionSameTransitions() {

        LinearAcceptingNestedWordAutomaton lanwa1 = getLanwa1();
        lanwa1.setAlphabet(getAlphabet());

        LinearAcceptingNestedWordAutomaton lanwa2 = getLanwa2();
        lanwa2.setAlphabet(getAlphabet());

        // Internal Transition
        try {
            lanwa1.getDeltaI().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_0,0)"), "a", lanwa1.getLinearStateByName("(q^1_1,1)"));
            lanwa1.getDeltaI().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_1,1)"), "b", lanwa1.getLinearStateByName("(q^1_2,1)"));
            lanwa1.getDeltaI().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_2,1)"), "c", lanwa1.getLinearStateByName("(q^1_3,1)"));
            lanwa1.getDeltaI().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_3,1)"), "a", lanwa1.getLinearStateByName("(q^1_4,1)"));
            lanwa1.getDeltaI().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_4,1)"), "b", lanwa1.getLinearStateByName("(q^1_2,0)"));


            lanwa2.getDeltaI().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_0,0)"), "a", lanwa2.getLinearStateByName("(q^2_1,1)"));
            lanwa2.getDeltaI().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_1,1)"), "b", lanwa2.getLinearStateByName("(q^2_2,1)"));
            lanwa2.getDeltaI().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_2,1)"), "c", lanwa2.getLinearStateByName("(q^2_3,1)"));
            lanwa2.getDeltaI().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_3,1)"), "a", lanwa2.getLinearStateByName("(q^2_4,1)"));
            lanwa2.getDeltaI().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_4,1)"), "b", lanwa2.getLinearStateByName("(q^2_2,0)"));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }


        try {
            lanwa1.getDeltaC().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_0,0)"), "<a", lanwa1.getLinearStateByName("(q^1_1,1)"), lanwa1.getHierarchyStateByName("(p^1_1,0)"));
            lanwa1.getDeltaC().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_1,1)"), "<b", lanwa1.getLinearStateByName("(q^1_2,1)"), lanwa1.getHierarchyStateByName("(p^1_1,1)"));
            lanwa1.getDeltaC().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_2,1)"), "<c", lanwa1.getLinearStateByName("(q^1_3,1)"), lanwa1.getHierarchyStateByName("(p^1_2,0)"));
            lanwa1.getDeltaC().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_3,1)"), "<a", lanwa1.getLinearStateByName("(q^1_4,1)"), lanwa1.getHierarchyStateByName("(p^1_2,1)"));
            lanwa1.getDeltaC().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_4,1)"), "<b", lanwa1.getLinearStateByName("(q^1_2,0)"), lanwa1.getHierarchyStateByName("(p^1_1,0)"));


            lanwa2.getDeltaC().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_0,0)"), "<a", lanwa2.getLinearStateByName("(q^2_1,1)"), lanwa2.getHierarchyStateByName("(p^2_1,0)"));
            lanwa2.getDeltaC().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_1,1)"), "<b", lanwa2.getLinearStateByName("(q^2_2,1)"), lanwa2.getHierarchyStateByName("(p^2_1,0)"));
            lanwa2.getDeltaC().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_2,1)"), "<c", lanwa2.getLinearStateByName("(q^2_3,1)"), lanwa2.getHierarchyStateByName("(p^2_2,0)"));
            lanwa2.getDeltaC().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_3,1)"), "<a", lanwa2.getLinearStateByName("(q^2_4,1)"), lanwa2.getHierarchyStateByName("(p^2_2,0)"));
            lanwa2.getDeltaC().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_4,1)"), "<b", lanwa2.getLinearStateByName("(q^2_2,0)"), lanwa2.getHierarchyStateByName("(p^2_1,0)"));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }


        try {
            lanwa1.getDeltaR().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_0,0)"), lanwa1.getHierarchyStateByName("(p^1_1,0)"), "a>", lanwa1.getLinearStateByName("(q^1_1,1)"));
            lanwa1.getDeltaR().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_1,1)"), lanwa1.getHierarchyStateByName("(p^1_1,1)"), "b>", lanwa1.getLinearStateByName("(q^1_2,1)"));
            lanwa1.getDeltaR().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_2,1)"), lanwa1.getHierarchyStateByName("(p^1_2,0)"), "c>", lanwa1.getLinearStateByName("(q^1_3,1)"));
            lanwa1.getDeltaR().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_3,1)"), lanwa1.getHierarchyStateByName("(p^1_2,1)"), "a>", lanwa1.getLinearStateByName("(q^1_4,1)"));
            lanwa1.getDeltaR().addTransitionFunction(lanwa1.getLinearStateByName("(q^1_4,1)"), lanwa1.getHierarchyStateByName("(p^1_1,0)"), "b>", lanwa1.getLinearStateByName("(q^1_2,0)"));

            lanwa2.getDeltaR().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_0,0)"), lanwa2.getHierarchyStateByName("(p^2_1,0)"), "a>", lanwa2.getLinearStateByName("(q^2_1,1)"));
            lanwa2.getDeltaR().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_1,1)"), lanwa2.getHierarchyStateByName("(p^2_1,0)"), "b>", lanwa2.getLinearStateByName("(q^2_2,1)"));
            lanwa2.getDeltaR().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_2,1)"), lanwa2.getHierarchyStateByName("(p^2_2,0)"), "c>", lanwa2.getLinearStateByName("(q^2_3,1)"));
            lanwa2.getDeltaR().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_3,1)"), lanwa2.getHierarchyStateByName("(p^2_2,0)"), "a>", lanwa2.getLinearStateByName("(q^2_4,1)"));
            lanwa2.getDeltaR().addTransitionFunction(lanwa2.getLinearStateByName("(q^2_4,1)"), lanwa2.getHierarchyStateByName("(p^2_1,0)"), "b>", lanwa2.getLinearStateByName("(q^2_2,0)"));

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        Intersection intersection = new Intersection();
        LinearAcceptingNestedWordAutomaton lanwa = intersection.Intersection(lanwa1, lanwa2);

        lanwa.printAll();


    }

}
