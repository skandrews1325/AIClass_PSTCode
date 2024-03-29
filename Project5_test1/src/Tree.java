
/*
 * Sarah Andrews
 * 
 * (for proj5)
 * this generic class extends off of the Node class and holds the root node and interacts with the 
 * tree via the root node. However the tree doesn't add any nodes or performs actions of traversing
 * the tree beyond its calls to the root node. (In other words, this is more of a way of displaying
 * the PST; with the additional function to train off the input)
 * 
 * (Oct 2019)
 */

import java.util.ArrayList; //to import the ArrayList class

public class Tree<T> extends Node { // since the Tree is tied in with node

	Node<T> root = new Node<T>(); // the root of the tree
	int L; // the maximum token sequence length (order length)
	
	double pMin; //this is set in the constructor
	int totalInputTokens; //the total number of input tokens

	// constructor
	Tree(int number_input, double pMin_input) {
		L = number_input;
		pMin = pMin_input;
	}

	void train(ArrayList input) {// (she didn't set anything in here equal to each other)
		for (int i = 1; i <= L; i++) {// i refers to the current order num
			for (int j = 0; j < input.size() - (i-1); j++) {// j is index into the input (each element in the input)
				int start_elem = j;
				int last_elem = j+i;
				
				ArrayList<T> curSequence = new ArrayList<T>(input.subList(start_elem, last_elem));
				
				// create a new node w/ the current sequence, theNewNode
				Node<T> theNewNode = new Node<T>();
				
				theNewNode.setTokenSeq(curSequence);

				root.addNode(theNewNode);
			}
		}
		totalInputTokens += input.size();
		root.pMinElimination(totalInputTokens, pMin);

	}

	void print() {
		root.print();
	}

}
