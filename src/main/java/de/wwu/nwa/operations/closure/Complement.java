package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunction;
import de.wwu.nwa.automata.items.transition.InternalTransitionFunction;
import de.wwu.nwa.automata.items.transition.ReturnTransitionFunction;
import de.wwu.nwa.operations.transformation.CommonToLinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.operations.transformation.LinearAcceptingToCommonNestedWordAutomaton;

import java.util.ArrayList;

/**
 * Class for creating complement of nested-word language by creating new automata that accepts the complement language
 *
 * @author Allan Grunert
 */
public class Complement {
    public LinearAcceptingNestedWordAutomaton Complement(NestedWordAutomaton nwa1) {
        LinearAcceptingNestedWordAutomaton lanwa1 = new LinearAcceptingNestedWordAutomaton();

        CommonToLinearAcceptingNestedWordAutomaton ctlanwa = new CommonToLinearAcceptingNestedWordAutomaton();

        if (nwa1.getClass().equals(LinearAcceptingNestedWordAutomaton.class))
            lanwa1 = (LinearAcceptingNestedWordAutomaton) nwa1;

        if (nwa1.getClass().equals(CommonNestedWordAutomaton.class)) {
            lanwa1 = ctlanwa.transform((CommonNestedWordAutomaton) nwa1);
        }
        return this.Complement(lanwa1);
    }


    //  TODO: Test

    /**
     * Create Complement out of linear accepting nested-word automaton
     *
     * @param lnwaa linear accepting nested-word autoamton that should be complemented
     * @return complement of given linear accepting nested-word automaton
     */
    public LinearAcceptingNestedWordAutomaton Complement(LinearAcceptingNestedWordAutomaton lnwaa) {

        LinearAcceptingNestedWordAutomaton lnwab = new LinearAcceptingNestedWordAutomaton();

        // create new accepting states
        LinearState acceptingState = new LinearState("q_acc");
        HierarchyState acceptingHierarchyState = new HierarchyState("p_acc");

        lnwab.setAlphabet(lnwaa.getAlphabet().clone());

        lnwab.setQ((ArrayList<LinearState>) lnwaa.getQ().clone());
        lnwab.setP((ArrayList<HierarchyState>) lnwaa.getP().clone());

        lnwab.setQ0(lnwab.getLinearStateByName(lnwaa.getQ0().getStateName()));
        lnwab.setP0(lnwab.getHierarchyStateByName(lnwaa.getP0().getStateName()));

        lnwab.addStateToQ(acceptingState);
        lnwab.addStateToQf(acceptingState);

        lnwab.addHierarchyStateToP(acceptingHierarchyState);

        // all nonaccepting states are changed to accepting states
        for (LinearState q : lnwaa.getQ()
                ) {
            boolean stateNotContained = true;
            for (LinearState qf : lnwaa.getQf()
                    ) {
                if (q.getStateName().equals(qf.getStateName())) {
                    stateNotContained = false;
                    break;
                }

            }
            if (stateNotContained)
                lnwab.addStateToQf(q);
        }

        // Iterate through all linear states
        // find out which symbols have not been used from transitions to other states
        for (LinearState ls : lnwaa.getQ()) {
            ArrayList<String> usedSymbol = new ArrayList<String>();
            ArrayList<CallTransitionFunction> ctfs = lnwaa.getDeltaC().findCallTransitionFunctions(ls);
            for (CallTransitionFunction ctf : ctfs) {
                try {
                    lnwab.getDeltaC().addTransitionFunction(ls, ctf.getSymbol(), ctf.getqTarget(), ctf.getP(), "tc" + ctf.getName());
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
                usedSymbol.add(ctf.getSymbol());
            }

            ArrayList<ReturnTransitionFunction> rtfs = lnwaa.getDeltaR().findReturnTransitionFunctions(ls);
            for (ReturnTransitionFunction rtf : rtfs) {
                try {
                    lnwab.getDeltaR().addTransitionFunction(ls, rtf.getP(), rtf.getSymbol(), rtf.getqTarget(), "tr " + rtf.getName());
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

                usedSymbol.add(rtf.getSymbol());
            }

            ArrayList<InternalTransitionFunction> itfs = lnwaa.getDeltaI().findInternalTransitionFunctions(ls);
            for (InternalTransitionFunction itf : itfs) {
                try {
                    lnwab.getDeltaI().addTransitionFunction(ls, itf.getSymbol(), itf.getqTarget(), "ti " + itf.getName());
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

                usedSymbol.add(itf.getSymbol());
            }

            // Create new transitons with symbols that have not been used to the accepting state
            for (String symbol : lnwaa.getAlphabet().getAlphabet(Alphabet.CALL_POSITION)) {
                if (usedSymbol.indexOf(symbol) < 0) {
                    try {
                        lnwab.getDeltaC().addTransitionFunction(ls, symbol, acceptingState, acceptingHierarchyState, "tc " + symbol);
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (String symbol : lnwaa.getAlphabet().getAlphabet(Alphabet.INTERNAL_POSITION)) {
                if (usedSymbol.indexOf(symbol) < 0) {
                    try {
                        lnwab.getDeltaI().addTransitionFunction(ls, symbol, acceptingState, "ti " + symbol);
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (String symbol : lnwaa.getAlphabet().getAlphabet(Alphabet.RETURN_POSITION)) {
                if (usedSymbol.indexOf(symbol) < 0) {
                    try {
                        lnwab.getDeltaR().addTransitionFunction(ls, acceptingHierarchyState, symbol, acceptingState, "tr " + symbol);
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        // Let new acceptance state have iteration on itself
        for (String symbol : lnwaa.getAlphabet().getAlphabet(Alphabet.CALL_POSITION)) {
            try {
                lnwab.getDeltaC().addTransitionFunction(acceptingState, symbol, acceptingState, acceptingHierarchyState, "atc " + symbol);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }


        for (String symbol : lnwaa.getAlphabet().getAlphabet(Alphabet.INTERNAL_POSITION)) {
            try {
                lnwab.getDeltaI().addTransitionFunction(acceptingState, symbol, acceptingState, "itc " + symbol);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }

        for (String symbol : lnwaa.getAlphabet().getAlphabet(Alphabet.RETURN_POSITION)) {
            try {
                lnwab.getDeltaR().addTransitionFunction(acceptingState, acceptingHierarchyState, symbol, acceptingState, "atr " + symbol);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }

        return lnwab;
    }

    /**
     * Create Complement from deterministic automaton
     *
     * @param cnwa common nested-word automata to be transformed
     * @return new common nested-word automata
     */
    public CommonNestedWordAutomaton Complement(CommonNestedWordAutomaton cnwa) {
        CommonToLinearAcceptingNestedWordAutomaton dnwa2lanwa = new CommonToLinearAcceptingNestedWordAutomaton();
        LinearAcceptingNestedWordAutomaton lanwa = dnwa2lanwa.transform(cnwa);
        LinearAcceptingToCommonNestedWordAutomaton lnwa2dnwa = new LinearAcceptingToCommonNestedWordAutomaton();
        return lnwa2dnwa.transform(this.Complement(lanwa));
    }

}
