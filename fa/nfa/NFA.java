package fa.nfa;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import fa.FAInterface;
import fa.State;
import fa.dfa.DFA;

/**
 * March 24,2020
 * This class create the NFA state by implementing the NFAInterface
 * @author vannguyen,lamnguyen
 *
 */
public class NFA implements FAInterface, NFAInterface {
	private Set<NFAState> states; // All the states
	private NFAState start; // The start state
	private Set<Character> alphabets; // The alphabets the machine uses
	private final char EMPTY_TRANS = 'e';
	private final String TRANS_STATE = "[]";
	
	/**
	 * Constructor for the new NFA
	 */
	public NFA() {
		states = new LinkedHashSet<NFAState>();
		alphabets = new LinkedHashSet<Character>();
	}
	/**
	 * Adds the initial state to the DFA instance
	 * @param name is the label of the start state
	 */
	@Override
	public void addStartState(String name) {
		NFAState startState = getState(name);
		if (startState == null) {
			startState = new NFAState(name);
		}
		start = startState;
		states.add(start);
	}

	/**
	 * Adds a non-final, not initial state to the DFA instance
	 * @param name is the label of the state 
	 */
	@Override
	public void addState(String name) {
		NFAState newState = new NFAState(name);
		states.add(newState);
	}
	/**
	 * Adds a final state to the DFA
	 * @param name is the label of the state
	 */
	@Override
	public void addFinalState(String name) {
		NFAState newState = new NFAState(name, true);
		states.add(newState);
	}
	/**
	 * 
	 * @return equivalent DFA
	 */
	@Override
	public DFA getDFA() {
		//Initiate all the variables
		Set<Set<NFAState>> checkedStates = new LinkedHashSet<Set<NFAState>>(); 
		Set<String> knownStates = new LinkedHashSet<String>();	
		Queue<Set<NFAState>> inputProcess = new LinkedList<Set<NFAState>>(); 
		DFA dfa = new DFA();
		boolean tranSet = false;

		//get the new start state
		Set<NFAState> startState = eClosure(start); 
		checkedStates.add(startState);
		if (containFinals(startState)) {
			dfa.addFinalState(startState.toString());
		} else {
			dfa.addState(startState.toString());
		}
		dfa.addStartState(startState.toString());
		knownStates.add(startState.toString());

		// searching and processing
		for (char c : alphabets) {
			Set<NFAState> transition = new LinkedHashSet<NFAState>();
			for (NFAState ns : startState) {
				Set<NFAState> alphaTransition = ns.getTo(c);
				if (ns.getTo(c) != null) {
					Set<NFAState> eTransition = eClosure(alphaTransition);
					if (eTransition != null) {
						transition.addAll(eTransition);}
				}
			}
			if (!knownStates.contains(transition.toString()) && !transition.isEmpty()) {
				inputProcess.add(transition); //causing warning
				if (containFinals(transition)) {
					dfa.addFinalState(transition.toString());
				} else {
					dfa.addState(transition.toString());
				}
				knownStates.add(transition.toString());
			}
			// case where the transition is not empty
			if (!transition.isEmpty())
				dfa.addTransition(startState.toString(), c, transition.toString());
			else {
				if (!tranSet) {
					dfa.addState(TRANS_STATE);
					tranSet = true;
				}
				dfa.addTransition(startState.toString(), c, TRANS_STATE);
			}
		}
		checkedStates.add(startState);
		while (!inputProcess.isEmpty()) {
			Set<NFAState> notcheckedStates = inputProcess.poll();
			if (!knownStates.contains(notcheckedStates.toString()) && !inputProcess.contains(notcheckedStates)) {
				if (containFinals(notcheckedStates)) {
					dfa.addFinalState(notcheckedStates.toString());
				} else {
					dfa.addState(notcheckedStates.toString());
				}
			}
			for (char c : alphabets) {
				Set<NFAState> transition = new LinkedHashSet<NFAState>();
				for (NFAState ns : notcheckedStates) {
					Set<NFAState> charTransition = ns.getTo(c);
					if (charTransition != null) {
						Set<NFAState> eTransition = eClosure(charTransition);
						if (eTransition != null)
							transition.addAll(eTransition);
					}
				}
				if (!knownStates.contains(transition.toString()) && !transition.isEmpty()) {
					if (!inputProcess.contains(transition))
						inputProcess.add(transition);
					if (containFinals(transition)) {
						dfa.addFinalState(transition.toString());
					} else {
						dfa.addState(transition.toString());
					}
					knownStates.add(transition.toString());
				}
				if (!transition.isEmpty())
					dfa.addTransition(notcheckedStates.toString(), c, transition.toString());
				else {
					if (!tranSet) {
						dfa.addState(TRANS_STATE);
						tranSet = true;
					}
					dfa.addTransition(notcheckedStates.toString(), c, TRANS_STATE);
				}
			}
			checkedStates.add(notcheckedStates);
		}
		if (tranSet) {
			for (char c : alphabets) {
				dfa.addTransition(TRANS_STATE, c, TRANS_STATE);
			}
		}
		return dfa;
	}
	/**
	 * Return delta entries
	 * @param from - the source state
	 * @param onSymb - the label of the transition
	 * @return a set of sink states
	 */
	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		return from.getTo(onSymb);
	}
	/**
	 * Adds the transition to the DFA's delta data structure
	 * @param fromState is the label of the state where the transition starts
	 * @param onSymb is the symbol from the DFA's alphabet.
	 * @param toState is the label of the state where the transition ends
	 */
	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		getState(fromState).addTransition(onSymb, getState(toState));

		if (!alphabets.contains(onSymb) && onSymb != EMPTY_TRANS) {
			alphabets.add(onSymb);
		}
	}
	/**
	 * Getter for Q
	 * @return a set of states that FA has
	 */
	@Override
	public Set<? extends State> getStates() {
		return states;
	}
	/**
	 * Getter for F
	 * @return a set of final states that FA has
	 */
	@Override
	public Set<? extends State> getFinalStates() {
		Set<NFAState> fStates = new LinkedHashSet<NFAState>();
		for (NFAState ns : states) {
			if (ns.isFinal())
				fStates.add(ns);
		}
		return fStates;
	}
	/**
	 * Getter for q0
	 * @return the start state of FA
	 */
	@Override
	public State getStartState() {
		return start;
	}
	/**
	 * Getter for the alphabet Sigma
	 * @return the alphabet of FA
	 */
	@Override
	public Set<Character> getABC() {
		return alphabets;
	}
	/**
	 * Traverses all epsilon transitions and determine
	 * what states can be reached from s through e
	 * @param s
	 * @return set of states that can be reached from s on epsilon trans.
	 */
	@Override
	public Set<NFAState> eClosure(NFAState s) {
		Set<NFAState> closure = new LinkedHashSet<NFAState>();
		Queue<NFAState> toProcess = new LinkedList<NFAState>();
		toProcess.add(s);
		while (!toProcess.isEmpty()) {
			NFAState n = toProcess.poll();
			if (!closure.contains(n)) {
				closure.add(n);
				Set<NFAState> gotTo = n.getTo(EMPTY_TRANS);
				if (gotTo != null)
					toProcess.addAll(n.getTo(EMPTY_TRANS));
			}
		}
		return closure;
	}
	/**
	 * Using the for each loop to get the state from the machine
	 * @param s
	 * @return desired NFAstate
	 */
	private NFAState getState(String name) {
		NFAState returnState = null;
		for (NFAState s : states) {
			if (s.getName().equals(name)) {
				returnState = s;}
		}
		return returnState;
	}
	/**
	 * Check if the set has a final state 
	 * @param s
	 * @return true if it has final state within the set
	 */
	private boolean containFinals(Set<NFAState> name) {
		boolean hasFinal = false;
		for (NFAState s : name) {
			if (s.isFinal())
				hasFinal = true;
		}
		return hasFinal;
	}
	/**
	 * Get all the empty transitions of the machine
	 * @param s 
	 * @return the set of e transitions
	 */
	private Set<NFAState> eClosure(Set<NFAState> s) {
		Set<NFAState> emptytrans = new LinkedHashSet<NFAState>();
		for (NFAState ns : s) {
			Set<NFAState> eTrans = eClosure(ns);
			if (eTrans != null)
				emptytrans.addAll(eClosure(ns));
		}
		return emptytrans;
	}
}