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
import de.wwu.nwa.operations.Operation;
import de.wwu.nwa.operations.transformation.CommonToLinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.operations.transformation.LinearAcceptingToCommonNestedWordAutomaton;

import java.util.ArrayList;

/**
 * Class for creating a union of two nested-word languages by creating a powerset automata of two automatas
 *
 * @author Allan Grunert
 */
public class Union extends Operation {

    public Union() {
        super();
    }

    public LinearAcceptingNestedWordAutomaton Union(NestedWordAutomaton nwa1, NestedWordAutomaton nwa2) {
        LinearAcceptingNestedWordAutomaton lanwa1 = new LinearAcceptingNestedWordAutomaton();
        LinearAcceptingNestedWordAutomaton lanwa2 = new LinearAcceptingNestedWordAutomaton();

        CommonToLinearAcceptingNestedWordAutomaton ctlanwa = new CommonToLinearAcceptingNestedWordAutomaton();

        if (nwa1.getClass().equals(CommonNestedWordAutomaton.class)) {
            lanwa1 = ctlanwa.transform((CommonNestedWordAutomaton) nwa1);
        } else if (nwa1.getClass().equals(LinearAcceptingNestedWordAutomaton.class))
            lanwa1 = (LinearAcceptingNestedWordAutomaton) nwa1;
        if (nwa2.getClass().equals(CommonNestedWordAutomaton.class)) {
            lanwa2 = ctlanwa.transform((CommonNestedWordAutomaton) nwa1);
        } else if (nwa2.getClass().equals(LinearAcceptingNestedWordAutomaton.class))
            lanwa2 = (LinearAcceptingNestedWordAutomaton) nwa2;

        return this.Union(lanwa1, lanwa2);
    }

    /**
     * Create a DNWA which will understand the set of words consisting out of the two given automata
     *
     * @param dnwa1 deterministic nested word automata for first language
     * @param dnwa2 deterministic nested word automata for first language
     * @return deterministic word automata understanding both languages
     */
    public CommonNestedWordAutomaton Union(CommonNestedWordAutomaton dnwa1, CommonNestedWordAutomaton dnwa2) {

        CommonToLinearAcceptingNestedWordAutomaton dnwaTransformation = new CommonToLinearAcceptingNestedWordAutomaton();

        LinearAcceptingNestedWordAutomaton lanwa1 = dnwaTransformation.transform(dnwa1);
        LinearAcceptingNestedWordAutomaton lanwa2 = dnwaTransformation.transform(dnwa2);

        LinearAcceptingNestedWordAutomaton lanwa = this.Union(lanwa1, lanwa2);

        LinearAcceptingToCommonNestedWordAutomaton linwaTransformation = new LinearAcceptingToCommonNestedWordAutomaton();

        return linwaTransformation.transform(lanwa);

    }

    /**
     * Create new powerset automata understanding the language both of lanwa1 and lanwa2
     * <p>
     * O(n^5), this is the time for creating the transitions
     *
     * @param lanwa1 first automata for powerset construction
     * @param lanwa2 second automata for powerset contruction
     * @return new automata which understands both language the given automata understand
     */
    public LinearAcceptingNestedWordAutomaton Union(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2) {
        LinearAcceptingNestedWordAutomaton lanwa = new LinearAcceptingNestedWordAutomaton();

        // this state is needed for transitions that only run on one automata
        // the other automata has, in this case no state with transitions

        // create Alphabet
        Alphabet alphabet = lanwa1.getAlphabet();

        lanwa.setAlphabet(alphabet.clone());

        // first step: add set of pairs of hierarchy and linear states
        lanwa.setP(crossProductHierarchyStates(lanwa1, lanwa2));

        ArrayList<LinearState> acceptingStates = new ArrayList<LinearState>();
        lanwa.setQ(crossProductLinearStates(lanwa1, lanwa2, acceptingStates));

        lanwa.setQf(acceptingStates);

        // second step: create transition functions
        createTransitionFunctions(lanwa1, lanwa2, lanwa);

        // third step: create starting states

        // set initial linear state
        lanwa.setQ0(lanwa.getLinearStateByName("(" + lanwa1.getQ0().getStateName() + "," + lanwa2.getQ0().getStateName() + ")"));

        // set initial hierarchy state
        lanwa.setP0(lanwa.getHierarchyStateByName("(" + lanwa1.getP0().getStateName() + "," + lanwa2.getP0().getStateName() + ")"));

        return lanwa;
    }

    /**
     * Create cross product of all hierarchy states from two automa
     *
     * @param lanw1 first linear accepting nested-word automata containing hierarchy states for the cross product
     * @param lanw2 second linear accepting nested-word automata containing hierarchy states for the cross product
     * @return cross product of hierarchy states
     */
    public ArrayList<HierarchyState> crossProductHierarchyStates(LinearAcceptingNestedWordAutomaton lanw1, LinearAcceptingNestedWordAutomaton lanw2) {
        ArrayList<HierarchyState> HierarchyStateList = new ArrayList<HierarchyState>();

        for (HierarchyState p1 : lanw1.getP()
                ) {
            // all states that are in both sets
            for (HierarchyState p2 : lanw2.getP()
                    ) {
                String stateName = "(" + p1.getStateName() + ",";
                stateName += p2.getStateName();
                stateName += ")";
                HierarchyState pnew = new HierarchyState(stateName);
                HierarchyStateList.add(pnew);
            }

            // states that are ony in first set
            String stateName = "(" + p1.getStateName() + ",";
            stateName += this.discardingHierarchyState.getStateName();
            stateName += ")";

            HierarchyState pnew = new HierarchyState(stateName);
            HierarchyStateList.add(pnew);
        }

        // states that are only in second set of states
        for (HierarchyState p2 : lanw2.getP()
                ) {
            String stateName = "(" + this.discardingHierarchyState.getStateName() + ",";
            stateName += p2.getStateName();
            stateName += ")";
            HierarchyState pnew = new HierarchyState(stateName);
            HierarchyStateList.add(pnew);

        }

        return HierarchyStateList;
    }

    /**
     * Create cross product of all linear states from two automa
     * <p>
     * O(n^3) for two for-loops containing the search for an accepting state
     *
     * @param lanw1           first linear accepting nested-word automata for creating a cross product of the linear states
     * @param lanw2           second linear accepting nested-word automata for creating a cross product of the linear states
     * @param acceptingStates all states that are accepting
     * @return cross product of linear states
     */
    public ArrayList<LinearState> crossProductLinearStates(LinearAcceptingNestedWordAutomaton lanw1, LinearAcceptingNestedWordAutomaton lanw2, ArrayList<LinearState> acceptingStates) {
        ArrayList<LinearState> linearStateList = new ArrayList<LinearState>();

        // we have acceptingStates as parameter as it is more efficient to
        // already define accepting states here if we already iterate through all

        for (LinearState q1 : lanw1.getQ()
                ) {
            for (LinearState q2 : lanw2.getQ()
                    ) {
                String stateName = "(" + q1.getStateName() + ",";
                stateName += q2.getStateName();
                stateName += ")";
                LinearState qnew = new LinearState(stateName);
                linearStateList.add(qnew);

                if (lanw1.isAcceptingLinearState(q1) || lanw2.isAcceptingLinearState(q2))
                    acceptingStates.add(qnew);

            }

            String stateName = "(" + q1.getStateName() + ",";
            stateName += this.discardingLinearState.getStateName();
            stateName += ")";
            LinearState qnew = new LinearState(stateName);
            linearStateList.add(qnew);
            if (lanw1.isAcceptingLinearState(q1))
                acceptingStates.add(qnew);

        }

        for (LinearState q2 : lanw2.getQ()
                ) {
            String stateName = "(" + this.discardingLinearState.getStateName() + ",";
            stateName += q2.getStateName();
            stateName += ")";
            LinearState qnew = new LinearState(stateName);
            linearStateList.add(qnew);
            if (lanw2.isAcceptingLinearState(q2))
                acceptingStates.add(qnew);
        }

        return linearStateList;
    }

    /**
     * Create transition functions from the transition functions of two automata and insert them into new automata
     * <p>
     * O(n^5) as we have two loops in which methods are called with O(n^3)
     *
     * @param lanwa1   first linear accepting nested-word automata for creating new transition functions
     * @param lanwa2   second linear accepting nested-word automata for creating new transition functions
     * @param lanwanew linear accepting nested-word automata new function should be added to
     */
    public void createTransitionFunctions(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {

        // loop ls1, loop ls2

        for (LinearState ls1 : lanwa1.getQ()
                ) {
            for (LinearState ls2 : lanwa2.getQ()) {
                createCallTransitions(ls1, ls2, lanwa1, lanwa2, lanwanew);
                createReturnTransitions(ls1, ls2, lanwa1, lanwa2, lanwanew);
                createInternalTransitions(ls1, ls2, lanwa1, lanwa2, lanwanew);
            }
        }

        // there must be created transitions with q^1 x discard and q^2 x discard
        createDiscardingCallTransitions(lanwa1, lanwa2, lanwanew);
        createDiscardingReturnTransitions(lanwa1, lanwa2, lanwanew);
        createDiscardingInternalTransitions(lanwa1, lanwa2, lanwanew);

    }


    /**
     * Create call transitions from both source automata in new new automaton from one state out of the states cross product of both automata
     * <p>
     * O(n^3) as we have 2 loops and one loop for finding transition
     *
     * @param ls1      first state
     * @param ls2      second state
     * @param lanwa1   first source automaton
     * @param lanwa2   second source automaton
     * @param lanwanew automaton the transition is added to
     */
    public void createCallTransitions(LinearState ls1, LinearState ls2, LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {
        for (CallTransitionFunction ctf1 : lanwa1.getDeltaC().findCallTransitionFunctions(ls1)) {
            boolean transitionFound = false;
            for (CallTransitionFunction ctf2 : lanwa2.getDeltaC().findCallTransitionFunctions(ls2)) {
                if (ctf1.getSymbol().equals(ctf2.getSymbol())) {
                    transitionFound = true;
                    LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                    LinearState qTarget = lanwanew.getLinearStateByName("(" + ctf1.getqTarget().getStateName() + "," + ctf2.getqTarget().getStateName() + ")");
                    HierarchyState p = lanwanew.getHierarchyStateByName("(" + ctf1.getP().getStateName() + "," + ctf2.getP().getStateName() + ")");
                    try {
                        lanwanew.getDeltaC().addTransitionFunction(qSource, ctf1.getSymbol(), qTarget, p, "(" + ctf1.getName() + "," + ctf2.getName() + ")");
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!transitionFound) {
                LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + ctf1.getqTarget().getStateName() + "," + discardingLinearState.getStateName() + ")");
                HierarchyState p = lanwanew.getHierarchyStateByName("(" + ctf1.getP().getStateName() + "," + discardingHierarchyState.getStateName() + ")");
                try {
                    lanwanew.getDeltaC().addTransitionFunction(qSource, ctf1.getSymbol(), qTarget, p, "(" + ctf1.getName() + "," + discardingLinearState.getStateName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

                for (CallTransitionFunction ctf2 : lanwa2.getDeltaC().findCallTransitionFunctions(ls2)) {
                    LinearState qSource2 = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                    LinearState qTarget2 = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + ctf2.getqTarget().getStateName() + ")");
                    HierarchyState p2 = lanwanew.getHierarchyStateByName("(" + discardingHierarchyState.getStateName() + "," + ctf2.getP().getStateName() + ")");
                    try {
                        lanwanew.getDeltaC().addTransitionFunction(qSource2, ctf2.getSymbol(), qTarget2, p2, "(" + discardingLinearState.getStateName() + "," + ctf2.getName() + ")");
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * create from both linear-accepting nested-word automata call transitions from states that are in the cross product of q^1 x discard and discard x q^2
     * <p>
     * O(n^3) as we have two loops and a search over transitions
     *
     * @param lanwa1   first source linear-accepting nested-word automaton
     * @param lanwa2   second source linear-accepting nested-word  automaton
     * @param lanwanew linear-accepting nested-word automaton the transitions are added to
     */
    public void createDiscardingCallTransitions(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {
        for (LinearState ls1 : lanwa1.getQ()) {
            for (CallTransitionFunction ctf1 : lanwa1.getDeltaC().findCallTransitionFunctions(ls1)) {
                LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + discardingLinearState.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + ctf1.getqTarget().getStateName() + "," + discardingLinearState.getStateName() + ")");
                HierarchyState p = lanwanew.getHierarchyStateByName("(" + ctf1.getP().getStateName() + "," + discardingHierarchyState.getStateName() + ")");
                try {
                    lanwanew.getDeltaC().addTransitionFunction(qSource, ctf1.getSymbol(), qTarget, p, "(" + ctf1.getName() + "," + discardingLinearState.getStateName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

            }
        }
        for (LinearState ls2 : lanwa2.getQ()) {
            for (CallTransitionFunction ctf2 : lanwa2.getDeltaC().findCallTransitionFunctions(ls2)) {

                LinearState qSource = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + ls2.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + ctf2.getqTarget().getStateName() + ")");
                HierarchyState p = lanwanew.getHierarchyStateByName("(" + discardingHierarchyState.getStateName() + "," + ctf2.getP().getStateName() + ")");
                try {
                    lanwanew.getDeltaC().addTransitionFunction(qSource, ctf2.getSymbol(), qTarget, p, "(" + discardingLinearState.getStateName() + "," + ctf2.getName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * Create return transitions from both source automata in new new automaton from one state out of the states cross product of both automata
     * <p>
     * O(n^3) as we have 2 loops and one loop for finding transition
     *
     * @param ls1      first state
     * @param ls2      second state
     * @param lanwa1   first source automaton
     * @param lanwa2   second source automaton
     * @param lanwanew automaton the transition is added to
     */
    public void createReturnTransitions(LinearState ls1, LinearState ls2, LinearAcceptingNestedWordAutomaton
            lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {
        for (ReturnTransitionFunction rtf1 : lanwa1.getDeltaR().findReturnTransitionFunctions(ls1)) {
            boolean transitionFound = false;
            for (ReturnTransitionFunction rtf2 : lanwa2.getDeltaR().findReturnTransitionFunctions(ls2)) {
                if (rtf1.getSymbol().equals(rtf2.getSymbol())) {
                    transitionFound = true;
                    LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                    LinearState qTarget = lanwanew.getLinearStateByName("(" + rtf1.getqTarget().getStateName() + "," + rtf2.getqTarget().getStateName() + ")");
                    HierarchyState p = lanwanew.getHierarchyStateByName("(" + rtf1.getP().getStateName() + "," + rtf2.getP().getStateName() + ")");
                    try {
                        lanwanew.getDeltaR().addTransitionFunction(qSource, p, rtf1.getSymbol(), qTarget, "(" + rtf1.getName() + "," + rtf2.getName() + ")");
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!transitionFound) {
                LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + rtf1.getqTarget().getStateName() + "," + discardingLinearState.getStateName() + ")");
                HierarchyState p = lanwanew.getHierarchyStateByName("(" + rtf1.getP().getStateName() + "," + discardingHierarchyState.getStateName() + ")");
                try {
                    lanwanew.getDeltaR().addTransitionFunction(qSource, p, rtf1.getSymbol(), qTarget, "(" + rtf1.getName() + "," + discardingLinearState.getStateName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

                for (ReturnTransitionFunction rtf2 : lanwa2.getDeltaR().findReturnTransitionFunctions(ls2)) {
                    LinearState qSource2 = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                    LinearState qTarget2 = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + rtf2.getqTarget().getStateName() + ")");
                    HierarchyState p2 = lanwanew.getHierarchyStateByName("(" + discardingHierarchyState.getStateName() + "," + rtf2.getP().getStateName() + ")");
                    try {
                        lanwanew.getDeltaR().addTransitionFunction(qSource2, p2, rtf2.getSymbol(), qTarget2, "(" + discardingLinearState.getStateName() + "," + rtf2.getName() + ")");
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * create from both linear-accepting nested-word automata return transitions from states that are in the cross product of q^1 x discard and discard x q^2
     * <p>
     * O(n^3) as we have two loops and a search over transitions
     *
     * @param lanwa1   first source linear-accepting nested-word automaton
     * @param lanwa2   second source linear-accepting nested-word  automaton
     * @param lanwanew linear-accepting nested-word automaton the transitions are added to
     */
    public void createDiscardingReturnTransitions(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {
        for (LinearState ls1 : lanwa1.getQ()) {
            for (ReturnTransitionFunction rtf1 : lanwa1.getDeltaR().findReturnTransitionFunctions(ls1)) {
                LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + discardingLinearState.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + rtf1.getqTarget().getStateName() + "," + discardingLinearState.getStateName() + ")");
                HierarchyState p = lanwanew.getHierarchyStateByName("(" + rtf1.getP().getStateName() + "," + discardingHierarchyState.getStateName() + ")");
                try {
                    lanwanew.getDeltaR().addTransitionFunction(qSource, p, rtf1.getSymbol(), qTarget, "(" + rtf1.getName() + "," + discardingLinearState.getStateName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

            }
        }
        for (LinearState ls2 : lanwa2.getQ()) {
            for (ReturnTransitionFunction rtf2 : lanwa2.getDeltaR().findReturnTransitionFunctions(ls2)) {

                LinearState qSource = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + ls2.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + rtf2.getqTarget().getStateName() + ")");
                HierarchyState p = lanwanew.getHierarchyStateByName("(" + discardingHierarchyState.getStateName() + "," + rtf2.getP().getStateName() + ")");
                try {
                    lanwanew.getDeltaR().addTransitionFunction(qSource, p, rtf2.getSymbol(), qTarget, "(" + discardingLinearState.getStateName() + "," + rtf2.getName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * Create internal transitions from both source automata in new new automaton from one state out of the states cross product of both automata
     * <p>
     * O(n^3) as we have 2 loops and one loop for finding transition
     *
     * @param ls1      first state
     * @param ls2      second state
     * @param lanwa1   first source automaton
     * @param lanwa2   second source automaton
     * @param lanwanew automaton the transition is added to
     */
    public void createInternalTransitions(LinearState ls1, LinearState ls2, LinearAcceptingNestedWordAutomaton
            lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {
        for (InternalTransitionFunction itf1 : lanwa1.getDeltaI().findInternalTransitionFunctions(ls1)) {
            boolean transitionFound = false;
            for (InternalTransitionFunction itf2 : lanwa2.getDeltaI().findInternalTransitionFunctions(ls2)) {
                if (itf1.getSymbol().equals(itf2.getSymbol())) {
                    transitionFound = true;
                    LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                    LinearState qTarget = lanwanew.getLinearStateByName("(" + itf1.getqTarget().getStateName() + "," + itf2.getqTarget().getStateName() + ")");
                    try {
                        lanwanew.getDeltaI().addTransitionFunction(qSource, itf1.getSymbol(), qTarget, "(" + itf1.getName() + "," + itf2.getName() + ")");
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!transitionFound) {
                LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + itf1.getqTarget().getStateName() + "," + discardingLinearState.getStateName() + ")");
                try {
                    lanwanew.getDeltaI().addTransitionFunction(qSource, itf1.getSymbol(), qTarget, "(" + itf1.getName() + "," + discardingLinearState.getStateName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

                for (InternalTransitionFunction itf2 : lanwa2.getDeltaI().findInternalTransitionFunctions(ls2)) {
                    LinearState qSource2 = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + ls2.getStateName() + ")");
                    LinearState qTarget2 = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + itf2.getqTarget().getStateName() + ")");
                    try {
                        lanwanew.getDeltaI().addTransitionFunction(qSource2, itf2.getSymbol(), qTarget2, "(" + discardingLinearState.getStateName() + "," + itf2.getName() + ")");
                    } catch (NestedWordSymbolAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * create from both linear-accepting nested-word automata internal transitions from states that are in the cross product of q^1 x discard and discard x q^2
     * <p>
     * O(n^3) as we have two loops and a search over transitions
     *
     * @param lanwa1   first source linear-accepting nested-word automaton
     * @param lanwa2   second source linear-accepting nested-word  automaton
     * @param lanwanew linear-accepting nested-word automaton the transitions are added to
     */
    public void createDiscardingInternalTransitions(LinearAcceptingNestedWordAutomaton lanwa1, LinearAcceptingNestedWordAutomaton lanwa2, LinearAcceptingNestedWordAutomaton lanwanew) {
        for (LinearState ls1 : lanwa1.getQ()) {
            for (InternalTransitionFunction itf1 : lanwa1.getDeltaI().findInternalTransitionFunctions(ls1)) {
                LinearState qSource = lanwanew.getLinearStateByName("(" + ls1.getStateName() + "," + discardingLinearState.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + itf1.getqTarget().getStateName() + "," + discardingLinearState.getStateName() + ")");

                try {
                    lanwanew.getDeltaI().addTransitionFunction(qSource, itf1.getSymbol(), qTarget, "(" + itf1.getName() + "," + discardingLinearState.getStateName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }

            }
        }
        for (LinearState ls2 : lanwa2.getQ()) {
            for (InternalTransitionFunction itf2 : lanwa2.getDeltaI().findInternalTransitionFunctions(ls2)) {

                LinearState qSource = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + ls2.getStateName() + ")");
                LinearState qTarget = lanwanew.getLinearStateByName("(" + discardingLinearState.getStateName() + "," + itf2.getqTarget().getStateName() + ")");
                try {
                    lanwanew.getDeltaI().addTransitionFunction(qSource, itf2.getSymbol(), qTarget, "(" + discardingLinearState.getStateName() + "," + itf2.getName() + ")");
                } catch (NestedWordSymbolAlreadyExistsException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}