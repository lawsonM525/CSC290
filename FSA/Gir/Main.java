import java.util.*;

class FSA {
    public static class State{
        public String name;
        public boolean isAcceptState;

        //constructor
        public State(String name, boolean isAcceptState) {
            this.name = name;
            this.isAcceptState = isAcceptState;
        }

        //hashmap for state transitions
        Map<Integer, State> transitionMap = new HashMap<>();

        //add transition to transitionMap
        public void addTransition(int symbol, State nextState) {
            transitionMap.put(symbol, nextState);
        }

        //snatch the next state by symbol 
        public State getNextState(int symbol) {
            return transitionMap.get(symbol);
        }
    }

    State startState;

    //constructor
    public FSA(State startState) {
        this.startState = startState;
    }
}

class Generator {
    public String gen(FSA fsa) {
        StringBuilder sBuilder = new StringBuilder();
        State currentState = fsa.startState;

        while(true) {
            Random random = new Random();
            
            if(currentState.isAcceptState) { //coin flip
                int coinFlip = (int)(Math.random() * 10);
                if(coinFlip %2 == 0) { //even to terminate
                    break;
                }
            }

            //array of symbols
            List<Integer> nextSymbols = new ArrayList<>(currentState.transitionMap.keySet());
            if(nextSymbols.isEmpty()) {
                break;
            }

            //get next symbol and append it
            int randomNum = random.nextInt(nextSymbols.size());
            int nextNextSymbol = nextSymbols.get(randomNum);
            sBuilder.append(nextNextSymbol); 

            //move onto the next state
            currentState = currentState.getNextState(nextNextSymbol);
        }
        return sBuilder.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        // FSA 1: 0(01)*
        //define the states, symbols, and transitions
        State q1 = new State("q1", false);
        State q2 = new State("q2", true); 
        State q3 = new State("q3", false); 
        State q4 = new State("q4", true);  
        q1.addTransition(0, q2); //add 0, accept or keep going
        q2.addTransition(0, q3); //add 0
        q3.addTransition(1, q4); //add 1, accept or loop
        q4.addTransition(0, q3); //add 0
        FSA fsa1 = new FSA(q1);

        // FSA 2
        State h1 = new State("h1", false);
        State h2 = new State("h2", true);
        State h3 = new State("h3", false);
        State h4 = new State("h4", false);
        h1.addTransition(1, h2); //add 1
        h2.addTransition(0, h3); //add 0
        h3.addTransition(1, h4); //add
        h4.addTransition(1, h2); //add 1
        h2.addTransition(0, h2); //add 0, possibly accept
        FSA fsa2 = new FSA(h1);

        //generate string from FSA 1
        Generator generator = new Generator();
        System.out.println("Outcomes from 0(01)*: ");
        for(int i = 0; i < 5; i++) {
            System.out.println(generator.gen(fsa1));
        }

        System.out.println("\n");

        //generate string from FSA 2
        System.out.println("Outcomes from 1(011)*0*: ");
        for(int i = 0; i < 5; i++) {
            System.out.println(generator.gen(fsa2));
        }
    }
}
