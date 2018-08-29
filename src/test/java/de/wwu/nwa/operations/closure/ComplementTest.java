package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import org.junit.Test;

public class ComplementTest {

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

    @Test
    public void complementTest() {
        CommonNestedWordAutomaton dnwa = new CommonNestedWordAutomaton();

        dnwa.setAlphabet(getAlphabet());

        LinearState q0 = new LinearState("q0");
        LinearState q1 = new LinearState("q1");
        LinearState q2 = new LinearState("q2");
        LinearState q3 = new LinearState("q3");

        HierarchyState p0 = new HierarchyState("p0");
        HierarchyState p1 = new HierarchyState("p1");
        HierarchyState p2 = new HierarchyState("p2");

        dnwa.addStateToQ(q0);
        dnwa.addStateToQ(q1);
        dnwa.addStateToQ(q2);
        dnwa.addStateToQ(q3);
        dnwa.addStateToQf(q1);

        dnwa.addHierarchyStateToP(p0);
        dnwa.addHierarchyStateToP(p1);
        dnwa.addHierarchyStateToP(p2);
        dnwa.addHierarchyStateToPf(p2);

        dnwa.setQ0(q0);
        dnwa.setP0(p0);

        try {
            dnwa.getDeltaC().addTransitionFunction(q0, "<a", q1, p1);
            dnwa.getDeltaC().addTransitionFunction(q1, "<b", q1, p2);

            dnwa.getDeltaR().addTransitionFunction(q1, p1, "a>", q1);
            dnwa.getDeltaR().addTransitionFunction(q2, p2, "b>", q1);

            dnwa.getDeltaI().addTransitionFunction(q1, "a", q2);
            dnwa.getDeltaI().addTransitionFunction(q2, "b", q3);

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        Complement complement = new Complement();

        // CommonNestedWordAutomaton cdnwa = complement.Complement(nwa);

        // cdnwa.printAll();

    }
}
