package de.wwu.nwa.items;

import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolNotFoundException;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


public class AlphabetTest {

    @Test
    public void inAlphabetTest() {
        Alphabet alphabet = new Alphabet();
        String symbol1 = "uhu";
        String symbol2 = "eule";
        String symbol3 = "bussard";
        String symbol4 = "ei";
        String symbol5 = "uhu";
        String symbol6 = "eule";


        try {
            alphabet.addSymbol(symbol1, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertFalse(alphabet.inAlphabet(symbol2));
        assertFalse(alphabet.inAlphabet(symbol3, Alphabet.INTERNAL_POSITION));
        assertFalse(alphabet.inAlphabet(symbol4, Alphabet.INTERNAL_POSITION));
        assertTrue(alphabet.inAlphabet(symbol5));
        assertTrue(alphabet.inAlphabet(symbol5, Alphabet.INTERNAL_POSITION));
        assertFalse(alphabet.inAlphabet(symbol6, Alphabet.INTERNAL_POSITION));

        try {
            alphabet.addSymbol(symbol2, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertTrue(alphabet.inAlphabet(symbol6));
        assertTrue(alphabet.inAlphabet(symbol2));

        try {
            alphabet.removeSymbol(symbol1, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertFalse(alphabet.inAlphabet(symbol5, Alphabet.INTERNAL_POSITION));
        assertFalse(alphabet.inAlphabet(symbol1, Alphabet.INTERNAL_POSITION));

        try {
            alphabet.addSymbol(symbol1, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        alphabet.clearAlphabet(Alphabet.INTERNAL_POSITION);

        // Test Call ---------------
        try {
            alphabet.addSymbol(symbol1, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertFalse(alphabet.inAlphabet(symbol2));
        assertFalse(alphabet.inAlphabet(symbol3, Alphabet.CALL_POSITION));
        assertFalse(alphabet.inAlphabet(symbol4, Alphabet.CALL_POSITION));
        assertTrue(alphabet.inAlphabet(symbol5));
        assertTrue(alphabet.inAlphabet(symbol5, Alphabet.CALL_POSITION));
        assertFalse(alphabet.inAlphabet(symbol6, Alphabet.CALL_POSITION));

        try {
            alphabet.addSymbol(symbol2, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertTrue(alphabet.inAlphabet(symbol6, Alphabet.CALL_POSITION));
        assertTrue(alphabet.inAlphabet(symbol2, Alphabet.CALL_POSITION));

        try {
            alphabet.removeSymbol(symbol1, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertFalse(alphabet.inAlphabet(symbol5, Alphabet.CALL_POSITION));
        assertFalse(alphabet.inAlphabet(symbol1, Alphabet.CALL_POSITION));

        alphabet.clearAlphabet(Alphabet.CALL_POSITION);

        // Test RETURN ---------------
        try {
            alphabet.addSymbol(symbol1, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertFalse(alphabet.inAlphabet(symbol2));
        assertFalse(alphabet.inAlphabet(symbol3, Alphabet.RETURN_POSITION));
        assertFalse(alphabet.inAlphabet(symbol4, Alphabet.RETURN_POSITION));
        assertTrue(alphabet.inAlphabet(symbol5));
        assertTrue(alphabet.inAlphabet(symbol5, Alphabet.RETURN_POSITION));
        assertFalse(alphabet.inAlphabet(symbol6, Alphabet.RETURN_POSITION));

        try {
            alphabet.addSymbol(symbol2, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertTrue(alphabet.inAlphabet(symbol6, Alphabet.RETURN_POSITION));
        assertTrue(alphabet.inAlphabet(symbol2, Alphabet.RETURN_POSITION));

        try {
            alphabet.removeSymbol(symbol1, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertFalse(alphabet.inAlphabet(symbol5, Alphabet.RETURN_POSITION));
        assertFalse(alphabet.inAlphabet(symbol1, Alphabet.RETURN_POSITION));
    }

    @Test
    public void removeSymbolTest() {
        // Alle 3 Typen
        // MinMax testen

        String s1 = "Uhu";
        String s2 = "Athene";
        String s3 = "Eule";

        Alphabet alphabet = new Alphabet();


        try {
            alphabet.addSymbol(s1, Alphabet.INTERNAL_POSITION);
            alphabet.addSymbol(s2, Alphabet.INTERNAL_POSITION);
            alphabet.addSymbol(s3, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertTrue(3 == alphabet.getMinInternalSymbolLength());


        try {
            alphabet.removeSymbol(s1, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(4 == alphabet.getMinInternalSymbolLength());

        assertTrue(s2.length() == alphabet.getMaxInternalSymbolLength());

        try {
            alphabet.removeSymbol(s2, Alphabet.INTERNAL_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(4 == alphabet.getMaxInternalSymbolLength());

        alphabet.clearAlphabet(Alphabet.INTERNAL_POSITION);

        // Calls

        try {
            alphabet.addSymbol(s1, Alphabet.CALL_POSITION);
            alphabet.addSymbol(s2, Alphabet.CALL_POSITION);
            alphabet.addSymbol(s3, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertTrue(3 == alphabet.getMinCallSymbolLength());


        try {
            alphabet.removeSymbol(s1, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(4 == alphabet.getMinCallSymbolLength());

        assertTrue(6 == alphabet.getMaxCallSymbolLength());

        try {
            alphabet.removeSymbol(s2, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(4 == alphabet.getMaxCallSymbolLength());

        alphabet.clearAlphabet(Alphabet.CALL_POSITION);

        // Returns

        try {
            alphabet.addSymbol(s1, Alphabet.RETURN_POSITION);
            alphabet.addSymbol(s2, Alphabet.RETURN_POSITION);
            alphabet.addSymbol(s3, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        assertTrue(3 == alphabet.getMinReturnSymbolLength());

        try {
            alphabet.removeSymbol(s1, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(4 == alphabet.getMinReturnSymbolLength());


        assertTrue(6 == alphabet.getMaxReturnSymbolLength());

        try {
            alphabet.removeSymbol(s2, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(4 == alphabet.getMaxReturnSymbolLength());

        alphabet.clearAlphabet();
    }


    @Test(expected = NestedWordSymbolAlreadyExistsException.class)
    public void testCallAlreadyExists() throws NestedWordSymbolAlreadyExistsException {
        Alphabet alphabet = new Alphabet();
        String symbol1 = "uhu";
        String symbol5 = "uhu";

        try {
            alphabet.addSymbol(symbol1, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            // e.printStackTrace();
        }

        alphabet.addSymbol(symbol5, Alphabet.CALL_POSITION);


    }


    @Test(expected = NestedWordSymbolAlreadyExistsException.class)
    public void testReturnAlreadyExists() throws NestedWordSymbolAlreadyExistsException {
        Alphabet alphabet = new Alphabet();
        String symbol1 = "uhu";
        String symbol5 = "uhu";

        try {
            alphabet.addSymbol(symbol1, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            // e.printStackTrace();
        }
        alphabet.addSymbol(symbol5, Alphabet.RETURN_POSITION);
    }


    @Test(expected = NestedWordSymbolAlreadyExistsException.class)
    public void testInternalAlreadyExists() throws NestedWordSymbolAlreadyExistsException {
        Alphabet alphabet = new Alphabet();
        String symbol1 = "uhu";
        String symbol5 = "uhu";

        try {
            alphabet.addSymbol(symbol1, Alphabet.CALL_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            // e.printStackTrace();
        }

        alphabet.addSymbol(symbol5, Alphabet.INTERNAL_POSITION);

    }

}