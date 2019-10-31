
/*
 * Sarah Andrews
 * 
 * (for proj 2)
 * the following is a generic class extending from the ProbabilityGenerator class and is used to collect data in
 * the form of a transition table that counts the number of times a unique token comes after another unique token.
 * In other words, this generator fine tunes the randomness of the generations so that the result matches the style
 * of the original input.
 * 
 * (Sept 2019)
 */

import java.util.ArrayList; //to import the ArrayList class

public class MarkovGenerator<T> extends ProbabilityGenerator {

	// global CLASS variables
	ArrayList<T> input = new ArrayList<T>(); // arraylist of the input tokens
	ArrayList<T> alphabet = new ArrayList<T>(); // this is for the unique symbols/tokens in the input

	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList<ArrayList<Integer>>();
	
	//variables to help solve the 4.0 rhythm token problem
	ProbabilityGenerator<T> probGenerator = new ProbabilityGenerator<T>();
	int indexOfTokenWithNoChances = -1; //this is the index of the row that's got 0.0 probability total

	// constructor
	MarkovGenerator() {

	}

	void train(ArrayList input) {
		probGenerator.train(input);
		int lastIndex = -1; // local var that is the index of the previous token

		for (int i = 0; i < input.size(); i++) {

			int tokenIndex = alphabet.indexOf(input.get(i));

			if (tokenIndex == -1) {
				tokenIndex = alphabet.size();

				// 2. adding a new row to the transition table
				ArrayList<Integer> row = new ArrayList<Integer>(alphabet.size());
				for (int k = 0; k < alphabet.size(); k++) {
					row.add(0);
				}
				transitionTable.add(row); // add to transitionTable

				// 3. adding a new "column" (expanding horizontally)
				for (int j = 0; j < transitionTable.size(); j++) {
					ArrayList<Integer> myRow = transitionTable.get(j); // second row array
					myRow.add(0);
				}

				// 4. add the token to the alphabet array
				alphabet.add((T) input.get(i));
			}
			// adding the counts to the transition table
			if (lastIndex > -1) { // already have a token, ergo not the 1st time thru
				// 1. use lastIndex to get the correct row (array) in your table
				ArrayList<Integer> cor_row = transitionTable.get(lastIndex);

				int val = cor_row.get(tokenIndex) + 1;
				cor_row.set(tokenIndex, val);

			}
			lastIndex = tokenIndex;
		}
	}
		
	// For unit test 1 to print off the transition table
	public void print_TransTable() {

		System.out.println(" ");
		System.out.println("-----Transition Table-----");
		System.out.println(" ");

		for (int i = 0; i < alphabet.size(); i++) {
			ArrayList row = indexProb_int(transitionTable.get(i), i);
			System.out.println("Token: " + alphabet.get(i) + " | " + row);
		}

		System.out.println(" ");
		System.out.println("------------");
		System.out.println(" ");
	}

	// generate
	T generate(T initToken) {
		//1. find token in the alphabet
		int row_index = alphabet.indexOf(initToken);

		ArrayList row = transitionTable.get(row_index);
		ArrayList prob = indexProb_int(row, row_index);
		
		//summing everything in the row
		if (row_index == indexOfTokenWithNoChances) {
			return (T) generate(prob);
			
		}

		return probGenerator.generate();
	}

	//this ArrayList here is for calling multiple iterations of generate
	ArrayList<T> generate(int n){ //n is the # of tokens to generate
		T initToken = probGenerator.generate();
		ArrayList<T> tokens = new ArrayList<T>();
		
		for (int i = 0; i < n; i++) {
			T token = generate(initToken);
			tokens.add(token);
			initToken = token;
		}
		return tokens;
	}

}
