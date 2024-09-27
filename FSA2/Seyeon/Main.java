import java.util.*;

class State {
    String name;
    boolean isAcceptState;

    // Map to hold transitions from this state to other states
    Map<String, List<State>> transitionMap = new HashMap<>();

    public State(String name, boolean isAcceptState) {
        this.name = name;
        this.isAcceptState = isAcceptState;
    }

    public void addTransition(String symbol, State destinationState) {
        if(!transitionMap.containsKey(symbol)) {
            transitionMap.put(symbol, new ArrayList<>());
        }
        transitionMap.get(symbol).add(destinationState);
    }

    public List<State> getNexState(String symbol) {
        return transitionMap.get(symbol);
    }
}

class FSA {
    State startState;

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
                int stopProbability = (int) (Math.random() * 10);
                if (stopProbability < 3 && sb.length() > 2) {
                    break;
                }
            }

            // List of possible next symbols
            List<String> nextSymbols = new ArrayList<>(currState.transitionMap.keySet());
            if(nextSymbols.isEmpty()) {
                break; // No possible next symbols
            }

            // Randomly choose a next symbol and append it to the result
            Random random = new Random();
            int randomNum = random.nextInt(nextSymbols.size());
            String chosenNextSymbol = nextSymbols.get(randomNum);
            sb.append(chosenNextSymbol); 

            List<State> nextStates = currState.getNexState(chosenNextSymbol);
            currState = nextStates.get(random.nextInt(nextStates.size()));
        }
        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        /**
         * !! The word should be roughly 3 - 15 characters long !!
         * So ignore the single word rule. (This was handled in the stop condition of the generator)
         * 
         * To create a word that is pronounceable (Completely Seyeon's opinion - could be wrong): 
         * 1. A word can start with a vowel, consonant, or consonant pair.
         *    1-a. The word is pronounceable even if it's just a single vowel.
         *    1-b. However, a single consonant/consonant pair is not pronounceable.
         * 2. A vowel can come after a vowel, consonant, or consonant pair.
         *    2-a. A word can end with a vowel.
         * 3. A consonant can come after a vowel.
         *    3-a. A word can end with a consonant.
         * 4. A consonant pair can come after a vowel.
         *    4-a. A word can end with a consonant pair.
         */
        State q0 = new State("0", false); // start state
        State q1 = new State("1", false); // 1-b
        State q2 = new State("2", true); // 1-a, 2-a
        State q3 = new State("3", false); // 1-b
        State q4 = new State("4", true); // 3-a
        State q5 = new State("5", true); // 4-a

        String[] vowels = {"a", "e", "i", "o", "u"};
        String[] consonants = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"};
        String[] consonantPairs = {"ch", "sh", "th", "ph", "gh", "ng", "zh", "kh", "wh"};

        for(String vowel : vowels) {
            q0.addTransition(vowel, q2); // A word can start with a vowel
            q1.addTransition(vowel, q2); // A vowel can come after a consonant (first character is a consonant)
            q2.addTransition(vowel, q2); // A vowel can come after a vowel
            q3.addTransition(vowel, q2); // A vowel can come after a consonant (first character is a consonant pair)
            q4.addTransition(vowel, q2); // A vowel can come after a consonant
            q5.addTransition(vowel, q2); // A vowel can come after a consonant pair
        }

        for(String consonant : consonants) {
            q0.addTransition(consonant, q1); // A word can start with a consonant
            q2.addTransition(consonant, q4); // A consonant can come after a vowel
        }

        for(String consonantPair : consonantPairs) {
            q0.addTransition(consonantPair, q3); // A word can start with a consonant pair
            q2.addTransition(consonantPair, q5); // A consonant pair can come after a vowel
        }

        FSA fsa = new FSA(q0);
        Generator generator = new Generator();
        for(int i = 0; i < 10; i++) {
            System.out.println(generator.gen(fsa));
        }
    }
}
