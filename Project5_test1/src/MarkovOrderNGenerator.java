/*
 * Sarah Andrews
 * 
 * (for proj 3)
 * the following is a generic class extending off of the ProbabilityGenerator class and is used to collect data
 * and parse the data into sequences of order N. The data is then formated into a transition table that counts
 * the number of times a unique token comes after each sequence. This version of the Markov Generator from
 * project 2 further fine tunes the randomness of the generators so that they mimic the original input more closely.
 * 
 * (Oct 2019)
 * 
 */

import java.util.ArrayList; //to import the ArrayList class

public class MarkovOrderNGenerator<T> extends ProbabilityGenerator {

	// global class variables
	ArrayList<ArrayList<T>> uniqueAlphabetSequences = new ArrayList<ArrayList<T>>(); // array of the unique sequences of size N found in input

	ArrayList<T> input = new ArrayList<T>(); // arraylist of the input tokens
	ArrayList<T> alphabet = new ArrayList<T>(); // this is for the unique symbols/tokens in the input
	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList<ArrayList<Integer>>();

	int number_input;//the number inputed from the code in HelloWorldMidiMain that sets the orderN int

	// constructor
	MarkovOrderNGenerator(int orderN) {
		number_input = orderN;
	}

	void train(ArrayList input) {

		for (int i = number_input - 1; i < input.size() - 1; i++) {
			// 1. create the current seq of size of orderN from the input
			ArrayList<T> curSequence = new ArrayList<T>(input.subList(i - (number_input - 1), i + 1));
			int rowIndex = uniqueAlphabetSequences.indexOf(curSequence);

			// 2. Find curSequence in uniqueAlphabetSequences
			if (rowIndex == -1) {
				rowIndex = uniqueAlphabetSequences.size();
				uniqueAlphabetSequences.add(curSequence);

				ArrayList<Integer> row = new ArrayList<Integer>(alphabet.size());
				for (int k = 0; k < alphabet.size(); k++) {
					row.add(0);
				}
				transitionTable.add(row); // add to transitionTable
			}
			// 3. find the cur next token (tokenIndex)
			int tokenIndex = alphabet.indexOf(input.get(i + 1));

			if (tokenIndex == -1) {
				tokenIndex = alphabet.size();

				// adding a new "column" (expanding horizontally)
				for (int j = 0; j < transitionTable.size(); j++) {
					ArrayList<Integer> myRow = transitionTable.get(j); // second row array
					myRow.add(0);
				}
				// add the token to the alphabet array
				alphabet.add((T) input.get(i + 1));
			}

			// updating counts
			if (rowIndex > -1) {
				ArrayList<Integer> cor_row = transitionTable.get(rowIndex);
				int val = cor_row.get(tokenIndex) + 1;
				cor_row.set(tokenIndex, val);
			}
			rowIndex = tokenIndex;
		}
	}

	public void print_TransTable() {

		System.out.println(" ");
		System.out.println("-----Transition Table-----");
		
		System.out.println("                " + alphabet); // the empty space is to line up the alphabet over the rows

		for (int i = 0; i < uniqueAlphabetSequences.size(); i++) {
			ArrayList row = indexProb_int(transitionTable.get(i), i);
			System.out.println(uniqueAlphabetSequences.get(i) + " | " + row);
		}

		System.out.println(" ");
		System.out.println("------------");
		System.out.println(" ");
	}
}
