import java.util.*;

class State {
    String name;
    boolean isAcceptState;

    // Map to hold transitions from this state to other states
    Map<Integer, State> transitionMap = new HashMap<>();

    // State constructor
    public State(String name, boolean isAcceptState) {
        this.name = name;
        this.isAcceptState = isAcceptState;
    }

    // Method to add new transition to trnsitionMap 
    public void addTransition(int symbol, State destinationState) {
        transitionMap.put(symbol, destinationState);
    }

    // Method to find the next state from the current state based on the input symbol
    public State getNexState(int symbol) {
        return transitionMap.get(symbol);
    }
}

class FSA {
    State startState;

    // FSA constructor
    public FSA(State startState) {
        this.startState = startState;
    }
}

class Generator {

    public Generator() {}
    
    public String gen(FSA fsa) {
        StringBuilder sb = new StringBuilder();
        State currState = fsa.startState;

        while(true) {
            if(currState.isAcceptState) {
                // Reduced the chance of stopping to make longer words
                int headOrTail = (int)(Math.random() * 10);
                if(headOrTail < 2) {
                    break;
                }
            }

            // List of possible next symbols
            List<Integer> nextSymbols = new ArrayList<>(currState.transitionMap.keySet());
            if(nextSymbols.isEmpty()) {
                break; // No more transitions available
            }

            // Randomly choose a next symbol and append it to the result
            Random random = new Random();
            int randomNum = random.nextInt(nextSymbols.size());
            int chosenNextSymbol = nextSymbols.get(randomNum);
            sb.append(chosenNextSymbol); 

            // Move to the next state
            currState = currState.getNexState(chosenNextSymbol);
        }
        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        // =======FSA 1: 0(01)*======
        State q1 = new State("q1", false);
        State q2 = new State("q2", true);
        State q3 = new State("q3", false);
        State q4 = new State("q4", true);

        q1.addTransition(0, q2);
        q2.addTransition(0, q3);
        q3.addTransition(1, q4);
        q4.addTransition(0, q3);

        FSA fsa1 = new FSA(q1);
        // ===========================

        // =====FSA 2: 1(011)*0*======
        State p1 = new State("p1", false);
        State p2 = new State("p2", true);
        State p3 = new State("p3", true);
        State p4 = new State("p4", false);
        State p5 = new State("p5", true);

        p1.addTransition(1, p2);
        p2.addTransition(0, p3);
        p3.addTransition(0, p5);
        p3.addTransition(1, p4);
        p4.addTransition(1, p5);
        p5.addTransition(0, p5);

        FSA fsa2 = new FSA(p1);
        // ===========================

        // Generate words from FSA 1
        Generator generator = new Generator();
        System.out.println("Words from FSA 1 - 0(01)*: ");
        for(int i = 0; i < 10; i++) {
            System.out.println(generator.gen(fsa1));
        }

        // Generate words from FSA 2
        System.out.println("\nWords from FSA 2 - 1(011)*0*: ");
        for(int i = 0; i < 10; i++) {
            System.out.println(generator.gen(fsa2));
        }
    }
}

