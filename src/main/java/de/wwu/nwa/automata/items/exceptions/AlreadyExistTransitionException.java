package de.wwu.nwa.automata.items.exceptions;

/**
 * Exception should be thrown if a Transition already exists.
 * In deterministic Nested-word automata there must be unique transitions.
 *
 * @author Allan Grunert
 */
public class AlreadyExistTransitionException extends Throwable {
}
