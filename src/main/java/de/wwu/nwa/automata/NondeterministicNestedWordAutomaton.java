package de.wwu.nwa.automata;

import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.node.CallNode;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.state.State;
import de.wwu.nwa.automata.items.transition.CallTransitionRelations;
import de.wwu.nwa.automata.items.transition.InternalTransitionRelations;
import de.wwu.nwa.automata.items.transition.ReturnTransitionRelations;

import java.util.ArrayList;

/**
 * Class that represents a nondeterministic Nested-Word automata (not yet implemented)
 *
 * @author Allan Grunert
 */
public class NondeterministicNestedWordAutomaton extends NestedWordAutomaton {
    // call-transition relation
    private CallTransitionRelations deltaC;
    // internal-transition relation
    private InternalTransitionRelations deltaI;
    // return-transition relation
    private ReturnTransitionRelations deltaR;

    public CallTransitionRelations getDeltaC() {
        return deltaC;
    }

    public void setDeltaC(CallTransitionRelations deltaC) {
        this.deltaC = deltaC;
    }

    public InternalTransitionRelations getDeltaI() {
        return deltaI;
    }

    public void setDeltaI(InternalTransitionRelations deltaI) {
        this.deltaI = deltaI;
    }

    public ReturnTransitionRelations getDeltaR() {
        return deltaR;
    }

    public void setDeltaR(ReturnTransitionRelations deltaR) {
        this.deltaR = deltaR;
    }


    public void processNestedWord(Run run) {
        int pos = 0;

        this.currentState = this.q0;

        // Initalize Stack
        // this.stackCallNode.clear();
        CallNode p0Node = new CallNode();
        p0Node.setState(q0);
        p0Node.setHierarchyState(this.p0);
        // this.stackCallNode.push(p0Node);

        boolean resume;
        // Iterate through word
        while (pos < run.getNw().length()) {
            resume = false;
            int oldPos = pos;

            // call
            for (int i = this.deltaC.getMaxWordLength(); i >= this.deltaC.getMinWordLength(); i--) {
                if (run.getNw().length() >= pos + i) {
                    String symbol = run.getNw().getSymbol(pos).substring(pos, pos + i);
                    if (this.deltaC.transitionFulfilled(this.currentState, symbol)) {
                        System.out.print("<a");
                        CallNode cn = new CallNode();
                        HierarchyState hs = this.deltaC.transitionHierarchyState(this.currentState, symbol);
                        cn.setHierarchyState(hs);
                        this.currentHierarchyState = hs;
                        LinearState state = this.deltaC.transitionState(this.currentState, symbol);
                        cn.setState(state);
                        this.currentState = state;
                        cn.setSymbol(symbol);
//                        this.stackCallNode.push(cn);
//                        this.stackNode.push(cn);
                        pos += i;
                        resume = true;
                        break;
                    }
                }
            }
        }

        // Return
/*            if (!resume)
                for (int i = this.deltaR.getMaxWordLength(); i >= this.deltaR.getMinWordLength(); i--) {
                    if (run.getNw().getSymbol(pos).length() >= pos + i) {
                        String symbol = run.getNw().getSymbol(pos).substring(pos, pos + i);
                        if (this.deltaR.transitionFulfilled(this.currentState, symbol, this.stackCallNode.lastElement().getHierarchyState())) {
                            System.out.print("a>");
                            ReturnNode rn = new ReturnNode();
                            this.stackCallNode.lastElement().setReturnNode(rn);
                            LinearState state = this.deltaR.transitionState(this.currentState, symbol, this.stackCallNode.lastElement().getHierarchyState());
                            rn.setState(state);
                            this.currentState = state;
                            this.stackCallNode.pop();
                            HierarchyState hs = this.stackCallNode.lastElement().getHierarchyState();
                            rn.setHierarchyState(hs);
                            this.currentHierarchyState = hs;
                            rn.setSymbol(symbol);
                            this.stackNode.push(rn);
                            pos += i;
                            resume = true;
                            break;
                        }
                    }
                }

            // Internal
            if (!resume)
                for (int i = this.deltaI.getMaxWordLength(); i >= this.deltaI.getMinWordLength(); i--) {
                    if (run.getNw().getSymbol(pos).length() >= pos + i) {
                        String symbol = run.getNw().getSymbol(pos).substring(pos, pos + i);
                        if (this.deltaI.transitionFulfilled(this.currentState, symbol)) {
                            System.out.print("a");
                            InternalNode in = new InternalNode();
                            this.stackCallNode.lastElement().addInternalNode(in);
                            LinearState state = this.deltaI.transitionState(this.currentState, symbol);
                            in.setHierarchyState(this.stackCallNode.lastElement().getHierarchyState());
                            in.setSymbol(symbol);
                            in.setState(state);
                            this.currentState = state;
                            this.stackNode.push(in);
                            pos += i;
                            break;
                        }
                    }
                }



            // Word is not in Language
            if (pos == oldPos) {
                System.out.println();
                System.out.println("Language not accepted" + run.getNw().getSymbol(pos).substring(pos));
                // TODO: Info Proecessed Word is not in Language
                break;
            }

        }
        System.out.println();*/
    }

    public NondeterministicNestedWordAutomaton clone() {
        NondeterministicNestedWordAutomaton newNWA = new NondeterministicNestedWordAutomaton();

        newNWA.setP0(new HierarchyState(this.getP0().getStateName()));
        newNWA.setQ0(new LinearState(this.getQ0().getStateName()));

        ArrayList<HierarchyState> pList = new ArrayList<HierarchyState>();
        for (HierarchyState p : this.getP()) {
            pList.add(new HierarchyState(p.getStateName()));
        }
        newNWA.setP(pList);

        ArrayList<HierarchyState> pfList = new ArrayList<HierarchyState>();
        for (HierarchyState pf : this.getPf()) {
            pfList.add(new HierarchyState(pf.getStateName()));
        }
        newNWA.setPf(pfList);

        ArrayList<LinearState> qList = new ArrayList<LinearState>();
        for (LinearState q : this.getQ()) {
            qList.add(new LinearState(q.getStateName()));
        }

        ArrayList<LinearState> qfList = new ArrayList<LinearState>();
        for (State qf : this.getQ()) {
            qfList.add(new LinearState(qf.getStateName()));
        }
        newNWA.setQf(qfList);

        // TODO: Clone Transition RElations

        return newNWA;
    }

}
