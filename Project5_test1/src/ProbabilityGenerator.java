//Sarah Andrews
//this generic class handles the code for both the pitch and rhythm (for proj 1)
//this generic class trains the AI and generates a new output based on its input (Sept 2019)

import java.lang.reflect.Array;//to import the Array class
import java.util.ArrayList; //to import the ArrayList class

class ProbabilityGenerator<T> { //type token

	//global variables
	ArrayList<T> alphabet = new ArrayList<T>();
	ArrayList<Integer> alphabet_counts = new ArrayList<Integer>();
	float sum;

	ArrayList<Float> index_prob = new ArrayList<Float>();//probability of each index

	int indexOfTokenWithNoChances = -1;

	//constructor
	ProbabilityGenerator(){

	}

	//train counts
	/*keep in mind that this void train contains not only the code for setting up the alphabet_count, 
	 * but also contains code to sum the values and normalize it (which is used for the probability
	 * of each token)
	 */
	void train(ArrayList<T> input) {

		for(int i = 0; i < input.size(); i++) {
			int index = alphabet.indexOf(input.get(i));

			if(index == -1) {
				alphabet.add(input.get(i));
				alphabet_counts.add(1);
			} else {
				int val = alphabet_counts.get(index);
				val++;
				alphabet_counts.set(index, val); //also updates the count of how many times something appears
			}
		}
	}

	//for the MarkovGenerator class
	ArrayList<Float> indexProb(ArrayList<Integer> counts) {
		ArrayList<Float> local_index_prob = new ArrayList<Float>();
		sum = 0;
		for(int j = 0; j < counts.size(); j++) {
			sum = sum + counts.get(j);
		}
		local_index_prob.clear();

		//normalizing the data (the probability of each token)
		for (int k = 0; k <counts.size(); k++) {
			local_index_prob.add((counts.get(k)/sum));
		} 
		return local_index_prob;
	}


	//Unit test 1
	public void print_TokensProb(ArrayList<T> input) { 

		System.out.println(" ");
		System.out.println("-----Probability Distribution-----");
		System.out.println(" ");
		ArrayList loc_indexProb = indexProb(alphabet_counts);


		for (int l = 0; l < alphabet.size(); l++) {
			System.out.println("Token: " + alphabet.get(l) + " | Probability: " + loc_indexProb.get(l));
		}

		System.out.println(" ");
		System.out.println("------------");
		System.out.println(" ");
	}


	ArrayList<Float> indexProb_int(ArrayList<Integer> counts, int index) {
		ArrayList<Float> local_index_prob = new ArrayList<Float>();
		float total_counts = 0;
		for (int j = 0; j < counts.size(); j++) {
			total_counts = (float) counts.get(j) + total_counts;
		}
		if (total_counts == 0) {
			indexOfTokenWithNoChances = index;
			for (int k = 0; k < counts.size(); k++) {
				local_index_prob.add(0.0f);
			} //make all the probabilities 0
		} 
		else {
			local_index_prob.clear();
			//the probability of each token
			for (int k = 0; k < counts.size(); k++) {
				local_index_prob.add(((float)counts.get(k) / total_counts));
			}
		}
		return local_index_prob;
	}

	//generate
	//this ArrayList here is for calling multiple iterations of generate
	ArrayList<T> generate(int n){ //n is the # of tokens to generate
		ArrayList<T> output = new ArrayList<T>();

		for (int i = 0; i < n; i++) {
			output.add(generate()); //*and this is the last area the error leads to (3rd)
		}
		return output;
	}

	T generate(ArrayList counts) {

		assert(!alphabet.isEmpty()); //this line is more of a safety precaution; nothing happens if the ArrayList is empty

		float randIndex = (float) Math.random();//generates a random index
		boolean found = false;
		int index = 0;
		float total = 0;

		ArrayList<Float> loc_indexProb = indexProb_int(counts, index);

		while(!found && index < counts.size()-1) {
			total = total + loc_indexProb.get(index);
			found = randIndex <= total;
			index++;
		}
		if (found) {
			
			return alphabet.get(index-1);
		} else {
			
			return alphabet.get(index); //*problem seems to arise here 1st; 
		}
	}

	T generate() {
		return generate(alphabet_counts);//*then problem goes here (2nd)
	}

}
