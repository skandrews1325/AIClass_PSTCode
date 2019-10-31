/*
 * Sarah Andrews
 * 
 * (for proj5)
 * This generic class holds the information of one node in a prediction suffix tree and is responsible for adding
 * nodes to the sequence
 * 
 * (Oct 2019)
 */

import java.util.ArrayList; //to import the ArrayList class

public class Node<T> {
	
	ArrayList<T> tokenSequence = new ArrayList<T>(); //the sequence at this node
	ArrayList<Node> children = new ArrayList<Node>(); // children - an array of the child nodes
	
	
	//constructor
	Node() {
		
	}
	
	ArrayList<T> getTokenSeq() {
		return tokenSequence;
	}
	
	//setter for the sequences
	void setTokenSeq(ArrayList<T> tokenArray) {
		tokenSequence = tokenArray;
	}
	
	boolean amIaSuffix(Node node) { //determines whether the tokenSequence of this node is a suffix of the token Sequence of the input node
		
		//think it would use .equals, b/c it addresses the content of the objects
		int start_char = node.getTokenSeq().size() - tokenSequence.size(); //the starting element for subList
		int last_char = node.getTokenSeq().size(); //the end/last element for subList
		
		System.out.println( "node.getTokenSeq().size() " + tokenSequence.size());
		System.out.println( "tokenSequence.size(): " + node.getTokenSeq().size());

		ArrayList<T> token = node.getTokenSeq();
		ArrayList<T> testSeq = new ArrayList<T>(token.subList(start_char, last_char));
		System.out.println("testSeq: " + testSeq);
//		System.out.println("our node seq: " + tokenSequence)
		
		return tokenSequence.equals(testSeq);
		
	}
	
	boolean addNode(Node node) { //(deal w/ children here)
		boolean found = false; //whether the node has been added yet or not
		
		if (tokenSequence.equals(node.getTokenSeq())) {
			found = true;
			//don't do anything else here for now
		}
		else if(amIaSuffix(node) || (tokenSequence.size()==0)) { //(sidenote: the "==" here assigns the address!!)
			
			int i = 0;
			
			while(i < children.size() && !found) { //1. try to add the node to all the CHILDREN nodes
				found = children.get(i).addNode(node);
				i++;
			}
			
			if (!found) { //2. if NOT found, add the node to children array; thus found = true
				children.add(node);
				found = true;
			}

		}
		
		return found;
	}
	
	void print() { //print the token sequence
		//for each node in the children (call node.print(1))
		
		for (int i = 0; i < children.size(); i++) {
			children.get(i).print(1);
		}
	}
	
	void print(int numSpacesBefore) {
		
		for (int i = 1; i < numSpacesBefore; i++) {
			System.out.print("  ");
		}
		
		System.out.print("-->");
		System.out.println(tokenSequence);
		
		//for each node in the children
		for (int j = 0; j < children.size(); j++) {
			//each time this is called from the next child, the number of spaces will increase by 1
			children.get(j).print(numSpacesBefore + 1);
		}
		
	}
	
}
