package de.wwu.nwa.automata.items.transition;

import de.wwu.nwa.automata.NestedWordAutomaton;

public class TransitionFunctions {
    protected int minWordLength;
    protected int maxWordLength;
    protected String name;
    protected NestedWordAutomaton nwa;

    public TransitionFunctions(NestedWordAutomaton nwa) {
        this.minWordLength = Integer.MAX_VALUE;
        this.maxWordLength = 0;
        this.nwa = nwa;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public boolean inWordLength(int length) {
        return ((this.getMaxWordLength() >= length) && (this.getMinWordLength() <= length));
    }

    public void recalculateMaxMinWordLength(String symbol) {
        if (getMaxWordLength() < symbol.length())
            this.maxWordLength = symbol.length();

        if (getMinWordLength() > symbol.length())
            this.minWordLength = symbol.length();
    }

}
