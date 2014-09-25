
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 *  A GUI version of the game of Hangman.  The user tries to guess letters in
 *  a secret word, and loses after 7 guesses that are not in the word.  The
 *  user guesses a letter by clicking a button whose text is that letter.
 */
public class Hangman extends JPanel {

	private Display display; // The central panel of the GUI, where things are drawn

	private ArrayList<JButton> alphabetButtons = new ArrayList<JButton>(); // 26 buttons, with lables "A", "B", ..., "Z"
	private JButton nextButton;    // A button the user can click after one game ends to go on to the next word.
	private JButton giveUpButton;  // A button that the user can click during a game to give up and end the game.

	private String message;     // A message that is drawn in the Display.
	private WordList wordlist;  // An object holding the list of possible words that can be used in the game.
	private String word;        // The current secret word.
	private String guesses;     // A string containing all the letters that the user has guessed so far.
	private boolean gameOver;   // False when a game is in progress, true when a game has ended and a new one not yet begun.
	private int badGuesses;     // The number of incorrect letters that the user has guessed in the current game.
	private String wordSoFar; //to be displayed as either a _ or a letter.

	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //the string of letters in the alphabet. Each character will have a button

	/**
	 * This class defines a listener that will respond to the events that occur
	 * when the user clicks any of the buttons in the button.  The buttons are
	 * labeled "Next word", "Give up", "Quit", "A", "B", "C", ..., "Z".
	 */
	private class ButtonHandler implements ActionListener {
		public void actionPerformed( ActionEvent evt ) {
			JButton whichButton = (JButton)evt.getSource();  // The button that the user clicked.
			String cmd = evt.getActionCommand();  // The test from the button that the user clicked.
			whichButton.setEnabled(false);


			if (cmd.equals("Quit")) { // Respond to Quit button by ending the program.
				System.exit(0);
			}
			else if (cmd.equals("Give up")){
				endgame();
			}

			else if (cmd.equals("Next word" )){
				startGame(); 
			}
			else if (word.indexOf(cmd)>= 0){
				guesses = guesses + cmd; 
				message = cmd + " is in the word";
				wordSoFar = " "; 
				for (int i = 0; i< word.length(); i++){
					char ch = word.charAt(i); 
					if( guesses.indexOf(ch)>=0){
						wordSoFar= wordSoFar + ch; 

					}
					else {
						wordSoFar = wordSoFar + " _ "; 
					}


				}



			}
			else {

				message = "sorry " + cmd + " does not occur in the word, you have " + (6 - badGuesses) + " wrong guesses remaining "; 

				guesses = guesses + cmd; 
				badGuesses ++; 
			}

			if (badGuesses == 7){
				message = "Game Over!"; 
				endgame(); 
			}

			if (wordIsComplete() == true){
				endgame();
			} 










			display.repaint();  // Causes the display to be redrawn, to show any changes made in this method.

		}




/**
 * this method can be called to the end the current game. It will set all the alphabet buttons to disabled 
 * and the "give up" button to disabled, and will enable the next work button to initiate 
 * the new game. 
 */


		private void endgame() {
			gameOver = true; 
			nextButton.setEnabled(true);


			for (int i = 0; i < alphabet.length(); i++){
				alphabetButtons.get(i).setEnabled(false); 
			}

			giveUpButton.setEnabled(false); 

			if (wordIsComplete() == true){
				message = "you got it!"; 
			}
			else {
				message = "Game Over! the word was " + word; 
				guesses = "";
			}



		}
	}



	/**
	 * This class defines the panel that occupies the large central area in the
	 * main panel.  The paintComponent() method in this class is responsible for
	 * drawing the content of that panel.  It shows everything that that the user
	 * is supposed to see, based on the current values of all the instance variables.
	 */
	private class Display extends JPanel {
		Display() {
			setPreferredSize(new Dimension(620,420));
			setBackground( new Color(250, 230, 180) );
			setFont( new Font("Serif", Font.BOLD, 20) );
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D)g).setStroke(new BasicStroke(3));
			if (message != null) {
				g.setColor(Color.RED);
				g.drawString(message, 30, 40);
				g.drawString(guesses,30,60);
				g.drawString(wordSoFar, 30, 300);
			}
			g.setColor(Color.RED);
			g.fillRect(350, 400, 200, 10);
			g.fillRect(400,200,10,200);
			g.fillRect(400,200,100,10);
			if (badGuesses >= 1){
				g.fillOval(470,210,30,30);
			}
			if (badGuesses >= 2){
				g.fillRect(481, 240, 10, 100);
			}
			if (badGuesses >=3){
				g.fillRect(490,250,50,10);
			}
			if (badGuesses >=4){
				g.fillRect(450, 250, 50, 10);
			}
			if (badGuesses >= 5){
				g.drawLine(490,340,500,380);
			}
			if (badGuesses >= 6){
				g.drawLine(480, 340, 480, 380);
			}
			if (badGuesses == 7){
				g.setColor(Color.BLACK); 
				g.drawLine(480, 220, 480, 225);
				g.drawLine(490,220,490,225);
				g.drawOval(480, 230, 5, 5);
			}
			
			
			
		}
	}

	/**
	 * The constructor that creates the main panel, which is represented
	 * by this class.  It makes all the buttons and subpanels and adds 
	 * them to the main panel.
	 */
	public Hangman() {

		ButtonHandler buttonHandler = new ButtonHandler(); // The ActionListener that will respond to button clicks.

		/* Create the subpanels and add them to the main panel.
		 */

		display = new Display();  // The display panel that fills the large central area of the main panel.
		JPanel bottom = new JPanel();  // The small panel on the bottom edge of the main panel.
		JPanel top = new JPanel(); 

		setLayout(new BorderLayout(3,3));  // Use a BorderLayout layout manager on the main panel.
		add(display, BorderLayout.CENTER); // Put display in the central position in the "CENTER" position.
		add(bottom, BorderLayout.SOUTH);   // Put bottom in the "SOUTH" position of the layout.
		add(top, BorderLayout.NORTH);

		top.setLayout( new GridLayout(2,13) );


		char ch = 'A'; 

		for (int i = 0; i <alphabet.length(); i++){
			ch = alphabet.charAt(i);
			JButton button = new JButton("" + ch);
			button.addActionListener(buttonHandler);
			top.add(button);
			alphabetButtons.add(button);
		}

		/* Create three buttons, register the ActionListener to respond to clicks on the
		 * buttons, and add them to the bottom panel.
		 */


		nextButton = new JButton("Next word");
		nextButton.addActionListener(buttonHandler);
		bottom.add(nextButton);

		giveUpButton = new JButton("Give up");
		giveUpButton.addActionListener(buttonHandler);
		bottom.add(giveUpButton);

		JButton quit = new JButton("Quit");
		quit.addActionListener(buttonHandler);
		bottom.add(quit);

		/* Make the main panel a little prettier
		 */

		setBackground( new Color(100,0,0) );
		setBorder(BorderFactory.createLineBorder(new Color(100,0,0), 3));

		/* Get the list of possible secret words from the resource file named "wordlist.txt".
		 */

		wordlist = new WordList("wordlist.txt");

		/* Start the first game.
		 */

		startGame();

	} // end constructor

	/**
	 * This method should be called any time a new game starts. It picks a new
	 * secret word, initializes all the variables that record the state of the
	 * game, and sets the enabled/disabled state of all the buttons.
	 */
	private void startGame() {
		gameOver = false;
		guesses = "";
		badGuesses = 0;
		nextButton.setEnabled(false);
		for (int i = 0; i < alphabetButtons.size(); i++) {
			alphabetButtons.get(i).setEnabled(true);
		}
		giveUpButton.setEnabled(true);
		int index = (int)(Math.random()*wordlist.getWordCount());
		word = wordlist.removeWord(index);
		word = word.toUpperCase();
		message = "The word has " + word.length() + " letters.  Let's play Hangman!";

		wordSoFar = ""; 
		for (int i = 0; i < word.length(); i ++ ){
			wordSoFar = " _ " + wordSoFar;
		}
		giveUpButton.setEnabled(true); 

	}







	/**
	 * This method can be called to test whether the user has guessed all the letters
	 * in the current secret word.  That would mean the user has won the game.
	 */
	private boolean wordIsComplete() {
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if ( guesses.indexOf(ch) == -1 ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This main program makes it possible to run this class as an application.  The main routine
	 * creates a window, sets it to contain a panel of type Hangman, and shows the window in the
	 * center of the screen.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Hangman"); // The window, with "Hangman" in the title bar.
		Hangman panel = new Hangman();  // The main panel for the window.
		window.setContentPane(panel);   // Set the main panel to be the content of the window
		window.pack();  // Set the size of the window based on the preferred sizes of what it contains.
		window.setResizable(false);  // Don't let the user resize the window.
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // End the program if the user closes the window.
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();  // The width/height of the screen.
		window.setLocation( (screen.width - window.getWidth())/2, 
				(screen.height - window.getHeight())/2 );  // Position window in the center of screen.
		window.setVisible(true);  // Make the window visible on the screen.
	}

} // end class Hangman
