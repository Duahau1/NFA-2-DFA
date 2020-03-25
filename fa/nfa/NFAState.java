package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.State;

/**
 * March 23,2020
 * This class create the NFA state by extends the State class
 * @author vannguyen,lamnguyen
 *
 */
public class NFAState extends State {
	
	private boolean isfinal;
	private HashMap<Character, Set<NFAState>> transitions;
	
	/**
	 * Constructor
	 * @param String stateName : specify the state name
	 */
	public NFAState(String name) {
		this.name = name;
		isfinal = false;
		transitions = new HashMap<Character, Set<NFAState>>();
	}
	/**
	 * Constructor 2 for the final state
	 * @param String stateName : specify the state name
	 * @param boolean isFinal: specify the final state
	 */
	public NFAState(String name, boolean isFinal) {
		this.name = name;
		isfinal = isFinal;
		transitions = new HashMap<Character, Set<NFAState>>();
	}
	
	/**
	 * Check if the state is final or not
	 * @return true if the state is final
	 */
	public boolean isFinal() {
		return isfinal;
	}
	
	/**
	 * Add a new transition into the machine
	 * @param c 
	 * @param newState
	 */
	public void addTransition(char c, NFAState newState) {
		Set<NFAState> transition = transitions.get(c);
		if (transition == null) {
			Set<NFAState> newTransition = new LinkedHashSet<NFAState>();
			newTransition.add(newState);
			transitions.put(c, newTransition);
		} else {
			transition.add(newState);
		}
	}
	/**
	 * Get the state given the symbol character
	 * @param c
	 * @return
	 */
	public Set<NFAState> getTo(char c) {
		return transitions.get(c);
	}
}