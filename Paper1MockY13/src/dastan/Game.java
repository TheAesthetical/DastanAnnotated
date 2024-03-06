/*
    Skeleton Program for the AQA A Level Paper 1 Summer 2023 examination
    this code should be used in conjunction with the Preliminary Material
    written by the AQA Programmer Team
    developed in NetBeans IDE 12.6 environment

 */

package dastan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//================================================================================================
// Game
//================================================================================================

// Game class which acts as the main class
public class Game
{

	//+++++++++++++++++++++++++++++++++++++++++++++++
	// Main
	//+++++++++++++++++++++++++++++++++++++++++++++++

	public static void main(String[] args)
	{
		// Creating a new game
		Dastan ThisGame = new Dastan(6 , 6 , 4);
		// Procedure to go and start the playing of the game
		ThisGame.playGame();

		// Upon exiting the game, it says a goodbye message
		Console.writeLine("Goodbye!");
		Console.readLine();

	}

}

//================================================================================================
// Dastan
//================================================================================================

class Dastan
{
	protected List <Square> board;

	// Declaring the number of rows, columns and move option offer variables 
	protected int noOfRows , noOfColumns , moveOptionOfferPosition;

	// Array list that stores the players in the game
	protected List <Player> players = new ArrayList <>();
	// Array list that stores the move option offers in the game
	protected List <String> moveOptionOffer = new ArrayList <>();

	// Player object, used as a pointer to the current player
	protected Player currentPlayer;
	// Random object for RNG
	protected Random rGen = new Random();

	//===============================================
	// Constructor
	//===============================================

	// Parses in the number of rows, columns and pieces
	public Dastan(int r , int c , int noOfPieces)
	{
		// Adding new players to the game
		players.add(new Player("Player One" , 1));
		players.add(new Player("Player Two" , -1));

		// Creating and setting up the move options for the two players
		createMoveOptions();

		// Setting global variables as the number of columns/rows/pieces that have been parsed in
		noOfRows = r;
		noOfColumns = c;
		moveOptionOfferPosition = 0;

		// Initialising the moveOptionOffer array list with the offers to move the player can choose from
		createMoveOptionOffer();

		// Creates the Dastan board to be used in the game
		createBoard();

		// Creates the pieces on the board
		createPieces(noOfPieces);

		// Sets the current player who's turn it is
		currentPlayer = players.get(0);

	}

	//===============================================
	// Utilities
	//===============================================

	private void displayBoard()
	{
		Console.write(System.lineSeparator() + "   ");

		// For the length of the columns
		for (int column = 1 ; column <= noOfColumns ; column++)
		{
			// Print the column number and a space
			Console.write(column + "  ");

		}

		Console.write(System.lineSeparator() + "  ");

		// For the length of the columns
		for (int count = 1 ; count <= noOfColumns ; count++)
		{
			// Print three dashes
			Console.write("---");

		}

		Console.writeLine("-");

		// For the number of rows
		for (int row = 1 ; row <= noOfRows ; row++)
		{
			// Print out the number of the row and a space
			Console.write(row + " ");

			// For the number of columns 
			for (int column = 1 ; column <= noOfColumns ; column++)
			{
				// Set the index of the square with the row and column concatinated in the loop
				int index = getIndexOfSquare(row * 10 + column);
				// Prints out a | plus the symbol at the index square
				Console.write("|" + board.get(index).getSymbol());
				// Sets the piece in the square as the piece at the index
				Piece pieceInSquare = board.get(index).getPieceInSquare();

				// If there is no piece print a blank piece
				if (pieceInSquare == null)
				{
					Console.write(" ");

				}
				// Else, it is occupied and print the piece
				else
				{
					Console.write(pieceInSquare.getSymbol());

				}

			}

			// Pipe symbol to end the current line
			Console.writeLine("|");

		}

		// Start of ending bracket
		Console.write("  -");

		// For the length of the columns
		for (int column = 1 ; column <= noOfColumns ; column++)
		{
			// Print three dashes
			Console.write("---");

		}

		// Two lines written for whitespace purposes
		Console.writeLine();
		Console.writeLine();

	}

	private void displayState()
	{
		// Displays the board
		displayBoard();

		// Prints out the move options and the players turn
		Console.writeLine("Move option offer: " + moveOptionOffer.get(moveOptionOfferPosition));
		Console.writeLine();
		Console.writeLine(currentPlayer.getPlayerStateAsString());
		Console.writeLine("Turn: " + currentPlayer.getName());
		Console.writeLine();

	}

	private int getIndexOfSquare(int squareReference)
	{
		// Gets the tens column
		int row = squareReference / 10;
		// Gets the ones column
		int col = squareReference % 10;

		// Returns the ordered from left to right, top to bottom 
		// (see diagram below for better demonstration)
		// So an input of 36 would return 18
		return (row - 1) * noOfColumns + (col - 1);

		//		    1  2  3  4  5  6  
		//		   -------------------
		//		 1 | 1| 2| 3| 4| 5| 6|
		//		 2 | 7| 8| 9|10|11|12|
		//		 3 |13|14|15|16|17|18|
		//		 4 |19|20|21|22|23|24|
		//		 5 |25|26|27|28|29|30|
		//		 6 |31|32|33|34|35|36|
		//		   -------------------
		
		// It does this because it is a LIST a stream of items that snakes across the board
		// 1D data structure

	}

	private boolean checkSquareInBounds(int squareReference)
	{
		// Gets the row and square of the inputed coordinates respectfully
		int row = squareReference / 10;
		int col = squareReference % 10;

		// If the row is less than 1 or greater than the number of rows than it is invalid
		if (row < 1 || row > noOfRows)
		{
			return false;

		}
		// If the column is less than 1 or greater than the number of columns than it is invalid
		else if (col < 1 || col > noOfColumns)
		{
			return false;

		}
		// Else, it falls within these ranges and therefore is valid
		else
		{
			return true;

		}

	}

	private boolean checkSquareIsValid(int squareReference , boolean startSquare)
	{
		// Checks if the inputed square is in the bounds of the grid
		if (!checkSquareInBounds(squareReference))
		{
			return false;

		}

		// New piece that is the piece of the inputed square
		Piece pieceInSquare = board.get(getIndexOfSquare(squareReference)).getPieceInSquare();

		// If there is nothing currently in the players square
		if (pieceInSquare == null)
		{
			// If it is the start square then it is already occupied
			if (startSquare)
			{
				return false;

			}
			else
			{
				return true;

			}

		}
		// If the piece in the square belongs to the current player
		else if (currentPlayer.sameAs(pieceInSquare.getBelongsTo()))
		{
			// If it is in the start square than it is valid
			if (startSquare)
			{
				return true;

			}
			// ...Otherwise false
			else
			{
				return false;

			}

		}
		else
		{
			// If it is the start square then it is already occupied
			if (startSquare)
			{
				return false;

			}
			else
			{
				return true;

			}

		}

	}

	private boolean checkIfGameOver()
	{
		// Variables hold whether the player has the opposite players mirza (won)
		boolean player1HasMirza = false;
		boolean player2HasMirza = false;
		
		// For all the squares in the board
		for (Square s : board)
		{
			// The piece object is updated to contain the current piece in the square (if any)
			Piece pieceInSquare = s.getPieceInSquare();

			// If it doesn't contain nothing
			if (pieceInSquare != null)
			{
				// If the current square contains a kotla and the piece is equal to a mirza, and the mirza doesn't belong to the same player who owns the kotla
				if (s.containsKotla() && pieceInSquare.getTypeOfPiece().equals("mirza") && !pieceInSquare.getBelongsTo().sameAs(s.getBelongsTo()))
				{
					// Then the game has ended (doesn't specify who has won!)
					return true;

				}
				// If the piece in the square is a mirza and it belongs to player 1
				else if (pieceInSquare.getTypeOfPiece().equals("mirza") && pieceInSquare.getBelongsTo().sameAs(players.get(0)))
				{
					// Then currently, player 1 has a mirza
					player1HasMirza = true;

				}
				// If the piece in the square is a mirza and it belongs to player 2
				else if (pieceInSquare.getTypeOfPiece().equals("mirza") && pieceInSquare.getBelongsTo().sameAs(players.get(1)))
				{
					// Then currently, player 2 has a mirza
					player2HasMirza = true;

				}

			}

		}

		// Returns the opposite of both of 
		return !(player1HasMirza && player2HasMirza);

	}

	private int getSquareReference(String description)
	{
		// Selected square variable
		int selectedSquare;
		
		// Gets the user to enter the square coordinates
		Console.write("Enter the square " + description + " (row number followed by column number): ");
		selectedSquare = Integer.parseInt(Console.readLine());

		// Returns the value
		return selectedSquare;

	}

	private void useMoveOptionOffer()
	{
		// Temporary variable to store the replaced choice in
		int replaceChoice;
		
		//Asking the user to replace the current move from one in their queue
		Console.write("Choose the move option from your queue to replace (1 to 5): ");
		replaceChoice = Integer.parseInt(Console.readLine());
		
		// Updates the move option with the offer chosen (- 1 to get to 0 index) and creates the move option for the current offer using the position of the offer and the direction of the player
		currentPlayer.updateMoveOptionQueueWithOffer(replaceChoice - 1 , createMoveOption(moveOptionOffer.get(moveOptionOfferPosition) , currentPlayer.getDirection()));
		// Sets the players score
		currentPlayer.changeScore(-(10 - (replaceChoice * 2)));
		// The move offer is indexed into a random position
		moveOptionOfferPosition = rGen.nextInt(5);

	}

	private int getPointsForOccupancyByPlayer(Player currentPlayer)
	{
		int scoreAdjustment = 0;

		// For all the squares in the board
		for (Square s : board)
		{
			// Add to the score adjustment the number of points for occupancy the current player holds
			scoreAdjustment += (s.getPointsForOccupancy(currentPlayer));

		}
		
		// Returns the score adjustment based on the calculation done above
		return scoreAdjustment;

	}

	private void updatePlayerScore(int pointsForPieceCapture)
	{
		// Score is incremented by the points the player gets for occupying the square, plus the points 
		currentPlayer.changeScore(getPointsForOccupancyByPlayer(currentPlayer) + pointsForPieceCapture);

	}

	private int calculatePieceCapturePoints(int finishSquareReference)
	{
		// If the piece in the square index is not nothing
		if (board.get(getIndexOfSquare(finishSquareReference)).getPieceInSquare() != null)
		{
			// Returns the points if captured
			return board.get(getIndexOfSquare(finishSquareReference)).getPieceInSquare().getPointsIfCaptured();

		}
		
		// Otherwise, it is empty, and therefore it will return no points
		return 0;

	}

	public void playGame()
	{
		// Sets the game over variable to false
		boolean gameOver = false;

		// While the game is not over
		while (!gameOver)
		{
			// Displays the board and general game state
			displayState();
			// Resets game variables
			boolean squareIsValid = false;
			int choice;

			do
			{
				// Asks the user to input their choice and converts it to int
				Console.write("Choose move option to use from queue (1 to 3) or 9 to take the offer: ");
				choice = Integer.parseInt(Console.readLine());

				// 9 takes the current offer
				if (choice == 9)
				{
					// Uses the move offer that is currently active
					useMoveOptionOffer();
					// Displays the games updated state
					displayState();

				}

				// While the choice is less than 1 or greater than 3 (not valid)
			} while (choice < 1 || choice > 3);
			
			// Reference for the starting square
			int startSquareReference = 0;

			// If the square is not valid
			while (!squareIsValid)
			{
				// Returns the value of the square reference pertaining to the piece to move that is inputed by the user
				startSquareReference = getSquareReference("containing the piece to move");
				// Checks if the square to move the piece from is valid in the starting state
				squareIsValid = checkSquareIsValid(startSquareReference , true);

			}
			// Variables to hold the state for the square after the move
			int finishSquareReference = 0;
			squareIsValid = false;

			// While the square chosen is not valid for validation
			while (!squareIsValid)
			{
				// Asks the user what square they would like to move to
				finishSquareReference = getSquareReference("to move to");
				// Checks if the square chosen by the user is valid to move to 
				squareIsValid = checkSquareIsValid(finishSquareReference , false);

			}
			// Calculates if this is a legal move in this boolean
			boolean moveLegal = currentPlayer.checkPlayerMove(choice , startSquareReference , finishSquareReference);

			// If it is legal
			if (moveLegal)
			{
				// Calculating points for the pieces capture (if any!)
				int pointsForPieceCapture = calculatePieceCapturePoints(finishSquareReference);
				// Changes the players score based on the choice level of the player
				currentPlayer.changeScore(-(choice + (2 * (choice - 1))));
				// Updates the players queue of moves after they have taken their move based on their choice
				currentPlayer.updateQueueAfterMove(choice);
				// The board is updated based on what has been moved
				updateboard(startSquareReference , finishSquareReference);
				// Players score is then subsequently updated based on the piece they have captured
				updatePlayerScore(pointsForPieceCapture);
				// Displaying new score
				Console.writeLine("New score: " + currentPlayer.getScore() + System.lineSeparator());

			}

			// Now that the player is done with their turn, swap the current player
			if (currentPlayer.sameAs(players.get(0)))
			{
				currentPlayer = players.get(1);

			}
			else
			{
				currentPlayer = players.get(0);

			}
			
			// Updates game over status required for the loop to continue/stop the game
			gameOver = checkIfGameOver();

		}
		
		displayState();
		displayFinalResult();

	}

	private void updateboard(int startSquareReference , int finishSquareReference)
	{
		// The square of the finishing state is replaced with the piece at the starting state, in which the starting state square is then subsequently reset
		board.get(getIndexOfSquare(finishSquareReference)).setPiece(board.get(getIndexOfSquare(startSquareReference)).removePiece());

	}

	private void displayFinalResult()
	{

		if (players.get(0).getScore() == players.get(1).getScore())
		{
			Console.writeLine("Draw!");

		}
		else if (players.get(0).getScore() > players.get(1).getScore())
		{
			Console.writeLine(players.get(0).getName() + " is the winner!");

		}
		else
		{
			Console.writeLine(players.get(1).getName() + " is the winner!");

		}

	}

	private void createBoard()
	{
		// Square object temporary object template
		Square s;
		// Initialising the board as an array list
		board = new ArrayList <>();

		// Loops from row 1 up until the total number of rows in the board
		for (int row = 1 ; row <= noOfRows ; row++)
		{
			// Loops from column 1 up until the total number of columns in the board
			for (int column = 1 ; column <= noOfColumns ; column++)
			{
				// If it is the first row and the column is equal to half of the total columns
				if (row == 1 && column == noOfColumns / 2)
				{
					// The first players Kotla gets added in the square on the board
					s = new Kotla(players.get(0) , "K");

				}
				// If it is the last row and the column is equal to half of the total columns + 1
				else if (row == noOfRows && column == noOfColumns / 2 + 1)
				{
					// The second players Kotla gets added in the square on the board
					s = new Kotla(players.get(1) , "k");

				}
				else
				{
					// An empty square is added if it is nothing else
					s = new Square();

				}

				// The square that has just been initialised is then added to the board
				board.add(s);

			}

		}

	}

	private void createPieces(int noOfPieces)
	{
		// Piece object that temporarily holds the current puece
		Piece currentPiece;

		// PLayer 1

		// For one piece to the total number of pieces
		for (int count = 1 ; count <= noOfPieces ; count++)
		{
			// Initialises the current piece as one of player 1's pieces
			// Denoted by !
			currentPiece = new Piece("piece" , players.get(0) , 1 , "!");

			// Adds 1 to make it pointing at the second square instead of the first one
			// 2 * 10 to get the row and column right, which is added to the count to increment 1 position each time
			// Sets the piece to the board square
			board.get(getIndexOfSquare(2 * 10 + count + 1)).setPiece(currentPiece);

		}

		// Sets the current piece to a Mirza that is player 1s which is depicted with a '1'
		currentPiece = new Piece("mirza" , players.get(0) , 5 , "1");
		// Sets the square of the board that is the middle of the first column (noOfColumns / 2)
		board.get(getIndexOfSquare(10 + noOfColumns / 2)).setPiece(currentPiece);

		// Player 2

		// For one piece to the total number of pieces
		for (int count = 1 ; count <= noOfPieces ; count++)
		{
			// Initialises the current piece as one of player 2's pieces
			// Denoted by \
			currentPiece = new Piece("piece" , players.get(1) , 1 , "\"");

			// Adds 1 to make it pointing at the second square instead of the first one
			// Number of total rows - 1 * 10 to get the row and column of the last row, which is added to the count to increment 1 position each time
			// Sets the piece to the board square	
			board.get(getIndexOfSquare((noOfRows - 1) * 10 + count + 1)).setPiece(currentPiece);

		}

		// Sets the current piece to a Mirza that is player 2s which is depicted with a '2'
		currentPiece = new Piece("mirza" , players.get(1) , 5 , "2");
		// Sets the square of the board that is in the middle of the last (noOfRows * 10) column (noOfColumns / 2)
		// Plus 1 to set it pointing to a column next to the half of the total columns (so it isn't directly opposite player 1)
		board.get(getIndexOfSquare(noOfRows * 10 + (noOfColumns / 2 + 1))).setPiece(currentPiece);

	}

	private void createMoveOptionOffer()
	{
		// Adding the move option offers that the players can choose from to the array list
		moveOptionOffer.add("jazair");
		moveOptionOffer.add("chowkidar");
		moveOptionOffer.add("cuirassier");
		moveOptionOffer.add("ryott");
		moveOptionOffer.add("faujdar");

	}

	private MoveOption createRyottMoveOption(int direction)
	{
		// Creates a new move option object for Cuirassier
		MoveOption newMoveOption = new MoveOption("ryott");

		// Initialises a new move object
		Move newMove = new Move(0 , 1 * direction);
		// And adds that to the list of possible moves
		newMoveOption.addToPossibleMoves(newMove);

		// Repeats that for all of the moves that can be made with Cuirassier

		newMove = new Move(0 , -1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(1 * direction , 0);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(-1 * direction , 0);
		newMoveOption.addToPossibleMoves(newMove);

		// Returns the move option object
		return newMoveOption;

	}

	private MoveOption createFaujdarMoveOption(int direction)
	{
		// Creates a new move option object for Cuirassier
		MoveOption newMoveOption = new MoveOption("faujdar");

		// Initialises a new move object
		Move newMove = new Move(0 , -1 * direction);
		// And adds that to the list of possible moves
		newMoveOption.addToPossibleMoves(newMove);

		// Repeats that for all of the moves that can be made with Cuirassier

		newMove = new Move(0 , 1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(0 , 2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(0 , -2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		// Returns the move option object
		return newMoveOption;

	}

	private MoveOption createJazairMoveOption(int direction)
	{
		// Creates a new move option object for Cuirassier
		MoveOption newMoveOption = new MoveOption("jazair");

		// Initialises a new move object
		Move newMove = new Move(2 * direction , 0);
		// And adds that to the list of possible moves
		newMoveOption.addToPossibleMoves(newMove);

		// Repeats that for all of the moves that can be made with Cuirassier

		newMove = new Move(2 * direction , -2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(2 * direction , 2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(0 , 2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(0 , -2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(-1 * direction , -1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(-1 * direction , 1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		// Returns the move option object
		return newMoveOption;

	}

	private MoveOption createCuirassierMoveOption(int direction)
	{
		// Creates a new move option object for Cuirassier
		MoveOption newMoveOption = new MoveOption("cuirassier");

		// Initialises a new move object
		Move newMove = new Move(1 * direction , 0);
		// And adds that to the list of possible moves
		newMoveOption.addToPossibleMoves(newMove);

		// Repeats that for all of the moves that can be made with Cuirassier

		newMove = new Move(2 * direction , 0);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(1 * direction , -2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(1 * direction , 2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		// Returns the move option object
		return newMoveOption;

	}

	private MoveOption createChowkidarMoveOption(int direction)
	{
		// Creates a new move option object for Chowkidar
		MoveOption newMoveOption = new MoveOption("chowkidar");

		// Initialises a new move object
		Move newMove = new Move(1 * direction , 1 * direction);
		// And adds that to the list of possible moves
		newMoveOption.addToPossibleMoves(newMove);

		// Repeats that for all of the moves that can be made with Chowkidar

		newMove = new Move(1 * direction , -1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(-1 * direction , 1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(-1 * direction , -1 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(0 , 2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		newMove = new Move(0 , -2 * direction);
		newMoveOption.addToPossibleMoves(newMove);

		// Returns the move option object
		return newMoveOption;

	}

	private MoveOption createMoveOption(String name , int direction)
	{
		// Switches the name of the piece
		switch (name)
		{
		case "chowkidar":
			return createChowkidarMoveOption(direction);

		case "ryott":
			return createRyottMoveOption(direction);

		case "faujdar":
			return createFaujdarMoveOption(direction);

		case "jazair":
			return createJazairMoveOption(direction);

			// Default case assumes a specific piece. Interesting design choice...
		default:
			return createCuirassierMoveOption(direction);

		}

	}

	private void createMoveOptions()
	{
		// Creating the move options for each player and adding them to the queue sequentially

		//creatMoveOption([the name of the piece] , whether it goes up or down)

		// Player 1
		players.get(0).addToMoveOptionQueue(createMoveOption("ryott" , 1));
		players.get(0).addToMoveOptionQueue(createMoveOption("chowkidar" , 1));
		players.get(0).addToMoveOptionQueue(createMoveOption("cuirassier" , 1));
		players.get(0).addToMoveOptionQueue(createMoveOption("faujdar" , 1));
		players.get(0).addToMoveOptionQueue(createMoveOption("jazair" , 1));

		// Player 2
		players.get(1).addToMoveOptionQueue(createMoveOption("ryott" , -1));
		players.get(1).addToMoveOptionQueue(createMoveOption("chowkidar" , -1));
		players.get(1).addToMoveOptionQueue(createMoveOption("jazair" , -1));
		players.get(1).addToMoveOptionQueue(createMoveOption("faujdar" , -1));
		players.get(1).addToMoveOptionQueue(createMoveOption("cuirassier" , -1));

	}

}

//================================================================================================
// Piece
//================================================================================================

class Piece
{
	protected String typeOfPiece , symbol;
	protected int pointsIfCaptured;
	protected Player belongsTo;

	//===============================================
	// Constructor
	//===============================================

	public Piece(String t , Player b , int p , String s)
	{
		typeOfPiece = t;
		belongsTo = b;
		pointsIfCaptured = p;
		symbol = s;

	}

	//===============================================
	// Getters
	//===============================================

	public String getSymbol()
	{
		return symbol;

	}

	public String getTypeOfPiece()
	{
		return typeOfPiece;

	}

	public Player getBelongsTo()
	{
		return belongsTo;

	}

	public int getPointsIfCaptured()
	{
		return pointsIfCaptured;

	}

}

//================================================================================================
// Square
//================================================================================================

class Square
{
	// Stores the symbol in the square
	protected String symbol;
	// Stores the piece that is currently in the square
	protected Piece pieceInSquare;
	// Stores who owns the square
	protected Player belongsTo;

	//===============================================
	// Constructor
	//===============================================

	public Square()
	{
		// Resetting variables
		pieceInSquare = null;
		belongsTo = null;
		symbol = " ";

	}

	//===============================================
	// Utilities
	//===============================================

	public void setPiece(Piece p)
	{
		pieceInSquare = p;

	}

	public Piece removePiece()
	{
		Piece pieceToReturn = pieceInSquare;
		pieceInSquare = null;
		return pieceToReturn;

	}

	public Piece getPieceInSquare()
	{
		return pieceInSquare;

	}

	public String getSymbol()
	{
		return symbol;

	}

	public int getPointsForOccupancy(Player currentPlayer)
	{
		return 0;

	}

	public Player getBelongsTo()
	{
		return belongsTo;

	}

	public boolean containsKotla()
	{

		if (symbol.equals("K") || symbol.equals("k"))
		{
			return true;

		}
		else
		{
			return false;

		}

	}

}

//================================================================================================
// Kotla
//================================================================================================

class Kotla extends Square
{
	//===============================================
	// Constructor
	//===============================================

	public Kotla(Player p , String s)
	{
		// Calls the inherited classes constructor (square)
		super();

		// Assigning the square to the player
		belongsTo = p;
		// Putting the symbol in the square
		symbol = s;

	}

	//===============================================
	// Utilities
	//===============================================

	@Override
	public int getPointsForOccupancy(Player currentPlayer)
	{

		if (pieceInSquare == null)
		{
			return 0;

		}
		else if (belongsTo.sameAs(currentPlayer))
		{

			if (currentPlayer.sameAs(pieceInSquare.getBelongsTo()) && (pieceInSquare.getTypeOfPiece().equals("piece") || pieceInSquare.getTypeOfPiece().equals("mirza")))
			{
				return 5;

			}
			else
			{
				return 0;

			}

		}
		else
		{

			if (currentPlayer.sameAs(pieceInSquare.getBelongsTo()) && (pieceInSquare.getTypeOfPiece().equals("piece") || pieceInSquare.getTypeOfPiece().equals("mirza")))
			{
				return 1;

			}
			else
			{
				return 0;

			}

		}

	}

}

//================================================================================================
// MoveOption
//================================================================================================

// Class to store all move options associated with a specific piece
class MoveOption
{
	// Global variable to store the name of the piece 
	protected String name;

	//Stores all possible moves that can be made with the particular piece
	protected List <Move> possibleMoves;

	//===============================================
	// Constructor
	//===============================================

	public MoveOption(String n)
	{
		name = n;
		possibleMoves = new ArrayList <>();

	}

	//===============================================
	// Utilities
	//===============================================

	public void addToPossibleMoves(Move m)
	{
		possibleMoves.add(m);

	}

	// Getter for the name of the move
	public String getName()
	{
		return name;

	}

	public boolean checkIfThereIsAMoveToSquare(int startSquareReference , int finishSquareReference)
	{
		// Gets the rows and columns of the start and end squares
		int startRow = startSquareReference / 10;
		int startColumn = startSquareReference % 10;
		int finishRow = finishSquareReference / 10;
		int finishColumn = finishSquareReference % 10;

		// For a new move up to the total amount of possible moves
		for (Move m : possibleMoves)
		{
			// Checks if the requested move is valid
			if (startRow + m.getRowChange() == finishRow && startColumn + m.getColumnChange() == finishColumn)
			{
				// If it is, returns true
				return true;

			}

		}
	
		// Otherwise it is not (false)
		return false;

	}

}

//================================================================================================
// Move
//================================================================================================

class Move
{
	// Initialising globals for the class
	protected int rowChange , columnChange;

	//===============================================
	// Constructor
	//===============================================

	// Parses in and assigns the row and column change for the specific move
	Move(int r , int c)
	{
		rowChange = r;
		columnChange = c;

	}

	//===============================================
	// Utilities
	//===============================================

	public int getRowChange()
	{
		return rowChange;

	}

	public int getColumnChange()
	{
		return columnChange;

	}

}

//================================================================================================
// MoveOptionQueue
//================================================================================================

class MoveOptionQueue
{
	private List <MoveOption> queue = new ArrayList <>();

	//===============================================
	// Utilities
	//===============================================

	public String getQueueAsString()
	{
		String queueAsString = "";
		int count = 1;

		for (MoveOption m : queue)
		{
			queueAsString += count + ". " + m.getName() + "   ";
			count += 1;

		}
		return queueAsString;

	}

	public void add(MoveOption newMoveOption)
	{
		queue.add(newMoveOption);

	}

	public void replace(int position , MoveOption newMoveOption)
	{
		// Sets the index of the queue as the new move option
		queue.set(position , newMoveOption);

	}

	public void moveItemToBack(int position)
	{
		// Assigns the item that needs to be moved to the back to a temporary move option
		MoveOption temp = queue.get(position);
		// It is then removed from the queue
		queue.remove(position);
		// And then re added to the queue at the back
		queue.add(temp);

	}

	//===============================================
	// Constructor
	//===============================================

	public MoveOption getMoveOptionInPosition(int pos)
	{
		// Returns the move option in the queue at the requested position
		return queue.get(pos);

	}

}

//================================================================================================
// Player
//================================================================================================

class Player
{
	private String name;
	private int direction , score;
	private MoveOptionQueue queue = new MoveOptionQueue();

	//===============================================
	// Constructor
	//===============================================

	// Parses in the name of the player and the direction they are going in
	public Player(String n , int d)
	{
		// Starting score
		score = 100;

		name = n;
		direction = d;

	}

	//===============================================
	// Utilities
	//===============================================

	public boolean sameAs(Player APlayer)
	{

		if (APlayer == null)
		{
			return false;

		}
		else if (APlayer.getName().equals(name))
		{
			return true;

		}
		else
		{
			return false;

		}

	}

	public String getPlayerStateAsString()
	{
		return name + System.lineSeparator() + "Score: " + score + System.lineSeparator() + "Move option queue: " + queue.getQueueAsString() + System.lineSeparator();

	}

	// Adds a new move option to the players queue of moves
	public void addToMoveOptionQueue(MoveOption newMoveOption)
	{
		// Adding it to the queue
		queue.add(newMoveOption);

	}

	public void updateQueueAfterMove(int position)
	{
		// Updates the queue to move the selected item to the back of the queue
		// Minus 1 to index it to 0 (directly because of user input)
		queue.moveItemToBack(position - 1);

	}

	public void updateMoveOptionQueueWithOffer(int position , MoveOption newMoveOption)
	{
		// Replaces the new move option with the move in the position
		queue.replace(position , newMoveOption);

	}

	public int getScore()
	{
		return score;

	}

	public String getName()
	{
		return name;

	}

	public int getDirection()
	{
		return direction;

	}

	// Doesn't set, just adds
	public void changeScore(int amount)
	{
		// Adds the amount requested to the players score
		score += amount;

	}

	public boolean checkPlayerMove(int pos , int startSquareReference , int finishSquareReference)
	{
		// Temporarily stores the move option selected by the user
		// Minus 1 to index it to 0 (directly because of user input)
		MoveOption temp = queue.getMoveOptionInPosition(pos - 1);
		// Checks if the move is valid and returns it
		return temp.checkIfThereIsAMoveToSquare(startSquareReference , finishSquareReference);

	}

}