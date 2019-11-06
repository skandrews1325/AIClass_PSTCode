
/*
 * c2017-2019 Courtney Brown edited by Sarah Andrews
 * 
 * Class: H
 * Description: Demonstration of MIDI file manipulations, etc. & 'MelodyPlayer' sequencer
 * 
 */

//last edited: Nov 5, 2019

import processing.core.*;

import java.util.*;

//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

//import javax.sound.midi.*;

//make sure this class name matches your file name, if not fix.
public class HelloWorldMidiMain extends PApplet {

	MelodyPlayer player; // play a midi sequence
	MidiFileToNotes midiNotes; // read a midi file

	// Project 1
	ProbabilityGenerator<Integer> pitch = new ProbabilityGenerator<Integer>();
	ProbabilityGenerator<Double> rhythm = new ProbabilityGenerator<Double>();

	// Project 2
	MarkovGenerator<Integer> mar_pitch = new MarkovGenerator<Integer>();
	MarkovGenerator<Double> mar_rhythm = new MarkovGenerator<Double>();

	// Project 3
	MarkovOrderNGenerator<Integer> marN_pitch = new MarkovOrderNGenerator<Integer>(3);//of order N (for testing)
	MarkovOrderNGenerator<Double> marN_rhythm = new MarkovOrderNGenerator<Double>(3);

	// Project 5
	double pMin_val1 = 0.1;
	double pMin_val2 = 0.15;
	
	Node<Integer> pitch_node = new Node<Integer>(); //for unit test w/ Mary Had a Little Lamb
	Tree<Integer> pst_pitch_01 = new Tree<Integer>(3, pMin_val1);//of order 3, Pmin = 0.1
	Tree<Integer> pst_pitch_15 = new Tree<Integer>(3, pMin_val2);//of order 3, Pmin = 0.1

	// variables for the strings of words in proj 5 (this might be over kill, but I think each one needs to train on its own information)
	Node<String> string_node1 = new Node<String>(); // for unit test w/ magic_words1
	Tree<String> pst_string_node1_01 = new Tree<String>(3, pMin_val1);// of order 3, Pmin = 0.1
	Tree<String> pst_string_node1_15 = new Tree<String>(3, pMin_val2);// of order 3, Pmin = 0.15

	Node<String> string_node2 = new Node<String>(); // for unit test w/ magic_words2
	Tree<String> pst_string_node2_01 = new Tree<String>(3, pMin_val1);// of order 3, Pmin = 0.1
	Tree<String> pst_string_node2_15 = new Tree<String>(3, pMin_val2);// of order 3, Pmin = 0.15

	Node<String> string_node3 = new Node<String>(); // for unit test w/ random_letters
	Tree<String> pst_string_node3_01 = new Tree<String>(3, pMin_val1);// of order 3, Pmin = 0.1
	Tree<String> pst_string_node3_15 = new Tree<String>(3, pMin_val2);// of order 3, Pmin = 0.15

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("HelloWorldMidiMain"); // change this to match above class & file name

	}

	// setting the window size
	public void settings() {
		size(1000, 450);

	}

	// doing all the setup stuff
	public void setup() {
		fill(120, 50, 240);

		// returns a url
		String filePath = getPath("mid/MaryHadALittleLamb.mid");
		// playMidiFile(filePath);

		midiNotes = new MidiFileToNotes(filePath);

		// // which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0);
		pitch.train(midiNotes.getPitchArray());
		rhythm.train(midiNotes.getRhythmArray());

		// proj 2
		mar_pitch.train(midiNotes.getPitchArray());
		mar_rhythm.train(midiNotes.getRhythmArray());

		// proj 3
		marN_pitch.train(midiNotes.getPitchArray());
		marN_rhythm.train(midiNotes.getRhythmArray());

		// proj 5

		// strings of words for the unit tests of proj5
		String[] magic_words1 = { "a", "b", "r", "a", "c", "a", "d", "a", "b", "r", "a" };
		String[] magic_words2 = { "a", "c", "a", "d", "a", "a", "c", "b", "d", "a" };
		String[] random_letters = { "a", "b", "c", "c", "c", "d", "a", "a", "d", "c", "d", "a", "a", "b", "c", "a", "d", "a", "d" };
		ArrayList<String> testList1 = new ArrayList<String>(Arrays.asList(magic_words1));
		ArrayList<String> testList2 = new ArrayList<String>(Arrays.asList(magic_words2));
		ArrayList<String> testList3 = new ArrayList<String>(Arrays.asList(random_letters));

		// PST strings for proj5
		pst_string_node1_01.train(testList1);
		pst_string_node1_15.train(testList1);

		pst_string_node2_01.train(testList2);
		pst_string_node2_15.train(testList2);

		pst_string_node3_01.train(testList3);
		pst_string_node3_15.train(testList3);

		pst_pitch_01.train(midiNotes.getPitchArray());
		pst_pitch_15.train(midiNotes.getPitchArray());

		
		player = new MelodyPlayer(this, 100.0f);

		player.setup();
		player.setMelody(midiNotes.getPitchArray());
		player.setRhythm(midiNotes.getRhythmArray());
	}

	public void draw() {
		fill(0, 0, 0);
		text("Unit test 1: press '1', 'a' or 'n' to print out the probabilities of each note detected in the melody to the console", 50, 70);
		text("'1' for Project 1, 'a' for Project 2, 'n' for Project 3", 60, 100);
		text("Unit tes 2: press '2' or 'b' to print out the generations of the pitch and rhythm of ONE melody to the console", 50, 160);
		text("'2' for Project 1, 'b' for Project 2", 60, 190);
		text("Unit test 3: press '3' or 'c' to print out the probabilities of 10,000 generated melodies of length 20", 50, 250);
		text("'3' for Project 1, 'c' for Project 2", 60, 280);

		text("Project 5 PST: (For Pmin = 0.1) press 'd' for 'abracadabra', 'e' for 'acadaacbda', 'f' for 'abcccdaadcdaabcadad', and 'g' for the PST of Mary Had a Little Lamb", 50, 340);
		text("(For Pmin = 0.15) press 'h' for 'abracadabra', 'i' for 'acadaacbda', 'j' for 'abcccdaadcdaabcadad', and 'k' for the PST of Mary Had a Little Lamb", 130, 370);
	}

	// Unit test methods for proj 5
	public void unit_test_magicWords1_proj5_01() {
		println("--------------------------------");
		println("abracadabra: PST L=3   Pmin=0.1");
		println("--------------------------------");
		pst_string_node1_01.print();
		println(" ");
	}
	
	public void unit_test_magicWords1_proj5_015() {
		println("--------------------------------");
		println("abracadabra: PST L=3   Pmin=0.15");
		println("--------------------------------");
		pst_string_node1_15.print();
		println(" ");
	}

	public void unit_test_magicWords2_proj5_01() {
		println("-------------------------------");
		println("acadaacbda: PST L=3  Pmin=0.1");
		println("-------------------------------");
		pst_string_node2_01.print();
		println(" ");
	}
	
	public void unit_test_magicWords2_proj5_015() {
		println("-------------------------------");
		println("acadaacbda: PST L=3  Pmin=0.15");
		println("-------------------------------");
		pst_string_node2_15.print();
		println(" ");
	}

	public void unit_test_randomLetters_proj5_01() {
		println("----------------------------------------");
		println("abcccdaadcdaabcadad: PST L=3  Pmin=0.1");
		println("----------------------------------------");
		pst_string_node3_01.print();
		println(" ");
	}
	
	public void unit_test_randomLetters_proj5_015() {
		println("----------------------------------------");
		println("abcccdaadcdaabcadad: PST L=3  Pmin=0.15");
		println("----------------------------------------");
		pst_string_node3_15.print();
		println(" ");
	}
	
	public void unit_test_MaryPST_proj5_01() {
		println("-------------------------------------------");
		println("Mary Had a Little Lamb: PST L=3  Pmin=0.1");
		println("-------------------------------------------"); 
		pst_pitch_01.print(); 
		println(" ");
	}

	public void unit_test_MaryPST_proj5_015() {
		println("-------------------------------------------");
		println("Mary Had a Little Lamb: PST L=3  Pmin=0.15");
		println("-------------------------------------------"); 
		pst_pitch_15.print(); 
		println(" ");
	}



	// Unit test 1 methods
	public void unit_test1_proj1() {
		println(" ");
		println("Pitches:");
		pitch.print_TokensProb(midiNotes.getPitchArray());

		println(" ");
		println("Rhythm:");
		rhythm.print_TokensProb(midiNotes.getRhythmArray());
		println(" ");
	}

	public void unit_test1_proj2() {
		println(" ");
		println("Pitches:");
		mar_pitch.print_TransTable();

		println(" ");
		println("Rhythm:");
		mar_rhythm.print_TransTable();
		println(" ");
	}

	public void unit_test1_proj3() {
		println(" ");
		println("Pitches of Order 3:");
		marN_pitch.print_TransTable();

		println(" ");
		println("Rhythms of Order 3:");
		marN_rhythm.print_TransTable();
		println(" ");
	}

	// Unit test 2 methods
	void unit_test2_proj1() {
		ArrayList<Integer> gen_pitches = pitch.generate(20);
		ArrayList<Double> gen_rhythms = rhythm.generate(20);

		println("Generated pitches are: ");
		println(gen_pitches);
		println("Generated rhythms are: ");
		println(gen_rhythms);

		println(" ");
	}

	void unit_test2_proj2() {
		ArrayList<Integer> mar_gen_pitches = mar_pitch.generate(20);
		ArrayList<Double> mar_gen_rhythms = mar_rhythm.generate(20);

		println("Generated Markov pitches are: ");
		println(mar_gen_pitches);
		println("Generated Markov rhythms are: ");
		println(mar_gen_rhythms);

		println(" ");
	}

	// Unit test 3 methods
	void unit_test3_proj1() {

		ProbabilityGenerator<Integer> newPitch = new ProbabilityGenerator<Integer>();
		ProbabilityGenerator<Double> newRhythm = new ProbabilityGenerator<Double>();

		ArrayList<Integer> gen_pitches = new ArrayList<Integer>();
		ArrayList<Double> gen_rhythms = new ArrayList<Double>();

		for (int i = 0; i <= 10000; i++) {

			gen_pitches = pitch.generate(20); // each generates 20 notes
			gen_rhythms = rhythm.generate(20);

			newPitch.train(gen_pitches); // each of the generates above are trained into their respective functions
			newRhythm.train(gen_rhythms);
		}
		println(" ");
		println("Probability of Generated Pitches after 10,000 iterations of 20 note melodies: ");
		newPitch.print_TokensProb(midiNotes.getPitchArray());
		println("Probability of Generated Rhythms after 10,000 iterations of 20 note melodies: ");
		newRhythm.print_TokensProb(midiNotes.getRhythmArray());
	}

	void unit_test3_proj2() {

		MarkovGenerator<Integer> mar_newPitch = new MarkovGenerator<Integer>();
		MarkovGenerator<Double> mar_newRhythm = new MarkovGenerator<Double>();

		ArrayList<Integer> mar_gen_pitches = new ArrayList<Integer>();
		ArrayList<Double> mar_gen_rhythms = new ArrayList<Double>();

		for (int i = 0; i <= 10000; i++) {

			mar_gen_pitches = mar_pitch.generate(20);
			mar_gen_rhythms = mar_rhythm.generate(20);

			mar_newPitch.train(mar_gen_pitches);
			mar_newRhythm.train(mar_gen_rhythms);
		}
		println(" ");
		println("Probability of Generated Markov Pitches after 10,000 iterations of 20 note melodies: ");
		mar_newPitch.print_TransTable();
		println("Probability of Generated Markov Rhythms after 10,000 iterations of 20 note melodies: ");
		mar_newRhythm.print_TransTable();
	}

	// this finds the absolute path of a file
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	// this function is not currently called. you may call this from setup() if you want to test
	// this just plays the midi file -- all of it via your software synth. You will
	// not use this function in upcoming projects but it could be a good debug tool.
	void playMidiFile(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}

	public void keyPressed() {
		if (key == '1') { // proj1 unit1

			unit_test1_proj1();

		}
		if (key == 'a') { // proj2 unit1

			unit_test1_proj2();

		}
		if (key == 'n') { // proj3 unit1

			unit_test1_proj3();

		}
		if (key == '2') { // proj1 unit2

			unit_test2_proj1();

		}
		if (key == 'b') { // proj2 unit2

			unit_test2_proj2();

		}
		if (key == '3') { // proj1 unit3

			unit_test3_proj1();

		}
		if (key == 'c') { // proj2 unit3

			unit_test3_proj2();

		}

		// each of the following is a separate test for proj 5
		
		if (key == 'd') {

			unit_test_magicWords1_proj5_01();

		}
		if (key == 'e') {

			unit_test_magicWords2_proj5_01();

		}
		if (key == 'f') {

			unit_test_randomLetters_proj5_01();

		}
		if (key == 'g') {

			unit_test_MaryPST_proj5_01();

		}
		if (key == 'h') {

			unit_test_magicWords1_proj5_015();

		}
		if (key == 'i') {

			unit_test_magicWords2_proj5_015();

		}
		if (key == 'j') {

			unit_test_randomLetters_proj5_015();

		}
		if (key == 'k') {

			unit_test_MaryPST_proj5_015();

		}
	}

}
