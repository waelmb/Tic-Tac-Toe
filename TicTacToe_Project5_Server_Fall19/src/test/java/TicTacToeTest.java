import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

class TicTacToeTest {
	Server serverConnection;
	ArrayList<String> listItems;

	@BeforeEach
	void init() {
		serverConnection = new Server(5555);
		listItems = new ArrayList<String>();
	}

	@Test
	void convertBoardTest() {
		ArrayList<Integer> board = new ArrayList<Integer>();
		board.add(-1);
		board.add(0);
		board.add(1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);

		String[] expectedBoard = new String[]{"b","O","X","b","b","b","b","b","b"};
		assertArrayEquals(expectedBoard, serverConnection.findNextMove.convertBoard(board), "boards aren't the same");
	}

	@Test
	void getServerMoveTest() {
		ArrayList<Integer> board = new ArrayList<Integer>();
		/*	board
			O O X
			X O X
			b b b
		 */
		board.add(0);
		board.add(0);
		board.add(1);
		board.add(0);
		board.add(1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		assertEquals(6, serverConnection.findNextMove.getServerMove(board, "expert"));
	}

	@Test
	void getServerMoveTest2() {
		ArrayList<Integer> board = new ArrayList<Integer>();
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(-1);
		board.add(0);
		board.add(0);
		board.add(-1);
		board.add(1);
		board.add(1);
		board.add(0);
		board.add(1);
		board.add(0);
		assertEquals(3, serverConnection.findNextMove.getServerMove(board, "expert"));
	}

	@Test
	void nullMinMaxTest(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		AI_MinMax minMax;
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(-1);
		board.add(0);
		board.add(0);
		board.add(-1);
		board.add(1);
		board.add(1);
		board.add(0);
		board.add(1);
		board.add(0);

		//convert board into the compatible format
		String[] newBoard = serverConnection.findNextMove.convertBoard(board);

		//init minMax with compatible board
		minMax = new AI_MinMax(newBoard);

		assertNotNull(minMax, "minMax is null");
	}

	@Test
	void nullMovesTest(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		AI_MinMax minMax;
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(1);
		board.add(1);
		board.add(0);
		board.add(1);
		board.add(0);

		//convert board into the compatible format
		String[] newBoard = serverConnection.findNextMove.convertBoard(board);

		//init minMax with compatible board
		minMax = new AI_MinMax(newBoard);

		assertNotNull(minMax.getMovesList(), "getMovesList empty");
	}

	@Test
	void getMinMaxTest(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		AI_MinMax minMax;
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(1);
		board.add(1);
		board.add(0);
		board.add(1);
		board.add(0);

		//convert board into the compatible format
		String[] newBoard = serverConnection.findNextMove.convertBoard(board);

		//init minMax with compatible board
		minMax = new AI_MinMax(newBoard);

		ArrayList<Node> movesList = minMax.getMovesList();
		assertNotNull(movesList.get(0).getMinMax(), "getMinMax function incorrect ");
	}

	@Test
	void  movesListWinningTest(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		AI_MinMax minMax;
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);
		board.add(-1);

		//convert board into the compatible format
		String[] newBoard = serverConnection.findNextMove.convertBoard(board);

		//init minMax with compatible board
		minMax = new AI_MinMax(newBoard);

		ArrayList<Node> movesList = minMax.getMovesList();

		for(int i = 0; i < movesList.size(); i++) {
			if(movesList.get(i).getMinMax() == 10) {
				serverConnection.findNextMove.movesListWinning.add(movesList.get(i));
			}
		}
		assertNotNull(serverConnection.findNextMove.movesListWinning, "movesLisWinning is empty function incorrect ");
	}

	@Test
	void  movesListDrawTest(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		AI_MinMax minMax;
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(0);
		board.add(0);
		board.add(1);
		board.add(1);
		board.add(1);
		board.add(0);
		board.add(0);
		board.add(1);
		board.add(-1);

		//convert board into the compatible format
		String[] newBoard = serverConnection.findNextMove.convertBoard(board);

		//init minMax with compatible board
		minMax = new AI_MinMax(newBoard);

		ArrayList<Node> movesList = minMax.getMovesList();

		for(int i = 0; i < movesList.size(); i++) {
			if(movesList.get(i).getMinMax() == 0) {
				serverConnection.findNextMove.movesListDraw.add(movesList.get(i));
			}
		}
		assertNotNull(serverConnection.findNextMove.movesListDraw, "movesListDraw is empty function incorrect ");
	}

	@Test
	void  movesListLosingTest(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		AI_MinMax minMax;
		/* board
		   b O O
		   b X X
		   O X O
		 */
		board.clear();
		board.add(0);
		board.add(-1);
		board.add(0);
		board.add(-1);
		board.add(1);
		board.add(-1);
		board.add(0);
		board.add(-1);
		board.add(-1);

		//convert board into the compatible format
		String[] newBoard = serverConnection.findNextMove.convertBoard(board);

		//init minMax with compatible board
		minMax = new AI_MinMax(newBoard);

		ArrayList<Node> movesList = minMax.getMovesList();

		for(int i = 0; i < movesList.size(); i++) {
			if(movesList.get(i).getMinMax() == -10) {
				serverConnection.findNextMove.movesListLosing.add(movesList.get(i));
			}
		}
		assertNotNull(serverConnection.findNextMove.movesListLosing, "movesListLosing is empty function incorrect ");
	}

}
