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
	
	int count = 1; //the number of times the node appears in the input, init set to 1
	
	
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
		
//		System.out.println( "node.getTokenSeq().size() " + tokenSequence.size());
//		System.out.println( "tokenSequence.size(): " + node.getTokenSeq().size());

		ArrayList<T> token = node.getTokenSeq();
		ArrayList<T> testSeq = new ArrayList<T>(token.subList(start_char, last_char));
//		System.out.println("testSeq: " + testSeq);
//		System.out.println("our node seq: " + tokenSequence)
		
		return tokenSequence.equals(testSeq);
		
	}
	
	boolean addNode(Node node) { //(deal w/ children here)
		boolean found = false; //whether the node has been added yet or not
		
		if (tokenSequence.equals(node.getTokenSeq())) {
			found = true;
			count += 1; //adding 1 to the count
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
		System.out.println(tokenSequence);
		for (int i = 0; i < children.size(); i++) {
			children.get(i).print(1);
		}
	}
	
	void print(int numSpacesBefore) {
		
		for (int i = 1; i < numSpacesBefore; i++) {
			System.out.print("   ");
		}
		
		System.out.print("-->");
		System.out.println(tokenSequence);
		
		//for each node in the children
		for (int j = 0; j < children.size(); j++) {
			//each time this is called from the next child, the number of spaces will increase by 1
			children.get(j).print(numSpacesBefore + 1);
		}
	}
	
	boolean pMinElimination(int totalTokens, double pMin) {
		
		boolean shouldRemove = false;
		
		//1. find the number of times that the sequence could have occurred (dependent on tokenSequence.size())
//		totalTokens = tokenSequence.size();
		float emp = (count / (float)(totalTokens - (tokenSequence.size() - 1))); //Empirical Probability (based on the equation in the lect sheet)
		
		//for debugging, trying to figure out what's going wrong
		System.out.println(" ");
		System.out.println("totalTokens size: " + totalTokens);
		System.out.println("count size: " + count);
		System.out.println("emp value: " + emp);
		System.out.println("current token sequence (in pMinElim): " + tokenSequence);
		System.out.println(" ");
		//2. shouldRemove = empirical prob of the token sequence < pMin (need to make sure the " " is NOT eliminated!!)
		shouldRemove = emp < pMin && (tokenSequence.size()!=0);
		System.out.println("shouldRemove: "+ shouldRemove);
		
		if(!shouldRemove) {
		
			for(int i = (children.size()-1); i >= 0; i--) {// since the code is going backwards in the nodes instead of forward
				children.get(i).pMinElimination(totalTokens, pMin);//call pMinElimination on all the children nodes

				boolean remove = children.get(i).pMinElimination(totalTokens, pMin);
				
				System.out.println("remove: "+ remove);

				//if they return true, we should remove them
				if (remove) { //(note to self, might want to rename this, so the ! makes more sense given the name)
					children.remove(children.get(i)); 
					//System.out.println("current children left (after remove): " + children);
				}

			}
		}
		
		System.out.println("modified array: " + tokenSequence);
		

		//4. return (putting this here so eclipse can calm down)
		return shouldRemove;
	}
	
}
