package FSA.Michelle;

import java.util.ArrayList;
import java.util.List;

public class FSA {
    public static class State {
        public String name;
        public boolean isAccept;
        public List<Transition> transitions;

        public State(String name, boolean isAccept) {
            this.name = name;
            this.isAccept = isAccept;
            this.transitions = new ArrayList<>();
        }

        public void addTransition(char symbol, State targetState) {
            transitions.add(new Transition(symbol, targetState));
        }
    }

    public static class Transition {
        public char symbol;
        public State targetState;

        public Transition(char symbol, State targetState) {
            this.symbol = symbol;
            this.targetState = targetState;
        }
    }

    public State startState;

    public FSA(State startState) {
        this.startState = startState;
    }
}
