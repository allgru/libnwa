package de.wwu.nwa.automata;

import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.exceptions.NestedEdgesCrossedException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.state.State;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Main Class for Nested Word Automata
 * <p>
 * Rajeev Alur and P. Madhusudan, "Adding Nesting Structure to Words",
 * Journal of the ACM 56(3). May 2009.
 * http://dx.doi.org/10.1145/1516512.1516518
 *
 * @author Allan Grunert
 */
public abstract class NestedWordAutomaton {
    protected Alphabet alphabet;
    protected ArrayList<HierarchyState> pendingCalls;
    protected ArrayList<HierarchyState> pendingReturns;
    // finite set of (linear) states
    protected ArrayList<LinearState> Q;
    // initial (linear) State
    protected LinearState q0;
    // set of (linear) final states
    protected ArrayList<LinearState> Qf;
    // set of hierarchical states
    protected ArrayList<HierarchyState> P;
    // initial hierarchical state
    protected HierarchyState p0;
    // set of hierarchical final states
    protected ArrayList<HierarchyState> Pf;
    protected HierarchyState currentHierarchyState;
    // call-transition function
    protected LinearState currentState;
    private String name;

    public NestedWordAutomaton() {
        this.P = new ArrayList<HierarchyState>();
        this.Pf = new ArrayList<HierarchyState>();
        this.Q = new ArrayList<LinearState>();
        this.Qf = new ArrayList<LinearState>();

        this.pendingCalls = new ArrayList<HierarchyState>();
        this.pendingReturns = new ArrayList<HierarchyState>();

        this.name = "";
    }

    public ArrayList<LinearState> getQ() {
        return Q;
    }

    public void setQ(ArrayList<LinearState> q) {
        Q = q;
    }

    public void addStateToQ(LinearState q) {
        Q.add(q);
    }

    public void addAllStatesToQ(Collection<LinearState> qs) {
        for (LinearState q : qs) {
            addStateToQ(q);
        }
    }

    public void addAllStatesToQf(Collection<LinearState> qs) {
        for (LinearState q : qs) {
            addStateToQf(q);
        }
    }

    public void addAllStatesToPf(Collection<HierarchyState> ps) {
        for (HierarchyState p : ps) {
            addHierarchyStateToPf(p);
        }
    }

    public void addAllStatesToP(Collection<HierarchyState> ps) {
        for (HierarchyState p : ps) {
            addHierarchyStateToP(p);
        }
    }

    public LinearState getQ0() {
        return q0;
    }

    public void setQ0(LinearState q0) {
        this.q0 = q0;
    }

    public ArrayList<LinearState> getQf() {
        return Qf;
    }

    public void setQf(ArrayList<LinearState> qf) {
        Qf = qf;
    }

    public void addStateToQf(LinearState q) {
        Qf.add(q);
    }

    public HierarchyState getP0() {
        return p0;
    }

    public void setP0(HierarchyState p0) {
        this.p0 = p0;
    }

    public ArrayList<HierarchyState> getPf() {
        return Pf;
    }

    public void setPf(ArrayList pf) {
        Pf = pf;
    }

    public ArrayList<HierarchyState> getP() {
        return P;
    }

    public void setP(ArrayList<HierarchyState> p) {
        P = p;
    }

    public void addHierarchyStateToP(HierarchyState p) {
        P.add(p);
    }

    public void addHierarchyStateToPf(HierarchyState p) {
        Pf.add(p);
    }

    public LinearState getLinearStateByName(String stateName) {
        for (LinearState q : this.Q
                ) {
            if (q.getStateName().equals(stateName))
                return q;
        }
        return null;
    }

    public HierarchyState getHierarchyStateByName(String stateName) {
        for (HierarchyState p : this.P
                ) {
            if (p.getStateName().equals(stateName))
                return p;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Remove hierarchy state from set of hierarchy states from automata
     * O(n + n) = O(n)
     *
     * @param pSrc hirtstchy state which should be removed from automata
     */
    public void removeHierarchyStateFromP(HierarchyState pSrc) {
        for (int i = 0; i < this.P.size(); i++) {
            if (pSrc.getStateName().equals(this.P.get(i).getStateName())) {
                this.removeHierarchyStateFromPf(this.P.get(i));
                this.P.remove(i);
                // TODO: transition
                break;
            }
        }
    }

    /**
     * Remove hierarchy state from set of accepting pending hierarchy states from automata
     * O(n)
     *
     * @param pSrc state that should be deleted
     */
    public void removeHierarchyStateFromPf(HierarchyState pSrc) {
        for (int i = 0; i < this.Pf.size(); i++) {
            if (pSrc.getStateName().equals(this.Pf.get(i).getStateName())) {
                this.Pf.remove(i);
                break;
            }
        }
    }

    /**
     * Remove linear state from set of hierarchy states from automata
     * O(n + n) = O(n)
     *
     * @param qSrc State that should be removed from accepting linear state and set of state of automata
     */
    public void removeLinearStateFromQ(LinearState qSrc) {
        for (int i = 0; i < this.Q.size(); i++) {
            if (qSrc.getStateName().equals(this.Q.get(i).getStateName())) {
                this.removeLinearStateFromQf(this.Q.get(i));
                this.Q.remove(i);
                break;
            }
        }
    }

    /**
     * Remove hierarchy state from set of accepting pending hierarchy states from automata
     * O(n)
     *
     * @param qSrc state that should be deleted
     */
    public void removeLinearStateFromQf(LinearState qSrc) {
        for (int i = 0; i < this.Qf.size(); i++) {
            if (qSrc.getStateName().equals(this.Qf.get(i).getStateName())) {
                this.Qf.remove(i);
                break;
            }
        }
    }

    /**
     * Check if linear state is accepting
     * O(n)
     *
     * @param q linear state which should be checked for acceptance
     * @return true if linear state is accepted
     */
    public boolean isAcceptingLinearState(LinearState q) {
        for (State qf : this.Qf) {
            if (qf.getStateName().equals(q.getStateName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if hierarchy state belongs to the pending states that are accepted
     * O(n)
     *
     * @param p pending state to be checked against list of accepting pending states
     * @return true, if pending state is accepted
     */
    public boolean isAcceptingHierarchyState(HierarchyState p) {
        for (HierarchyState pf : this.Pf) {
            if (pf.getStateName().equals(p.getStateName()))
                return true;
        }
        return false;
    }

    /**
     * Check if List of pending states are accepted by automata
     * O(n^2)
     *
     * @param pList List of pending states
     * @return true if all States of Pending states are accepted
     */
    public boolean isAcceptingHierarchyStateList(ArrayList<HierarchyState> pList) {
        for (HierarchyState p : pList) {
            if (!isAcceptingHierarchyState(p))
                return false;
        }
        return true;
    }

    /**
     * Check if linear state and List of Pending states is accepted by automata
     * O(n^2)
     *
     * @param q     State to check if it belongs to Qf
     * @param pList List of hierarchy states to check if they may be pending
     * @return true if all states (linear state and pending states) are accepted
     */
    public boolean isAcceptingStates(LinearState q, ArrayList<HierarchyState> pList) {
        if (isAcceptingLinearState(q)) {
            // Iterate through List of visited Hierarchy States
            // Not found accepted State exit
            if (pList != null && !isAcceptingHierarchyStateList(pList))
                return false;
            else
                return true;
        }
        return false;
    }

    /**
     * Gives back weather the automata accepts its current states
     *
     * @return true if pending states and linear state in which the automata stopped are accepting
     */
    public boolean isAcceptingStates() {
        return this.isAcceptingStates(this.currentState, this.pendingCalls);
    }

    /**
     * Check if linear state already exists in nwa
     * O(n)
     *
     * @param q State that should be checked for
     * @return true if state already exists in automata
     */
    public boolean linearStateExists(LinearState q) {
        for (State qs : this.Q) {
            if (qs.getStateName().equals(q.getStateName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if hierarchy state already exists in nwa
     * O(n)
     *
     * @param p State that should be checked for
     * @return true if state already exists in automata
     */
    public boolean hierarchyStateExists(HierarchyState p) {
        for (State ps : this.P) {
            if (ps.getStateName().equals(p.getStateName())) {
                return true;
            }
        }
        return false;
    }

    public HierarchyState getCurrentHierarchyState() {
        return currentHierarchyState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public void removeQf(LinearState linearState) {
        this.Qf.remove(linearState);
    }

    /**
     * Switch automata to its initial state
     */
    public void resetAutomata() {
        this.pendingCalls.clear();
        this.currentState = this.q0;
    }

    /**
     * Remove last element p from pending calls list
     *
     * @param p hierarchy state to be removed from pending call list
     * @throws NestedEdgesCrossedException Exception is thrown if pop is not possible.
     *                                     this means there are edges that cross
     */
    public void popPendingCall(HierarchyState p) throws NestedEdgesCrossedException {
        if (pendingCalls.size() > 0) {
            if (isLastPendingCall(p))
                popPendingCall();
            else
                throw new NestedEdgesCrossedException();
        }
    }

    /***
     * Removes last pending call
     */
    public void popPendingCall() {
        this.pendingCalls.remove(pendingCalls.size() - 1);
    }


    /**
     * Add pending call to list
     *
     * @param hierarchyState State that is pending
     */
    public void pushPendingCall(HierarchyState hierarchyState) {
        this.pendingCalls.add(hierarchyState);
    }


    /**
     * Check if last pending call is the pending call p
     *
     * @param p hierarchy state that should be checked against the last pending call
     * @return true, if last pending call equals p
     */
    public boolean isLastPendingCall(HierarchyState p) {
        if (this.pendingCalls.get(pendingCalls.size() - 1).getStateName().equals(p.getStateName()))
            return true;
        return false;
    }

    /**
     * Clears all states
     */
    public void clearStates() {
        this.setQ0(null);
        this.setQf(null);
        this.setQ(null);
        this.setP0(null);
        this.setPf(null);
        this.setP(null);
    }

    abstract public void processNestedWord(Run run);
}
