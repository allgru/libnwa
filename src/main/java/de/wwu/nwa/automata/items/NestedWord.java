package de.wwu.nwa.automata.items;

import de.wwu.nwa.automata.items.exceptions.InputWordNotInAlphabeth;

import java.util.ArrayList;
import java.util.Vector;


/**
 * Nested Word is a word which consists of symbols and nested edges
 * and can be read by a Nested-word automaton
 *
 * @author Allan Grunert
 */
public class NestedWord {
    final private int plusinf = -1;
    final private int minusinf = -2;
    private ArrayList<Vector<Integer>> nestedEdges;
    private ArrayList<String> symbols;
    private Alphabet alphabet;
    private int lastCallIndex = 0;
    private int lastReturnIndex = 0;

    public NestedWord() {
        nestedEdges = new ArrayList<Vector<Integer>>();
        symbols = new ArrayList<String>();
        alphabet = new Alphabet();
    }

    public void resetEdgeIndex() {
        this.lastCallIndex = 0;
        this.lastReturnIndex = 0;
    }

    public int getCallPosition(int returnPosition) {
        for (Vector<Integer> ne : this.nestedEdges
                ) {
            if (ne.get(1) == returnPosition)
                return ne.get(0);
        }
        return -3;
    }

    public int getReturnPosition(int callPosition) {
        for (Vector<Integer> ne : this.nestedEdges
                ) {
            if (ne.get(0) == callPosition)
                return ne.get(1);
        }
        return -3;
    }


    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public String getSymbol(int i) {
        return symbols.get(i);
    }

    public boolean isCallPosition(int pos) {
        for (int i = lastCallIndex; i < this.nestedEdges.size(); i++) {
            if (this.nestedEdges.get(i).get(0) == pos) {
                // lastCallIndex = i + 1;
                return true;
            } /* else if (this.nestedEdges.get(i).get(0) > pos) {
                lastCallIndex = i;
                return false;
            }*/

        }
        return false;
    }


    /**
     * Check if symbol at position has a return of a nested edge
     *
     * @param pos Position of symbol on nested word
     * @return true if return exists at position
     */
    public boolean isReturnPosition(int pos) {
        for (int i = lastReturnIndex; i < this.nestedEdges.size(); i++) {
            if (this.nestedEdges.get(i).get(1) == pos) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if symbol at position has no hierarchical quality
     *
     * @param pos position of symbol on nested word
     * @return true if symbol has no hierarchica quality
     */
    public boolean isInternalPosition(int pos) {
        for (int i = lastReturnIndex; i < this.nestedEdges.size(); i++) {
            if (this.nestedEdges.get(i).get(0) == pos || this.nestedEdges.get(i).get(1) == pos) {
                return false;
            }
        }
        return true;
    }

    public int length() {
        return symbols.size();
    }

    public void addSymbol(String symbol) throws InputWordNotInAlphabeth {
        if (alphabet.inAlphabet(symbol))
            symbols.add(symbol);
        else
            throw new InputWordNotInAlphabeth();
    }

    /**
     * Add nested Edge
     *
     * @param from Index of word nested comes from
     * @param to   Index of word nested edge goes to
     */
    public void addNestedEdge(int from, int to) {
        // create new edge
        Vector<Integer> nestingEdge = new Vector<Integer>();
        nestingEdge.add(from);
        nestingEdge.add(to);
        // add it to nesting edges
        nestedEdges.add(nestingEdge);
    }

    /**
     * Adds pending Return Edge
     *
     * @param to Index of word the return edge goes to
     */
    public void addPendingReturnEdge(int to) {
        this.addNestedEdge(minusinf, to);
    }

    /**
     * Add pending Call Edge
     *
     * @param from Index of word the pending edge comes from
     */
    public void addPendingCallEdge(int from) {
        this.addNestedEdge(plusinf, from);
    }

    /**
     * Checks if nested edge is well matched
     *
     * @return true if is well matched
     */
    public boolean isWellMatched() {
        // O(n)
        for (Vector<Integer> nestedEdge : nestedEdges) {
            if (nestedEdge.get(0) == minusinf || nestedEdge.get(1) == plusinf)
                return false;
        }
        return true;
    }

    /**
     * Gives back if word is rooted
     *
     * @return returns true if word has no pending calls and returns
     */
    public boolean isRooted() {
        for (Vector<Integer> pendingEdge : getPendingEdges()
                ) {
            if (pendingEdge.get(0) == this.plusinf)
                return false;
            if (pendingEdge.get(1) == this.minusinf)
                return false;
        }
        return true;
    }

    /**
     * Returns all Pending edges of word
     *
     * @return List of pending edges
     */
    public ArrayList<Vector<Integer>> getPendingEdges() {
        ArrayList<Vector<Integer>> pendingEdges = new ArrayList<Vector<Integer>>();
        // O(n)
        for (Vector<Integer> nestedEdge : nestedEdges) {
            if (nestedEdge.get(1) == minusinf || nestedEdge.get(0) == plusinf)
                pendingEdges.add(nestedEdge);
        }

        return pendingEdges;
    }

    /**
     * Checks if nested Word complies to all requirements in alur2009
     * 1. Nesting words run forward -&gt; garanteed through implementation
     * 2. Return and Call may not have same Word index
     * 3. two pending edges may not cross
     *
     * @return 0 if valid, 2 if condition 2., 3 if condition 3. (see discription)
     */
    public int isValidNestedWord() {
        // 2. O(n^2)
        for (Vector<Integer> nestedEdge : nestedEdges) {
            for (Vector<Integer> nestedEdge2 : nestedEdges) {
                if (nestedEdge.get(0) == nestedEdge2.get(1))
                    return 2;
            }

        }

        // 3. O(n^2)
        for (Vector<Integer> nestedEdge : nestedEdges) {
            for (Vector<Integer> nestedEdge2 : nestedEdges) {
                if (nestedEdge.get(0) != nestedEdge2.get(0) && nestedEdge.get(1) != nestedEdge2.get(1)) {
                    // check pending edges
                    if (nestedEdge.get(0) == this.minusinf) // pending return edge is inside of matched relation
                    {
                        if (nestedEdge.get(1) > nestedEdge2.get(0) && nestedEdge.get(1) < nestedEdge2.get(1))
                            return 3;
                    } else if (nestedEdge.get(1) != this.plusinf) // pending call edge is inside matched relation
                    {
                        if (nestedEdge.get(0) > nestedEdge2.get(0) && nestedEdge.get(0) < nestedEdge2.get(1))
                            return 3;

                    } else {
                        if (nestedEdge.get(0) > nestedEdge2.get(0) && nestedEdge.get(0) < nestedEdge2.get(1) && nestedEdge.get(1) > nestedEdge2.get(1))
                            return 3;
                        if (nestedEdge.get(0) < nestedEdge2.get(0) && nestedEdge.get(1) > nestedEdge2.get(0) && nestedEdge.get(1) < nestedEdge2.get(1))
                            return 3;
                    }
                }
            }

        }

        return 0;
    }

    /**
     * Print information on nested Word
     */
    public void print() {
        System.out.println("Nested Word");
        System.out.println("-----------");
        for (int i = 0; i < this.symbols.size(); i++
                ) {
            System.out.print((i + 1) + ". ");
            System.out.print(symbols.get(i));
            if (isCallPosition(i)) {
                System.out.print(" (c)");
                int returnPosition = this.getReturnPosition(i);
                if (returnPosition >= 0)
                    System.out.print("return: " + this.symbols.get(returnPosition));
                else
                    System.out.print("pending");

            }
            if (isReturnPosition(i)) {
                System.out.print(" (r) ");
                int callPosition = this.getCallPosition(i);
                if (callPosition >= 0)
                    System.out.print("call: " + this.symbols.get(callPosition));
                else
                    System.out.println("pending");
            }
            System.out.println();
        }
        System.out.println();
    }

    public ArrayList<Vector<Integer>> getNestedEdges() {
        return nestedEdges;
    }

    public void setNestedEdges(ArrayList<Vector<Integer>> nestedEdges) {
        this.nestedEdges = nestedEdges;
    }

    /**
     * deletes all elements from nested word
     */
    public void clear() {
        this.symbols.clear();
        this.nestedEdges = new ArrayList<Vector<Integer>>();
        resetEdgeIndex();
    }

    /**
     * removes nested edge at particular position
     *
     * @param i (array) position of pair of nested edge that should be deleted
     */
    public void removeNestingEdge(int i) {
        this.nestedEdges.remove(i);
    }

    /**
     * removes symbol at particular position
     *
     * @param i (array) position of symbol that should be deleted
     */
    public void removeSymbol(int i) {
        this.symbols.remove(i);
    }

    /**
     * Give back a list of call position of the word
     *
     * @return call position in form of a list of numbers
     */
    public ArrayList<Integer> getCallPositions() {
        ArrayList<Integer> cpList = new ArrayList<Integer>();

        for (int i = 0; i < this.length(); i++) {
            if (this.alphabet.getTypeOfSymbol(symbols.get(i)) == Alphabet.CALL_POSITION)
                cpList.add(i);
        }

        return cpList;
    }

    /**
     * Give back a list of return position of the word
     *
     * @return return position in form of a list of numbers
     */
    public ArrayList<Integer> getReturnPositions() {
        ArrayList<Integer> rpList = new ArrayList<Integer>();

        for (int i = 0; i < this.length(); i++) {
            if (this.alphabet.getTypeOfSymbol(symbols.get(i)) == Alphabet.RETURN_POSITION)
                rpList.add(i);
        }

        return rpList;
    }

}
