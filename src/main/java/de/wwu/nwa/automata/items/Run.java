package de.wwu.nwa.automata.items;

import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.items.exceptions.NestedEdgeSharePositionException;
import de.wwu.nwa.automata.items.exceptions.NestedEdgesCrossedException;
import de.wwu.nwa.automata.items.node.CallNode;
import de.wwu.nwa.automata.items.node.Node;
import de.wwu.nwa.automata.items.node.ReturnNode;

import java.util.ArrayList;

/**
 * Class that represents a run over nested word
 *
 * @author Allan Grunert
 */
public class Run {
    private ArrayList<Node> nodes;
    private NestedWord nw;
    private int position;
    private NestedWordAutomaton nwa;

    public Run(NestedWord nw) {
        this.position = 0;
        this.nw = nw;
        nodes = new ArrayList<Node>();
    }

    public NestedWord getNw() {
        return nw;
    }

    public void setNw(NestedWord nw) {
        this.nw = nw;
    }

    public void addStep(Node node) {
        node.setPosition(this.position);

        // TODO: first position must be initial state otherwise error

        if (node.getType() == Node.CallNode) {
            int returnPosition = getNw().getReturnPosition(this.position - 1);
            if (returnPosition < 0)
                node.setPending(true);
        }

        if (node.getType() == Node.ReturnNode) {
            int callPosition = getNw().getCallPosition(this.position - 1);
            if (callPosition >= 0) {
                ((CallNode) nodes.get(callPosition + 1)).setReturnNode((ReturnNode) node);
            } else {
                node.setPending(true);
            }
        }

        this.nodes.add(node);
        this.position++;
    }

    /**
     * Give Run Item at certain step back
     *
     * @param i Step of which the RunItem should be given back
     * @return Corresponding RunItem at Step i, if not available null
     */
    public Node getStep(int i) {
        if (i - 1 < this.nodes.size() && i > 0)
            return this.nodes.get(i - 1);
        else return null;
    }

    public int length() {
        return this.position - 1;
    }

    public NestedWordAutomaton getNwa() {
        return nwa;
    }

    public void setNwa(NestedWordAutomaton nwa) {
        this.nwa = nwa;
    }

    /**
     * Proecess nested word with given automata
     *
     * @return return if word is accepted true if yes, false if no
     * @throws NestedEdgeSharePositionException is thrown if there is a return and call sharing the same position
     * @throws NestedEdgesCrossedException      is thrown if return and call-edge are overlapping
     */
    public boolean getInputWordAccepted() throws NestedEdgeSharePositionException, NestedEdgesCrossedException {

        nw.resetEdgeIndex();
        nwa.resetAutomata();
        this.position = 0;
        this.nodes.clear();
        // this.nodes.clear();

        // Test if nested word is valid
        int nwValid = nw.isValidNestedWord();

        if (nwValid == 2) {
            throw new NestedEdgeSharePositionException();
        }

        // TODO: correct
/*        if (nwValid ==3) {
                throw new NestedEdgesCrossedException();
        }
*/
        getNwa().processNestedWord(this);

        return this.nwa.isAcceptingStates();
    }

    public int size() {
        return this.position;
    }

}
