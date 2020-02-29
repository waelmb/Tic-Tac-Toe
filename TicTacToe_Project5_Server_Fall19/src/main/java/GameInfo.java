import java.io.Serializable;
import java.util.ArrayList;


public class GameInfo  implements Serializable {
    int myCount;
    int score;
    ArrayList<Integer> scores;
    public String difficulty;
    boolean challengeRequest;
    boolean playAgainRequest;
    boolean playMoveRequest;
    int move;
    ArrayList<Integer> board;
    boolean gameWon;
    String winner;

    private static final long serialVersionUID = 5867735837149809485L;

    public GameInfo () {
        myCount = -1;
        score = 0;
        scores = new ArrayList<Integer>();
        difficulty = "None";
        challengeRequest = false;
        playAgainRequest = false;
        playMoveRequest = false;
        move = -1;
        board = new ArrayList<Integer>();
        //initialize empty spaced in the board
        for(int i = 0; i < 9; i++) {
            board.add(-1);
        }
        gameWon = false;
        winner = "None";
    }

    //prints content of a game info object
    public void printElems() {
        System.out.println("myCount: " + myCount);
        System.out.println("score: " + score);
        System.out.print("ArrayList<Integer> scores: ");
        if(scores == null) {
            System.out.println("empty");
        } else {
            for(int i = 0; i < scores.size(); i++) {
                System.out.print(scores.get(i) + " ");
            }
            System.out.println(" ");
        }
        System.out.println("difficulty: " + difficulty);
        System.out.println("challengeRequest: " + challengeRequest);
        System.out.println("playAgainRequest: " + playAgainRequest);
        System.out.println("playMoveRequest: " + playMoveRequest);
        System.out.println("move: " + move);
        System.out.println("ArrayList<Integer> board: 0 1 2 3 4 5 6 7 8");
        System.out.print("ArrayList<Integer> board: ");
        if(board == null) {
            System.out.println("empty");
        } else {
            for(int i = 0; i < board.size(); i++) {
                System.out.print(board.get(i) + " ");
            }
            System.out.println(" ");
        }
        System.out.println("gameWon: " + gameWon);
        System.out.println("winner: " + winner);
    }

    //resets a game info object
    public void reset() {
        scores.clear();
        difficulty = "None";
        challengeRequest = false;
        playAgainRequest = false;
        playMoveRequest = false;
        move = -1;
        board.clear();
        //initialize empty spaced in the board
        for(int i = 0; i < 9; i++) {
            board.add(-1);
        }
        gameWon = false;
        winner = "None";
    }

}
