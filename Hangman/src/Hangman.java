import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {
	private ArrayList<String> words = new ArrayList<String>();
	private String key;
	private String guess;
	private String badGuess = "";
	private String[][] art = new String[7][8];
	private int state = 0;
	private int wordCount = 0;
	private boolean reset = false;


	/**
	 * Sets up the ascii art array There are 7 states, 8 rows, 12 columns
	 */
	private void setStates() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("AsciiArt.txt"));
		Scanner sc = new Scanner(br);
		String s;

		for (int i = 0; i <= 6; i++)
			for (int j = 0; j <= 7; j++) {
				s = sc.nextLine();
				art[i][j] = s;
			}
		sc.close();
	}

	/**
	 * Ascii array to string for output
	 */
	public String toString() {
		String s = "";

		for (int i = 0; i <= 7; i++) {
			s += art[state][i];
			s += "\n";
		}
		return s;
	}

	/**
	 * Takes all the words in the Fixed_List file Makes an array which will be
	 * used to grab a random word
	 */
	private void init() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("Fixed_List.txt"));
		Scanner sc = new Scanner(br);
//		InputStream in = this.getClass().getResourceAsStream("/Hangman/Fixed_List.txt");
//		File temp = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
//		temp.deleteOnExit();
//		
//		Scanner sc = new Scanner(temp);

		while (sc.hasNext()) {
			incWC();
			words.add(sc.next());
		}
		sc.close();
	}

	/**
	 * Generates a new random word for the game Sets the guess word to asterixes
	 * (asterii?) equal in length to key
	 */
	private void newKey() {
		int x = ThreadLocalRandom.current().nextInt(0, getWC() + 1);
		setKey(words.get(x));
		String s = "";
		int i = 0;
		while (i < getKey().length()) {
			s += "*";
			i++;
		}
		
		setGuess(s);
	}

	/**
	 * Takes user inputed char and compares it to the key.
	 * If the char has already been used tell user to do it right.
	 * If there is a match, guess is updated.
	 * If not, state is updated and that char is added to badGuess
	 * If state is 6, hangman is hanged and game is over
	 * 
	 * @param c - user's guess
	 */
	private void guess(char c) {
		String keyVal = getKey();
		char[] oldGuess = getGuess().toCharArray();
		boolean goodVal = false;
		String newGuess = "";
		
		if (getBad().contains(Character.toString(c))){
			System.out.println("You've already guessed " + c);
			return;
		}
		
		for(int i = 0; i < keyVal.length(); i++){
			if (c == keyVal.charAt(i)){
				oldGuess[i] = c;
				goodVal = true;
			}
		}
		
		if(!goodVal){
			state++;
			setBad(c);
		}
		
		for(int i = 0; i < oldGuess.length ; i++){
			newGuess += oldGuess[i];
		}
		
		if(getState() == 6 || !getGuess().contains("*")){
			isDone();
		}
		
		if(!reset){
		setGuess(newGuess);
		}
		
	}

	/**
	 * Resets game or exits system depending on user input
	 */
	public void isDone(){
		Scanner sc = new Scanner(System.in);
		char choice;
		
		System.out.println("The word was " + getKey() + "!");
		
		if(!getGuess().contains("*")){
			System.out.println("Hey, you got it! GG! Play again? (y/n)");
			choice = sc.next().charAt(0);
		}else{
			System.out.println(this.toString());
			System.out.println("Gameover! Play again? (y/n)");
			choice = sc.next().charAt(0);
		}
		
		if (choice == 'y' || choice == 'Y'){
			reset = true;
			resetGame();
		}
		else if (choice == 'n' || choice == 'N'){
			System.exit(0);
		}else{
			System.out.println("Please enter 'y' or 'n'");
			isDone();
		}
			
//	sc.close();		
	}
	
	public void resetGame(){
		resetBad();
		resetState();
		newKey();
	}

	// GETTERS AND SETTERS
	public void setKey(String s) {
		key = s;
	}

	public String getKey() {
		return key;
	}

	public void setGuess(String s) {
		guess = s;
	}

	public String getGuess() {
		return guess;
	}

	public void incWC() {
		wordCount++;
	}

	public int getWC() {
		return wordCount;
	}

	public int getState() {
		return state;
	}

	public void incState() {
		state++;
	}

	public void resetState() {
		state = 0;
	}

	public void setBad(char c){
		badGuess += c;
	}
	
	public String getBad(){
		return badGuess;
	}
	
	public void resetBad() {
		badGuess = "";
	}

	public static void main(String[] args) throws IOException {
		Hangman h = new Hangman();
		Scanner sc = new Scanner(System.in);
		h.setStates();
		h.init();
		h.newKey();
		while(true){
			System.out.println(h.toString());
			System.out.println("Bad guesses: " + h.getBad());
			System.out.println("\n" + h.getGuess());
			System.out.print("\nEnter your next guess: ");
			h.guess(sc.next().charAt(0));
			if(!h.getGuess().contains("*"))
				h.isDone();
			if(h.reset){
				sc = new Scanner(System.in);
				h.reset = false;
			}
		}
		
}}
