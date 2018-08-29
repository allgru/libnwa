package de.wwu.nwa.automata;

import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.NestedWord;
import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.exceptions.InputWordNotInAlphabeth;
import de.wwu.nwa.automata.items.exceptions.NestedEdgeSharePositionException;
import de.wwu.nwa.automata.items.exceptions.NestedEdgesCrossedException;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.node.Node;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunctions;
import de.wwu.nwa.automata.items.transition.InternalTransitionFunctions;
import de.wwu.nwa.automata.items.transition.ReturnTransitionFunctions;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NestedWordAutomatonTest {

    @Test
    public void testDNWA() {
        CommonNestedWordAutomaton dnwa = new CommonNestedWordAutomaton();


        String one = "1";
        String zero = "0";

        String onebrac = "<1";

        String zerobrac = "<0";

        String onebrar = "1>";

        String zerobrar = "0>";


        Alphabet alpha = new Alphabet();

        try {
            alpha.addSymbol(one, Alphabet.INTERNAL_POSITION);
            alpha.addSymbol(onebrac, Alphabet.CALL_POSITION);
            alpha.addSymbol(onebrar, Alphabet.RETURN_POSITION);
            alpha.addSymbol(zero, Alphabet.INTERNAL_POSITION);
            alpha.addSymbol(zerobrac, Alphabet.INTERNAL_POSITION);
            alpha.addSymbol(zerobrar, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        NestedWord nw = new NestedWord();
        nw.setAlphabet(alpha);

        try {
            nw.addSymbol("1");
            nw.addSymbol("<0");
            nw.addSymbol("1");
            nw.addSymbol("<1");
            nw.addSymbol("0");
            nw.addSymbol("0");
            nw.addSymbol("1>");
            nw.addSymbol("0>");
            nw.addSymbol("0");
        } catch (InputWordNotInAlphabeth inputWordNotInAlphabeth) {
            inputWordNotInAlphabeth.printStackTrace();
        }

        nw.addNestedEdge(1, 7);
        nw.addNestedEdge(3, 6);


        LinearState q0 = new LinearState("q0");
        LinearState q1 = new LinearState("q1");

        HierarchyState p = new HierarchyState("p");

        HierarchyState p0 = new HierarchyState("p0");
        HierarchyState p1 = new HierarchyState("p1");

        dnwa.setQ0(q0);

        dnwa.addStateToQ(q0);
        dnwa.addStateToQ(q1);

        dnwa.addStateToQf(q0);

        dnwa.addHierarchyStateToP(p);
        dnwa.addHierarchyStateToP(p0);
        dnwa.addHierarchyStateToP(p1);

        dnwa.setP0(p);

        dnwa.addHierarchyStateToPf(p);

        CallTransitionFunctions deltaC = new CallTransitionFunctions(dnwa);
        ReturnTransitionFunctions deltaR = new ReturnTransitionFunctions(dnwa);
        InternalTransitionFunctions deltaI = new InternalTransitionFunctions(dnwa);

        try {


            deltaC.addTransitionFunction(q0, zerobrac, q1, p0);
            deltaC.addTransitionFunction(q0, onebrac, q0, p0);
            deltaC.addTransitionFunction(q1, zerobrac, q1, p1);
            deltaC.addTransitionFunction(q1, onebrac, q0, p1);

            deltaR.addTransitionFunction(q1, p0, zerobrar, q0);
            deltaR.addTransitionFunction(q0, p1, onebrar, q1);
            deltaR.addTransitionFunction(q1, p1, onebrar, q1);
            deltaR.addTransitionFunction(q1, p1, zerobrar, q1);

            deltaI.addTransitionFunction(q1, one, q1);
            deltaI.addTransitionFunction(q1, zero, q0);
            deltaI.addTransitionFunction(q0, one, q0);
            deltaI.addTransitionFunction(q0, zero, q1);

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        dnwa.setDeltaC(deltaC);
        dnwa.setDeltaI(deltaI);
        dnwa.setDeltaR(deltaR);


        Run run = new Run(nw);
        run.setNwa(dnwa);

        boolean nestedWordAccepted = true;
        try {
            nestedWordAccepted = run.getInputWordAccepted();
        } catch (NestedEdgeSharePositionException e) {
            e.printStackTrace();
        } catch (NestedEdgesCrossedException e) {
            e.printStackTrace();
        }

        assertTrue("nested-word not accepted", !nestedWordAccepted);
        dnwa.addStateToQf(q1);

        nestedWordAccepted = false;

        try {
            nestedWordAccepted = run.getInputWordAccepted();
        } catch (NestedEdgeSharePositionException e) {
            e.printStackTrace();
        } catch (NestedEdgesCrossedException e) {
            e.printStackTrace();
        }
        assertTrue("nested-word accepted", nestedWordAccepted);


        for (int i = 1; i <= run.size(); i++) {
            Node node = run.getStep(i);

            System.out.println((i) + "." + node.getState().getStateName() + ": " + node.getSymbol());
        }


    }
}
