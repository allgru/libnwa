package de.wwu.nwa.automata;

import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.exceptions.NestedEdgesCrossedException;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.node.CallNode;
import de.wwu.nwa.automata.items.node.InternalNode;
import de.wwu.nwa.automata.items.node.ReturnNode;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.state.State;
import de.wwu.nwa.automata.items.transition.*;

import java.util.ArrayList;

/**
 * Implementation of a common nested-word automata
 * <p>
 * Rajeev Alur and P. Madhusudan, "Adding Nesting Structure to Words",
 * Journal of the ACM 56(3). May 2009.
 * http://dx.doi.org/10.1145/1516512.1516518
 *
 * @author Allan Grunert
 */
public class CommonNestedWordAutomaton extends NestedWordAutomaton {

    // call-transition function
    protected CallTransitionFunctions deltaC;
    // internal-transition function
    protected InternalTransitionFunctions deltaI;
    // return-transition function
    protected ReturnTransitionFunctions deltaR;

    // this state is needed if a transition is missing
    protected LinearState discardingState;

    public CommonNestedWordAutomaton() {
        super();
        deltaC = new CallTransitionFunctions(this);
        deltaR = new ReturnTransitionFunctions(this);
        deltaI = new InternalTransitionFunctions(this);
        discardingState = new LinearState("q_v");
    }

    public CallTransitionFunctions getDeltaC() {
        return deltaC;
    }

    public void setDeltaC(CallTransitionFunctions deltaC) {
        this.deltaC = deltaC;
    }

    public InternalTransitionFunctions getDeltaI() {
        return deltaI;
    }

    public void setDeltaI(InternalTransitionFunctions deltaI) {
        this.deltaI = deltaI;
    }

    public ReturnTransitionFunctions getDeltaR() {
        return deltaR;
    }

    public void setDeltaR(ReturnTransitionFunctions deltaR) {
        this.deltaR = deltaR;
    }

    /**
     * Method to process input Strings
     * <p>
     * Strategy:
     * - First search through the call functions
     * - Secondly search through the return functions
     * - Finally search through the internal functions
     * We take the word and start with the maximum length
     * of the accepted words in each function
     * then we go down to the minimum length.
     **/
    public void processNestedWord(Run run) {
        this.currentState = this.q0;

        // Create initialising node for linear starting state
        InternalNode initialNode = new InternalNode();
        initialNode.setPosition(0);
        initialNode.setSymbol("");
        initialNode.setLinearState(this.q0);
        run.addStep(initialNode);

        // iterate through nested word
        for (int i = 0; i < run.getNw().length(); i++) {
            String symbol = run.getNw().getSymbol(i);

            System.out.println(i);

            // check what type the symbol in the nested word is
            if (run.getNw().isCallPosition(i)) {
                // do transition and get linear state after call transition
                LinearState state = this.deltaC.transitionState(this.currentState, symbol);

                if (state != null) {
                    // create new call node for run
                    CallNode cn = new CallNode();
                    cn.setSymbol(symbol);
                    cn.setPosition(i + 1);

                    cn.setState(state); // set linear state

                    // get hierarchy state after call transition
                    HierarchyState hs = this.deltaC.transitionHierarchyState(this.currentState, symbol);
                    cn.setHierarchyState(hs);

                    // push call on stack for pending
                    // the stack is later analysed considerating
                    // if the automata is in accepting state
                    this.pushPendingCall(hs);
                    cn.setState(state);
                    run.addStep(cn);

                    this.currentHierarchyState = hs;
                    this.currentState = state;
                } else
                    this.currentState = this.discardingState; // there is call symbol that has no transition from source state

            } else {
                // if symbol is return
                if (run.getNw().isReturnPosition(i)) {
                    // do transition

                    // there is a pending-return
                    if (this.currentHierarchyState == null)
                        this.currentHierarchyState = this.getP0();

                    LinearState state = this.deltaR.transitionState(this.currentState, symbol, this.currentHierarchyState);
                    // is there a state
                    if (state != null) {
                        ReturnNode rn = new ReturnNode();
                        rn.setPosition(i + 1);
                        rn.setLastHierarchyState(this.currentHierarchyState);
                        rn.setSymbol(symbol);
                        if (this.pendingCalls.size() == 0)
                            rn.setPending(true);
                        else
                            rn.setPending(false);
                        try {
                            this.popPendingCall(this.currentHierarchyState);
                        } catch (NestedEdgesCrossedException e) {
                            e.printStackTrace();
                        }
                        if (this.pendingCalls.size() == 0) // pending return, as there are no pending calls
                        {
                            this.currentHierarchyState = this.p0;

                        } else {
                            this.currentHierarchyState = this.pendingCalls.get(this.pendingCalls.size() - 1);

                        }
                        rn.setHierarchyState(this.currentHierarchyState);
                        rn.setState(state);
                        run.addStep(rn);
                        this.currentState = state;
                    } else
                        this.currentState = this.discardingState; // there is return symbol that has no transition from source state

                } else if (run.getNw().isInternalPosition(i)) {
                    LinearState state = this.deltaI.transitionState(this.currentState, symbol);
                    if (state != null) {
                        InternalNode in = new InternalNode();
                        in.setPosition(i + 1);
                        in.setHierarchyState(this.currentHierarchyState);
                        in.setSymbol(symbol);
                        in.setState(state);
                        this.currentState = state;
                        in.setState(state);
                        run.addStep(in);
                    } else
                        this.currentState = this.discardingState; // there is internal symbol that has no transition from source state
                }
            }
        }
    }


    /**
     * Makes a hard copy of this automata
     *
     * @return new nested-word automata
     */
    public CommonNestedWordAutomaton clone() {
        // create new Automata
        CommonNestedWordAutomaton newNWA = new CommonNestedWordAutomaton();

        // create and set starting states
        HierarchyState p0 = new HierarchyState(this.getP0().getStateName());
        newNWA.setP0(p0);
        LinearState q0 = new LinearState(this.getQ0().getStateName());
        newNWA.setQ0(q0);

        // create all hierarchical states
        ArrayList<HierarchyState> pList = new ArrayList<HierarchyState>();
        for (HierarchyState p : this.getP()) {
            if (!p0.getStateName().equals(p.getStateName()))
                pList.add(new HierarchyState(p.getStateName()));
            else
                pList.add(p0);
        }
        newNWA.setP(pList);

        ArrayList<HierarchyState> pfList = new ArrayList<HierarchyState>();
        for (HierarchyState pf : this.getPf()) {
            if (!p0.getStateName().equals(pf.getStateName()))
                pfList.add(new HierarchyState(pf.getStateName()));
            else
                pfList.add(p0);
        }
        newNWA.setPf(pfList);

        // Add all Hierarchical states
        ArrayList<LinearState> qList = new ArrayList<LinearState>();
        for (LinearState q : this.getQ()) {
            if (!q0.getStateName().equals(q.getStateName()))
                qList.add(new LinearState(q.getStateName()));
            else
                qList.add(q0);
        }
        newNWA.setQ(qList);

        ArrayList<LinearState> qfList = new ArrayList<LinearState>();
        for (State qf : this.getQ()) {
            if (!q0.getStateName().equals(qf.getStateName()))
                qfList.add(new LinearState(qf.getStateName()));
            else
                qfList.add(q0);
        }
        newNWA.setQf(qfList);

        // Add transition functions
        CallTransitionFunctions deltaC = new CallTransitionFunctions(this);

        for (CallTransitionFunction ctf : this.getDeltaC().getDeltaC()) {
            try {
                deltaC.addTransitionFunction(ctf.getqSource(), ctf.getSymbol(), ctf.getqTarget(), ctf.getP());
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        newNWA.setDeltaC(deltaC);

        InternalTransitionFunctions deltaI = new InternalTransitionFunctions(this);

        for (InternalTransitionFunction itf : this.getDeltaI().getDeltaI()) {
            try {
                deltaI.addTransitionFunction(itf.getqSource(), itf.getSymbol(), itf.getqTarget());
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        newNWA.setDeltaI(deltaI);

        ReturnTransitionFunctions deltaR = new ReturnTransitionFunctions(this);

        for (ReturnTransitionFunction rtf : this.getDeltaR().getDeltaR()) {
            try {
                deltaR.addTransitionFunction(rtf.getqSource(), rtf.getP(), rtf.getSymbol(), rtf.getqTarget());
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        newNWA.setDeltaC(deltaC);

        return newNWA;
    }

    /**
     * Method for printing out all States and Transitions
     */
    public void printAll() {
        System.out.println("States");
        System.out.println("******");
        printStates();
        System.out.println("Transitions");
        System.out.println("***********");
        printTransitions();

    }

    /**
     * print information on states
     */
    public void printStates() {
        printLinearStates();
        printHierarchyStates();
    }

    /**
     * print information on linear states
     */
    public void printLinearStates() {
        System.out.println("Linear States");
        System.out.println("-------------");
        for (LinearState q : this.getQ()
                ) {
            System.out.println("" + q.getStateName());
        }
        System.out.println("Linear Accepting States");
        System.out.println("-----------------------");
        for (LinearState q : this.getQf()
                ) {
            System.out.println("" + q.getStateName());
        }

        if (this.getQ0() != null)
            System.out.println("Initial Linear State: " + this.getQ0().getStateName());

    }

    /**
     * print information on hierarchical states
     */
    public void printHierarchyStates() {
        System.out.println("Hierarchy States");
        System.out.println("----------------");
        for (HierarchyState p : this.getP()
                ) {
            System.out.println("" + p.getStateName());
        }

        System.out.println("Hierarchy accepting States");
        System.out.println("--------------------------");
        for (HierarchyState p : this.getPf()
                ) {
            System.out.println("" + p.getStateName());
        }

        if (this.getQ0() != null)
            System.out.println("Initial Hierarchy State: " + this.getP0().getStateName());
    }

    /**
     * print information on transitions
     */
    public void printTransitions() {
        System.out.println("Call Transitions");
        printCallTransitions();
        System.out.println("Internal Transitions");
        printInternalTransitions();
        System.out.println("Return Transitions");
        printReturnTransitions();
    }

    /**
     * print information on call transitions
     */
    public void printCallTransitions() {
        for (CallTransitionFunction ctf : this.getDeltaC().getDeltaC()
                ) {
            System.out.println("(q:" + ctf.getqSource().getStateName() + ",s:" + ctf.getSymbol()
                    + ") -> (q:" + ctf.getqTarget().getStateName() + ",p:" + ctf.getP().getStateName() + ")");
        }
    }

    /**
     * print informartion on internal transitions
     */
    public void printInternalTransitions() {
        for (InternalTransitionFunction itf : this.getDeltaI().getDeltaI()
                ) {
            System.out.println("(q:" + itf.getqSource().getStateName()
                    + ",s:" + itf.getSymbol() + ") -> q:" + itf.getqTarget().getStateName());
        }
    }

    /**
     * print information on return transitions
     */
    public void printReturnTransitions() {
        for (ReturnTransitionFunction rtf : this.getDeltaR().getDeltaR()
                ) {
            System.out.println("(q:" + rtf.getqSource().getStateName() + ",p:" + rtf.getP().getStateName()
                    + ",s:" + rtf.getSymbol() + ") -> q:" + rtf.getqTarget().getStateName());
        }
    }

    /**
     * Clear all transitions
     */
    public void clearTransitions() {
        this.deltaC.getDeltaC().clear();
        this.deltaI.getDeltaI().clear();
        this.deltaR.getDeltaR().clear();
    }

    /**
     * Clear all items
     */
    public void clearAll() {
        clearStates();
        clearTransitions();
        this.pendingCalls.clear();
        this.pendingReturns.clear();
    }
}
