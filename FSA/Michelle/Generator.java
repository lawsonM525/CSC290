package FSA.Michelle;

import java.util.Random;
import java.util.List;

public class Generator {
    public static void main(String[] args) {
        // generate words from FSA1 (0(01)*)
        FSA fsa1 = createFSA1();
        System.out.println("Words generated from FSA1 (0(01)*):");
        for (int i = 0; i < 5; i++) {
            String word = gen(fsa1);
            System.out.println(word);
        }

        // generate words from FSA2 (1(011)*0*)
        FSA fsa2 = createFSA2();
        System.out.println("\nWords generated from FSA2 (1(011)*0*):");
        for (int i = 0; i < 10; i++) {
            String word = gen(fsa2);
            System.out.println(word);
        }
    }

    public static String gen(FSA fsa) {
        StringBuilder word = new StringBuilder();
        Random rand = new Random();
        FSA.State currentState = fsa.startState;

        while (true) {
            if (currentState.isAccept) {
                // random word ending
                int randAccept = (int) (Math.random() * 15);
                if (randAccept < 2) {
                    break;
                }
            }

            List<FSA.Transition> transitions = currentState.transitions;
            if (transitions.isEmpty()) {
                // if there's no transitions available; end the word
                break;
            } else {
                // randomly pick a transition
                FSA.Transition transition = transitions.get(rand.nextInt(transitions.size()));
                word.append(transition.symbol);
                currentState = transition.targetState;
            }
        }

        return word.toString();
    }

    public static FSA createFSA1() {
        // regex: 0(01)*

        FSA.State q0 = new FSA.State("q0", false); // Start state
        FSA.State q1 = new FSA.State("q1", true); // Accept state after '0'
        FSA.State q2 = new FSA.State("q2", false); // extra state

        q0.addTransition('0', q1); // q0 --0--> q1

        q1.addTransition('0', q2); // q1 --0--> q2
        q2.addTransition('1', q1); // q2 --1--> q1

        FSA fsa = new FSA(q0);

        return fsa;
    }

    public static FSA createFSA2() {
        // regex: 1(011)*0*

        FSA.State q0 = new FSA.State("q0", false); // Start state
        FSA.State q1 = new FSA.State("q1", true); // Accept state
        FSA.State q2 = new FSA.State("q2", false); // loop state
        FSA.State q3 = new FSA.State("q3", false); // loop state
        FSA.State q4 = new FSA.State("q4", true); // loop state

        q0.addTransition('1', q1); // q0 --1--> q1

        q1.addTransition('0', q2); // q1 --0--> q2 (start of '011' loop)
        q1.addTransition('0', q4); // q1 --1--> q4 (skipping '011' loop)

        q2.addTransition('1', q3); // q2 --1--> q3

        // q3.addTransition('0', q4); // q3 --0--> q4
        q3.addTransition('1', q1); // q3 --1--> q1 (repeat the '011' loop)

        q4.addTransition('0', q4); // q1 --0--> q1 ('0*' at the end)

        FSA fsa = new FSA(q0);

        return fsa;
    }
}