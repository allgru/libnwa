package de.wwu.nwa.automata.items;

import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolNotFoundException;

import java.util.ArrayList;

/**
 * This class gives the oportunity to create an alphabet for nested words
 * it helps prepare the alphabet for other classe by seperating the alphabets
 * symbols in call, return and internal-symbols.
 * <p>
 * Symbols are represented by Strings. The different types are implemented as differen lists
 *
 * @author Allan Grunert
 */
public class Alphabet {

    // Normally Sigma would be one set of symbols

    // Identifyer for different types
    public static final int INTERNAL_POSITION = 0;
    public static final int CALL_POSITION = 1;
    public static final int RETURN_POSITION = 2;
    // Sigma
    private ArrayList<String> internalAlphabet;
    // <Sigma
    private ArrayList<String> callAlphabet;
    // Sigma>
    private ArrayList<String> returnAlphabet;
    // min and max lengths for optimisation of word interpretation
    private int minInternalSymbolLength;
    private int maxInternalSymbolLength;

    private int minCallSymbolLength;
    private int maxCallSymbolLength;

    private int minReturnSymbolLength;
    private int maxReturnSymbolLength;


    // initalize all member fields
    public Alphabet() {
        this.internalAlphabet = new ArrayList<String>();
        this.callAlphabet = new ArrayList<String>();
        this.returnAlphabet = new ArrayList<String>();

        this.minInternalSymbolLength = Integer.MAX_VALUE;
        this.minCallSymbolLength = Integer.MAX_VALUE;
        this.minReturnSymbolLength = Integer.MAX_VALUE;

        this.maxInternalSymbolLength = 0;
        this.maxCallSymbolLength = 0;
        this.maxReturnSymbolLength = 0;

        this.minInternalSymbolLength = Integer.MAX_VALUE;
        this.minCallSymbolLength = Integer.MAX_VALUE;
        this.minReturnSymbolLength = Integer.MAX_VALUE;
    }

    /**
     * Add Symbol to Alphabet
     * <p>
     * O(n)
     *
     * @param symbol     Symbol to be added
     * @param symbolType Type of Symbol that should be added
     * @throws NestedWordSymbolAlreadyExistsException is thrown if symbol is already in Alphabet
     */
    public void addSymbol(String symbol, int symbolType) throws NestedWordSymbolAlreadyExistsException {
        if (symbolType < 0 || symbolType > 2)
            throw new IllegalArgumentException();

        // check if symbol is in Alphabet
        if (this.inAlphabet(symbol))
            throw new NestedWordSymbolAlreadyExistsException();

        switch (symbolType) {
            case 0: //this.INTERNAL_POSITION:
                // set min and max length
                if (this.minInternalSymbolLength > symbol.length())
                    this.minInternalSymbolLength = symbol.length();
                if (this.maxInternalSymbolLength < symbol.length())
                    this.maxInternalSymbolLength = symbol.length();

                this.internalAlphabet.add(symbol);
                break;
            case 1: // this.CALL_POSITION:
                // set min and max length
                if (this.minCallSymbolLength > symbol.length())
                    this.minCallSymbolLength = symbol.length();
                if (this.maxCallSymbolLength < symbol.length())
                    this.maxCallSymbolLength = symbol.length();

                this.callAlphabet.add(symbol);
                break;
            case 2: // this.RETURN_POSITION:
                // check if word exists
                for (String returnSymbol : this.returnAlphabet
                        ) {
                    if (returnSymbol.equals(symbol))
                        throw new NestedWordSymbolAlreadyExistsException();
                }
                // set min and max length
                if (this.minReturnSymbolLength > symbol.length())
                    this.minReturnSymbolLength = symbol.length();
                if (this.maxReturnSymbolLength < symbol.length())
                    this.maxReturnSymbolLength = symbol.length();

                this.returnAlphabet.add(symbol);
                break;
        }

    }

    /**
     * Check if symbol already exists in Alphabet
     * <p>
     * O(n)
     *
     * @param symbol Symbol to be checked
     * @return True if exists in alphabet else false
     */
    public boolean inAlphabet(String symbol) {
        return (this.inAlphabet(symbol, this.INTERNAL_POSITION) || this.inAlphabet(symbol, this.CALL_POSITION) || this.inAlphabet(symbol, this.RETURN_POSITION));
    }

    /**
     * Check if symbol already exists in Alphabet of specific type
     * <p>
     * O(n)
     *
     * @param symbol     Symbol to be checked
     * @param symbolType Type of symbol
     * @return True if Symbol is contained in alphabet
     */
    public boolean inAlphabet(String symbol, int symbolType) {
        switch (symbolType) {
            case 0: // this.INTERNAL_POSITION:
                // check if word exists
                for (String internalSymbol : this.internalAlphabet
                        ) {
                    if (internalSymbol.equals(symbol))
                        return true;
                }
                break;
            case 1: // this.CALL_POSITION:
                // check if word exists
                for (String callSymbol : this.callAlphabet
                        ) {
                    if (callSymbol.equals(symbol)) {
                        return true;
                    }
                }
                break;
            case 2: // this.RETURN_POSITION:
                // check if word exists
                for (String returnSymbol : this.returnAlphabet
                        ) {
                    if (returnSymbol.equals(symbol)) {
                        return true;
                    }
                }
                break;
        }

        return false;

    }

    /**
     * Remove symbol from alphabet
     * <p>
     * O(n^2)
     *
     * @param symbol     Symbol to be removed
     * @param symbolType Type of Symbol
     * @throws NestedWordSymbolNotFoundException Symbol not found if not in alphabet
     */
    public void removeSymbol(String symbol, int symbolType) throws NestedWordSymbolNotFoundException {
        if (!this.inAlphabet(symbol, symbolType))
            throw new NestedWordSymbolNotFoundException();

        switch (symbolType) {
            case 0: //  this.INTERNAL_POSITION
                // check if word exists
                for (String internalSymbol : this.internalAlphabet
                        ) {
                    if (internalSymbol.equals(symbol)) {
                        this.internalAlphabet.remove(internalSymbol);
                        if (internalSymbol.length() == this.minInternalSymbolLength)
                            this.correctMaxMinSymbolLength(this.INTERNAL_POSITION, true);
                        if (internalSymbol.length() == this.maxInternalSymbolLength)
                            this.correctMaxMinSymbolLength(this.INTERNAL_POSITION, false);

                        break;
                    }

                }
                break;
            case 1: // this.CALL_POSITION:
                // check if word exists
                for (String callSymbol : this.callAlphabet
                        ) {
                    if (callSymbol.equals(symbol)) {
                        this.callAlphabet.remove(callSymbol);
                        if (callSymbol.length() == this.minCallSymbolLength)
                            this.correctMaxMinSymbolLength(this.CALL_POSITION, true);

                        if (callSymbol.length() == this.maxCallSymbolLength)
                            this.correctMaxMinSymbolLength(this.CALL_POSITION, false);

                        break;
                    }
                }
                break;
            case 2: // this.RETURN_POSITION:
                // check if word exists
                for (String returnSymbol : this.returnAlphabet
                        ) {
                    if (returnSymbol.equals(symbol)) {
                        this.returnAlphabet.remove(returnSymbol);
                        if (returnSymbol.length() == this.minReturnSymbolLength)
                            this.correctMaxMinSymbolLength(this.RETURN_POSITION, true);

                        if (returnSymbol.length() == this.maxReturnSymbolLength)
                            this.correctMaxMinSymbolLength(this.RETURN_POSITION, false);

                        break;
                    }
                }
                break;
        }

    }

    /**
     * Correct max and min symbol length
     * <p>
     * O(n)
     *
     * @param symbolType     type of symbol
     * @param checkMinLength max or min to correct
     */
    private void correctMaxMinSymbolLength(int symbolType, boolean checkMinLength) {
        int tmpSymbolMinLength = Integer.MAX_VALUE;
        int tmpSymbolMaxLength = 0;

        switch (symbolType) {
            case 0: //  this.INTERNAL_POSITION
                // check if word exists
                for (String internalSymbol : this.internalAlphabet
                        ) {
                    if (checkMinLength) {
                        if (tmpSymbolMinLength > internalSymbol.length()) {
                            tmpSymbolMinLength = internalSymbol.length();
                        }
                    } else {

                        if (tmpSymbolMaxLength < internalSymbol.length()) {
                            tmpSymbolMaxLength = internalSymbol.length();
                        }
                    }
                }
                if (checkMinLength) {
                    if (tmpSymbolMinLength > this.minInternalSymbolLength)
                        this.minInternalSymbolLength = tmpSymbolMinLength;
                } else {
                    if (tmpSymbolMaxLength < this.maxInternalSymbolLength)
                        this.maxInternalSymbolLength = tmpSymbolMaxLength;
                }
                break;

            case 1: // this.CALL_POSITION:
                // check if word exists
                for (String callSymbol : this.callAlphabet
                        ) {
                    if (checkMinLength) {
                        if (tmpSymbolMinLength > callSymbol.length()) {
                            tmpSymbolMinLength = callSymbol.length();
                        }
                    } else {

                        if (tmpSymbolMaxLength < callSymbol.length()) {
                            tmpSymbolMaxLength = callSymbol.length();
                        }
                    }
                }
                if (checkMinLength) {
                    if (tmpSymbolMinLength > this.minCallSymbolLength)
                        this.minCallSymbolLength = tmpSymbolMinLength;
                } else {
                    if (tmpSymbolMaxLength < this.maxCallSymbolLength)
                        this.maxCallSymbolLength = tmpSymbolMaxLength;
                }
                break;

            case 2: // this.RETURN_POSITION:
                // check if word exists
                for (String returnSymbol : this.returnAlphabet
                        ) {
                    if (checkMinLength) {
                        if (tmpSymbolMinLength > returnSymbol.length()) {
                            tmpSymbolMinLength = returnSymbol.length();
                        }
                    } else {

                        if (tmpSymbolMaxLength < returnSymbol.length()) {
                            tmpSymbolMaxLength = returnSymbol.length();
                        }
                    }
                }
                if (checkMinLength) {
                    if (tmpSymbolMinLength > this.minReturnSymbolLength)
                        this.minReturnSymbolLength = tmpSymbolMinLength;
                } else {
                    if (tmpSymbolMaxLength < this.maxReturnSymbolLength)
                        this.maxReturnSymbolLength = tmpSymbolMaxLength;
                }
                break;
        }

    }

    /**
     * Clear entire alphabet
     * <p>
     * O(1)
     */
    public void clearAlphabet() {
        clearAlphabet(this.INTERNAL_POSITION);
        clearAlphabet(this.CALL_POSITION);
        clearAlphabet(this.RETURN_POSITION);
    }

    /**
     * Clear certain symbol Type from alphabet
     * <p>
     * O(1)
     *
     * @param symbolType Type of symbols in alphabet which should be cleared
     */
    public void clearAlphabet(int symbolType) {
        switch (symbolType) {
            case 0: // this.INTERNAL_POSITION:
                this.internalAlphabet.clear();
                this.minInternalSymbolLength = Integer.MAX_VALUE;
                this.maxInternalSymbolLength = 0;
                break;
            case 1: // this.CALL_POSITION
                this.callAlphabet.clear();
                this.minCallSymbolLength = Integer.MAX_VALUE;
                this.maxCallSymbolLength = 0;
                break;
            case 2: // this.RETURN_POSITION
                this.returnAlphabet.clear();
                this.minReturnSymbolLength = Integer.MAX_VALUE;
                this.maxReturnSymbolLength = 0;
                break;
        }
    }

    public int getMinInternalSymbolLength() {
        return minInternalSymbolLength;
    }

    public int getMinCallSymbolLength() {
        return minCallSymbolLength;
    }

    public int getMinReturnSymbolLength() {
        return minReturnSymbolLength;
    }

    public int getMaxInternalSymbolLength() {
        return maxInternalSymbolLength;
    }

    public int getMaxCallSymbolLength() {
        return maxCallSymbolLength;
    }

    public int getMaxReturnSymbolLength() {
        return maxReturnSymbolLength;
    }

    /**
     * Give back complete alphabet &lt;Sigma union Sigma union Sigma&gt;
     * n.b. this would be the "real" Sigma
     *
     * @return returns a list of all symbols contained in the alphabet
     */
    public ArrayList<String> getAlphabet() {
        ArrayList<String> completeAlphabet = new ArrayList();
        completeAlphabet.addAll(this.getAlphabet(this.INTERNAL_POSITION));
        completeAlphabet.addAll(this.getAlphabet(this.CALL_POSITION));
        completeAlphabet.addAll(this.getAlphabet(this.RETURN_POSITION));

        return completeAlphabet;
    }

    /**
     * Give back alphabet of certain type.
     *
     * @param symbolType type of symbols (see constants of this class)
     * @return get list of either call, internal or return symbols
     */
    public ArrayList<String> getAlphabet(int symbolType) {
        if (symbolType == this.INTERNAL_POSITION)
            return this.internalAlphabet;
        else if (symbolType == this.CALL_POSITION)
            return this.callAlphabet;
        if (symbolType == this.RETURN_POSITION)
            return this.returnAlphabet;
        else return new ArrayList<String>();
    }

    /**
     * Make deep copy of alphabet and give it back
     *
     * @return cloned Alphabet
     */
    public Alphabet clone() {
        Alphabet alphabet = new Alphabet();
        for (String internalSymbol : this.internalAlphabet
                ) {
            try {
                alphabet.addSymbol(internalSymbol, this.INTERNAL_POSITION);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }

        for (String callSymbol : this.callAlphabet
                ) {
            try {
                alphabet.addSymbol(callSymbol, this.CALL_POSITION);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }


        for (String returnSymbol : this.returnAlphabet
                ) {
            try {
                alphabet.addSymbol(returnSymbol, this.RETURN_POSITION);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }

        return alphabet;
    }


    /**
     * Add all Symbols of other alphabet to this alphabet
     * Remark: if symbols are in both sets an exception is catched
     *
     * @param alphabet alphabet which contents should be added to this one
     */
    public void addAll(Alphabet alphabet) {
        for (String internalSymbol : alphabet.getAlphabet(this.INTERNAL_POSITION)
                ) {
            try {
                this.addSymbol(internalSymbol, this.INTERNAL_POSITION);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }

        for (String callSymbol : alphabet.getAlphabet(this.CALL_POSITION)
                ) {
            try {
                this.addSymbol(callSymbol, this.CALL_POSITION);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }

        for (String returnSymbol : alphabet.getAlphabet(this.RETURN_POSITION)
                ) {
            try {
                this.addSymbol(returnSymbol, this.RETURN_POSITION);
            } catch (NestedWordSymbolAlreadyExistsException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * Gives back type of Symbol
     *
     * @param symbol Symbol which type should be checked
     * @return the type of symbol, if symbol is not in alpahabet -1 is given back
     */
    public int getTypeOfSymbol(String symbol) {
        for (String sym : this.getAlphabet(Alphabet.INTERNAL_POSITION)
                ) {
            if (sym.equals(symbol)) return Alphabet.INTERNAL_POSITION;
        }
        for (String sym : this.getAlphabet(Alphabet.CALL_POSITION)
                ) {
            if (sym.equals(symbol)) return Alphabet.CALL_POSITION;
        }
        for (String sym : this.getAlphabet(Alphabet.RETURN_POSITION)
                ) {
            if (sym.equals(symbol)) return Alphabet.RETURN_POSITION;
        }
        return -1;
    }

    public void printAll() {
        System.out.println("Alphabet");
        System.out.println("--------");
        for (String symbol : this.getAlphabet(CALL_POSITION)
                ) {
            System.out.println(symbol + " (c)");
        }

        for (String symbol : this.getAlphabet(INTERNAL_POSITION)
                ) {
            System.out.println(symbol + " (i)");
        }

        for (String symbol : this.getAlphabet(RETURN_POSITION)
                ) {
            System.out.println(symbol + " (r)");
        }
    }
}
