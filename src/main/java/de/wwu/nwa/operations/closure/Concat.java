package de.wwu.nwa.operations.closure;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;


/**
 * Concatenation of two nested-word automata
 * This Class is not yet implemented
 *
 * @author Allan Grunert
 */
public class Concat {


    // TODO: Work with supset if on accpting Q is a transition with same symbol?
    // TODO: Test if internal = call or return then word can not be concatenated or is this erronous?

    public static CommonNestedWordAutomaton Concat(CommonNestedWordAutomaton dnwa1, CommonNestedWordAutomaton dnwa2) {

        CommonNestedWordAutomaton dnwa = dnwa1.clone();

        CommonNestedWordAutomaton dnwa2Tmp = dnwa2.clone();

        // TODO: Funktioniert dies?
        dnwa.getP().addAll(dnwa2Tmp.getP());
        dnwa.getQ().addAll(dnwa2Tmp.getQ());
        dnwa.getDeltaC().getDeltaC().addAll(dnwa2Tmp.getDeltaC().getDeltaC());
        dnwa.getDeltaI().getDeltaI().addAll(dnwa2Tmp.getDeltaI().getDeltaI());
        dnwa.getDeltaR().getDeltaR().addAll(dnwa2Tmp.getDeltaR().getDeltaR());

        // Übergangsmethoden erstellen von Akzeptiernden Q zu 2.Q0
        // von Akzeptierenden P zu 2.P0

        dnwa.setQf(dnwa2Tmp.getQf());
        dnwa.setPf(dnwa2Tmp.getPf());

        return dnwa;


    }

    public static NondeterministicNestedWordAutomaton Concat(NondeterministicNestedWordAutomaton nnwa1, NondeterministicNestedWordAutomaton nnwa2) {
        NondeterministicNestedWordAutomaton nnwa = new NondeterministicNestedWordAutomaton();

        // integrateStates(nnwa,nnwa1,nnwa2);

        return nnwa;
    }
}
